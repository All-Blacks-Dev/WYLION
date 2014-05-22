package com.allblacks.views;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.KeepOnView;
import com.google.android.music.download.IntentConstants;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.*;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.Store;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.utils.*;
import com.google.android.music.utils.async.AsyncRunner;
import java.util.*;

// Referenced classes of package com.google.android.music.ui:
//            BaseListFragment, UIStateManager, BaseActionButton, ScreenMenuHandler,
//            ActionBarController, ArtistPageActivity

public class HeaderView extends RelativeLayout
        implements View.OnClickListener, AbsListView.OnScrollListener
{
    private class ArtHelper
            implements com.google.android.music.utils.BitmapDiskCache.Callback
    {

        public String getViewName()
        {
            if(mViewId == 0x7f090081)
                return "view1";
            else
                return "view2";
        }

        public boolean isViewReady()
        {
            return mViewReady;
        }

        public void onBitmapResult(String s, Bitmap bitmap, Exception exception)
        {
            if(!s.equals(mRequestedUrl))
            {
                if(HeaderView.LOGV)
                    Log.w("ContainerHeader", (new StringBuilder()).append("Ignoring stale result in onBitmapResult for ").append(getViewName()).append(" received: ").append(Store.generateId(s)).append(" requested: ").append(Store.generateId(mRequestedUrl)).toString());
            } else
            {
                if(HeaderView.LOGV)
                    Log.d("ContainerHeader", (new StringBuilder()).append("onBitmapResult for ").append(getViewName()).append(" time: ").append(SystemClock.uptimeMillis() - mRequestTime).append(" id: ").append(Store.generateId(s)).toString());
                if(bitmap != null && !mArtClearedOnStop)
                {
                    if(mViewId == 0x7f090081)
                    {
                        mArtistArt1.setImageBitmap(bitmap);
                        mArtistArt1ShowingNonDefaultArt = true;
                    } else
                    {
                        mArtistArt2.setImageBitmap(bitmap);
                    }
                    mViewReady = true;
                    return;
                }
            }
        }

        public void requestBitmap(String s)
        {
            mRequestedUrl = s;
            mViewReady = false;
            mRequestTime = SystemClock.uptimeMillis();
            BitmapDiskCache.getInstance(getContext()).getBitmap(mRequestedUrl, this);
        }

        private long mRequestTime;
        private String mRequestedUrl;
        private final int mViewId;
        private boolean mViewReady;

        ArtHelper(int i)
        {
            mViewId = i;
        }
    }


    public HeaderView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mArtistId = -1L;
        mShowingFirstSlide = false;
        mSlideShowInitialized = false;
        mArtClearedOnStop = false;
        mIsAddedToLibraryObserverRegistered = false;
    }

    private void clearArtistArt(ImageView imageview)
    {
        Drawable drawable = imageview.getDrawable();
        if(drawable != null && (drawable instanceof BitmapDrawable))
            ((BitmapDrawable)drawable).getBitmap().recycle();
        imageview.setImageDrawable(null);
    }

    private void disablePinning()
    {
        mPinButton.setVisibility(8);
        mPinButton.setOnClick(false);
    }

    private void enablePinning()
    {
        mPinButton.setVisibility(0);
        mPinButton.setSongList(mSongList);
        mPinButton.setOnClick(true);
    }

    private void extractArtistArtUrls(Cursor cursor, int i)
    {
        long l = SystemClock.uptimeMillis();
        int j = cursor.getColumnIndexOrThrow("ArtistArtLocation");
        int k = cursor.getPosition();
        cursor.moveToPosition(-1);
        HashSet hashset = new HashSet();
        int i1 = 0;
        while(cursor.moveToNext() && hashset.size() < i && i1 < 1000)
        {
            String s;
            if(j >= 0 && !cursor.isNull(j))
                s = cursor.getString(j);
            else
                s = null;
            if(!TextUtils.isEmpty(s))
                hashset.add(s);
            i1++;
        }
        if(!cursor.moveToPosition(k) && k != -1)
            Log.w("ContainerHeader", (new StringBuilder()).append("Failed to restore cursor position. Current pos=").append(cursor.getPosition()).toString());
        mArtistArtUrls = new ArrayList(hashset.size());
        mArtistArtUrls.addAll(hashset);
        if(LOGV)
        {
            Object aobj[] = new Object[3];
            aobj[0] = Integer.valueOf(mArtistArtUrls.size());
            aobj[1] = Long.valueOf(SystemClock.uptimeMillis() - l);
            aobj[2] = Integer.valueOf(i1);
            Log.d("ContainerHeader", String.format("Gathering %d artist urls took: %d ms. Looked at %d rows.", aobj));
        }
    }

    private String getFirstArtistUrl()
    {
        mCurrentArtistArtUrlIndex = 0;
        return (String)mArtistArtUrls.get(mCurrentArtistArtUrlIndex);
    }

    private String getNextArtistUrl()
    {
        mCurrentArtistArtUrlIndex = (1 + mCurrentArtistArtUrlIndex) % mArtistArtUrls.size();
        return (String)mArtistArtUrls.get(mCurrentArtistArtUrlIndex);
    }

    private boolean isOnDeviceOnlyMode()
    {
        return mMusicPreferences != null && mMusicPreferences.getDisplayOptions() == 1;
    }

    private void setupArtistArtSlideShow(Cursor cursor)
    {
        if(!mSlideShowInitialized && cursor != null && MusicUtils.hasCount(cursor))
        {
            if(mSlideShowHandler == null)
                mSlideShowHandler = new Handler();
            if(mSlideSwitchRunnable == null)
                mSlideSwitchRunnable = new Runnable() {

                    public void run()
                    {
                        mSlideShowHandler.removeCallbacks(mSlideSwitchRunnable);
                        mSlideShowHandler.removeCallbacks(mSlideRefreshRunnable);
                        ArtHelper arthelper;
                        HeaderView containerheaderview;
                        boolean flag;
                        if(mShowingFirstSlide)
                            arthelper = mArtistArtHelper2;
                        else
                            arthelper = mArtistArtHelper1;
                        if(HeaderView.LOGV)
                            Log.d("ContainerHeader", (new StringBuilder()).append("Slide switch. Switching to: ").append(arthelper.getViewName()).append(" is ready: ").append(arthelper.isViewReady()).toString());
                        mViewFlipper.showNext();
                        containerheaderview = HeaderView.this;
                        if(!mShowingFirstSlide)
                            flag = true;
                        else
                            flag = false;
                        containerheaderview.mShowingFirstSlide = flag;
                        mSlideShowHandler.postDelayed(mSlideRefreshRunnable, 1600L);
                        mSlideShowHandler.postDelayed(mSlideSwitchRunnable, 5000L);
                    }

                };
            if(mSlideRefreshRunnable == null)
                mSlideRefreshRunnable = new Runnable() {

                    public void run()
                    {
                        if(mArtistArtUrls == null || mArtistArtUrls.isEmpty())
                        {
                            Log.e("ContainerHeader", "mSlideRefreshRunnable: the artist url list is empty");
                            return;
                        }
                        if(HeaderView.LOGV)
                            Log.d("ContainerHeader", (new StringBuilder()).append("Slide refresh. Showing first slide=").append(mShowingFirstSlide).toString());
                        ImageView imageview;
                        ArtHelper arthelper;
                        if(mShowingFirstSlide)
                            imageview = mArtistArt2;
                        else
                            imageview = mArtistArt1;
                        if(mShowingFirstSlide)
                            arthelper = mArtistArtHelper2;
                        else
                            arthelper = mArtistArtHelper1;
                        clearArtistArt(imageview);
                        arthelper.requestBitmap(getNextArtistUrl());
                    }

                };
            Collections.shuffle(mArtistArtUrls);
            mSlideShowInitialized = true;
            startArtistArtSlideShow();
        }
    }

    private boolean shouldDoArtistSlideShow()
    {
        return shouldTryArtistSlideShow() && mArtistArtUrls != null && mArtistArtUrls.size() >= 2;
    }

    private boolean shouldShowSongCount()
    {
        if(mSongList == null)
            return false;
        if(mSongList instanceof PlaylistSongList)
        {
            boolean flag;
            if(((PlaylistSongList)mSongList).getPlaylistType() != 50)
                flag = true;
            else
                flag = false;
            return flag;
        } else
        {
            return true;
        }
    }

    private boolean shouldTryArtistSlideShow()
    {
        return (mSongList instanceof PlaylistSongList) || (mSongList instanceof AutoPlaylist) || (mSongList instanceof SharedWithMeSongList);
    }

    private void showButtonIfXLarge(View view)
    {
        if(mFragment.getPreferences().isXLargeScreen())
            view.setVisibility(0);
    }

    private void showOwnerProfilePicture(String s)
    {
        if(UIStateManager.getInstance().isNetworkConnected() && !TextUtils.isEmpty(s))
        {
            mOwnerPhoto.setVisibility(0);
            mOwnerPhoto.setExternalAlbumArt(s);
        }
    }

    private void startArtistArtSlideShow()
    {
        if(mSlideShowInitialized && mArtistArtUrls != null)
            if(mArtistArtUrls.size() < 2)
            {
                if(LOGV)
                {
                    Log.d("ContainerHeader", "Not enough artists to do a slide show");
                    return;
                }
            } else
            {
                if(mViewFlipper.getDisplayedChild() != 0)
                    mViewFlipper.showNext();
                String s = getFirstArtistUrl();
                mArtistArtHelper1.requestBitmap(s);
                String s1 = getNextArtistUrl();
                mArtistArtHelper2.requestBitmap(s1);
                mViewFlipper.setInAnimation(getContext(), 0x7f050006);
                mViewFlipper.setOutAnimation(getContext(), 0x7f050007);
                mViewFlipper.getInAnimation().setDuration(1500L);
                mViewFlipper.getOutAnimation().setDuration(1500L);
                mShowingFirstSlide = true;
                mSlideShowHandler.removeCallbacks(mSlideSwitchRunnable);
                mSlideShowHandler.removeCallbacks(mSlideRefreshRunnable);
                mSlideShowHandler.postDelayed(mSlideSwitchRunnable, 5000L);
                return;
            }
    }

    private void updateActionButtonsVisibility()
    {
        if(mSongList instanceof PlaylistSongList)
        {
            showButtonIfXLarge(mShuffle);
        } else
        {
            if(mSongList instanceof SharedWithMeSongList)
            {
                mFollowPlaylist.setVisibility(0);
                mFollowPlaylist.setActionButtonListener(new BaseActionButton.ActionButtonListener() {

                    public void onActionFinish()
                    {
                        mFollowPlaylist.setVisibility(8);
                    }

                    public void onActionStart()
                    {
                    }

                });
                showButtonIfXLarge(mShuffle);
                return;
            }
            if(mSongList instanceof NautilusSongList)
            {
                if(!mIsAddedToLibraryObserverRegistered)
                {
                    mIsAddedToLibraryObserverRegistered = true;
                    getContext().getContentResolver().registerContentObserver(MusicContent.CONTENT_URI, false, mAddedToLibraryStateObserver);
                }
                mAddToLibrary.setVisibility(4);
                MusicUtils.runAsyncWithCallback(mUpdateAddToLibraryButtonRunner);
                return;
            }
            if(!(mSongList instanceof SharedSongList))
            {
                if(mSongList instanceof AutoPlaylist)
                {
                    showButtonIfXLarge(mShuffle);
                    return;
                }
                if((mSongList instanceof AlbumSongList) || (mSongList instanceof ArtistSongList))
                {
                    if(UIStateManager.getInstance().getPrefs().isStreamingEnabled())
                        showButtonIfXLarge(mPlayRadio);
                    showButtonIfXLarge(mShuffle);
                    return;
                }
                if(mSongList instanceof GenreSongList)
                {
                    showButtonIfXLarge(mShuffle);
                    return;
                }
            }
        }
    }

    private void updateAddToLibraryButtonVisibility(boolean flag)
    {
        int i;
        if(flag)
            i = 0;
        else
            i = 8;
        if(mAddToLibrary.getVisibility() != i)
        {
            mAddToLibrary.setVisibility(i);
            if(!flag)
            {
                mAddedToLibBadge.setVisibility(0);
            } else
            {
                mAddedToLibBadge.setVisibility(8);
                mAddToLibrary.setActionButtonListener(new BaseActionButton.ActionButtonListener() {

                    public void onActionFinish()
                    {
                        mAlbumArt.setAvailable(true);
                        mAddToLibSpinner.setVisibility(8);
                    }

                    public void onActionStart()
                    {
                        mAlbumArt.setAvailable(false);
                        mAddToLibSpinner.setVisibility(0);
                    }

                });
            }
        }
        showButtonIfXLarge(mPlayRadio);
        showButtonIfXLarge(mShuffle);
    }

    public int getAlbumArtHeight()
    {
        return mAlbumArt.getHeight();
    }

    public boolean isArtistArtShown()
    {
        return mViewFlipper.getVisibility() == 0;
    }

    public void onClick(final View v)
    {
        final Context context = getContext();
        if(v != mAlbumArt) goto _L2; else goto _L1
        _L1:
        MusicUtils.playMediaList(mSongList, -1);
        _L4:
        return;
        _L2:
        String s;
        if(v != mBuyButton)
            continue; /* Loop/switch isn't completed */
        s = mSongList.getStoreUrl();
        if(TextUtils.isEmpty(s)) goto _L4; else goto _L3
        _L3:
        context.startActivity(IntentConstants.getStoreBuyIntent(context, s));
        return;
        if(v != mOverflow) goto _L4; else goto _L5
        _L5:
        if(mContainerDocument != null)
        {
            (new ScreenMenuHandler(mFragment, mContainerDocument, ScreenMenuHandler.ScreenMenuType.TRACK_CONTAINER)).showPopupMenu(v);
            return;
        } else
        {
            MusicUtils.runAsyncWithCallback(new AsyncRunner() {

                public void backgroundTask()
                {
                    mContainerDocument = Document.fromSongList(context, mSongList);
                }

                public void taskCompleted()
                {
                    if(mFragment.getActivity() == null)
                    {
                        return;
                    } else
                    {
                        (new ScreenMenuHandler(mFragment, mContainerDocument, ScreenMenuHandler.ScreenMenuType.TRACK_CONTAINER)).showPopupMenu(v);
                        return;
                    }
                }

            });
            return;
        }
    }

    public void onDestroyView()
    {
        if(mIsAddedToLibraryObserverRegistered)
            getContext().getContentResolver().unregisterContentObserver(mAddedToLibraryStateObserver);
    }

    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mArtistArt1 = (ImageView)findViewById(0x7f090081);
        mArtistArt2 = (ImageView)findViewById(0x7f090082);
        mAlbumArt = (AsyncAlbumArtImageView)findViewById(0x7f090068);
        mViewFlipper = (ViewFlipper)findViewById(0x7f090080);
        mSharePlaylist = (BaseActionButton)findViewById(0x7f090087);
        mFollowPlaylist = (BaseActionButton)findViewById(0x7f090088);
        mAddToLibrary = (BaseActionButton)findViewById(0x7f090089);
        mPlayRadio = (BaseActionButton)findViewById(0x7f09006e);
        mShuffle = (BaseActionButton)findViewById(0x7f09008a);
        mBuyButton = findViewById(0x7f09007d);
        mBuyButton.setOnClickListener(this);
        mOverflow = findViewById(0x7f09008c);
        mOverflow.setOnClickListener(this);
        mPinButton = (KeepOnView)findViewById(0x7f09008b);
        mActionsContainer = (ViewGroup)findViewById(0x7f090085);
        BaseActionButton abaseactionbutton[] = new BaseActionButton[5];
        abaseactionbutton[0] = mShuffle;
        abaseactionbutton[1] = mPlayRadio;
        abaseactionbutton[2] = mAddToLibrary;
        abaseactionbutton[3] = mFollowPlaylist;
        abaseactionbutton[4] = mSharePlaylist;
        mActionButtons = abaseactionbutton;
        mTitle = (TextView)findViewById(0x7f090044);
        mSubtitle = (TextView)findViewById(0x7f09008e);
        mSongCount = (TextView)findViewById(0x7f090064);
        mOwnerPhoto = (AsyncAlbumArtImageView)findViewById(0x7f09008d);
        mAddToLibSpinner = (ProgressBar)findViewById(0x7f090091);
        mAddedToLibBadge = (ImageView)findViewById(0x7f09008f);
        if(UIStateManager.getInstance().getPrefs().isTabletMusicExperience())
            mAddedToLibBadge.setImageResource(0x7f0201bf);
    }

    public void onScroll(AbsListView abslistview, int i, int j, int k)
    {
        if(MusicPreferences.isHoneycombOrGreater())
        {
            View view = abslistview.getChildAt(0);
            if(view == this)
            {
                int l = -view.getTop() / 2;
                mViewFlipper.setTranslationY(l);
            }
        }
    }

    public void onScrollStateChanged(AbsListView abslistview, int i)
    {
    }

    public void onStart()
    {
        if(!mArtClearedOnStop) goto _L2; else goto _L1
        _L1:
        if(!shouldDoArtistSlideShow()) goto _L4; else goto _L3
        _L3:
        startArtistArtSlideShow();
        _L2:
        mArtClearedOnStop = false;
        return;
        _L4:
        if(mArtistArtUrls != null && mArtistArtUrls.size() > 0 && mArtistArt1.getDrawable() == null)
            mArtistArtHelper1.requestBitmap((String)mArtistArtUrls.get(0));
        else
        if(!TextUtils.isEmpty(mArtistArtUrl))
            mArtistArtHelper1.requestBitmap(mArtistArtUrl);
        if(true) goto _L2; else goto _L5
        _L5:
    }

    public void onStop()
    {
        if(mArtistArt1ShowingNonDefaultArt)
        {
            clearArtistArt(mArtistArt1);
            mArtistArt1ShowingNonDefaultArt = false;
        }
        clearArtistArt(mArtistArt2);
        if(mSlideShowInitialized)
        {
            mSlideShowHandler.removeCallbacks(mSlideRefreshRunnable);
            mSlideShowHandler.removeCallbacks(mSlideSwitchRunnable);
        }
        mArtClearedOnStop = true;
    }

    public void setContainerDocument(Document document)
    {
        mContainerDocument = document;
    }

    public void setCursor(Cursor cursor)
    {
        if(cursor != null)
        {
            if(mSongCount != null)
                if(shouldShowSongCount() && MusicUtils.hasCount(cursor))
                {
                    int i = cursor.getCount();
                    Resources resources = getResources();
                    Object aobj[] = new Object[1];
                    aobj[0] = Integer.valueOf(i);
                    String s = resources.getQuantityString(0x7f0e0000, i, aobj);
                    if(mSongList.getShouldFilter() && isOnDeviceOnlyMode())
                    {
                        Resources resources1 = getResources();
                        Object aobj1[] = new Object[2];
                        aobj1[0] = s;
                        aobj1[1] = getResources().getText(0x7f080139);
                        String s1 = resources1.getString(0x7f08013a, aobj1);
                        mSongCount.setText(s1);
                    } else
                    {
                        mSongCount.setText(s);
                    }
                } else
                {
                    mSongCount.setText(null);
                }
            if(shouldTryArtistSlideShow() && mArtistArtUrls == null && MusicUtils.hasCount(cursor))
            {
                extractArtistArtUrls(cursor, 10);
                if(shouldDoArtistSlideShow())
                {
                    setupArtistArtSlideShow(cursor);
                    return;
                }
                if(mArtistArtUrls.size() > 0)
                {
                    mArtistArtHelper1.requestBitmap((String)mArtistArtUrls.get(0));
                    return;
                }
            }
        }
    }

    public void setFragment(BaseListFragment baselistfragment)
    {
        mFragment = baselistfragment;
    }

    public void setMusicPreferences(MusicPreferences musicpreferences)
    {
        mMusicPreferences = musicpreferences;
    }

    public void setSongList(SongList songlist)
    {
        mSongList = songlist;
        if(!songlist.hasArtistArt())
            mViewFlipper.setVisibility(8);
        updateActionButtonsVisibility();
        mSharePlaylist.setMediaList(mSongList);
        mFollowPlaylist.setMediaList(mSongList);
        mAddToLibrary.setMediaList(mSongList);
        mPlayRadio.setMediaList(mSongList);
        mShuffle.setMediaList(mSongList);
        mAlbumArt.setOnClickListener(new OnClickListener() {

            public void onClick(View view)
            {
                MusicUtils.playMediaList(mSongList, -1);
            }

        });
        if(mPinButton != null)
            if(mSongList.supportsOfflineCaching() && !(mSongList instanceof NautilusSongList))
                enablePinning();
            else
                disablePinning();
        if(mSongList instanceof SharedSongList)
        {
            mOverflow.setVisibility(8);
            mBuyButton.setVisibility(0);
        }
        MusicUtils.runAsyncWithCallback(new AsyncRunner() {

            public void backgroundTask()
            {
                if(mSavedSongList == mSongList)
                {
                    mSavedPrimaryTitle = mSavedSongList.getName(context);
                    mSavedSecondaryTitle = mSavedSongList.getSecondaryName(context);
                    mSavedArtistId = mSavedSongList.getArtistId(context);
                    mSavedArtistArtUrl = MusicUtils.getArtistArtUrl(context, mSavedArtistId);
                    if(mSongList instanceof NautilusAlbumSongList)
                    {
                        String s = ((NautilusAlbumSongList)mSongList).getNautilusId();
                        Cursor cursor = null;
                        try
                        {
                            cursor = MusicUtils.query(context, com.google.android.music.store.MusicContent.Albums.getNautilusAlbumsUri(s), new String[] {
                                    "ArtistMetajamId"
                            }, null, null, null);
                            if(cursor != null && cursor.moveToFirst())
                                mSavedArtistMetajamId = cursor.getString(0);
                            mSavedArtistArtUrl = MusicUtils.getNautilusArtistArtUrl(context, mSavedArtistMetajamId);
                            return;
                        }
                        finally
                        {
                            Store.safeClose(cursor);
                        }
                    }
                }
            }

            public void taskCompleted()
            {
                if(mSavedSongList != mSongList || mFragment.getActivity() == null)
                    return;
                mPrimaryTitle = mSavedPrimaryTitle;
                mSecondaryTitle = mSavedSecondaryTitle;
                mArtistId = mSavedArtistId;
                mArtistMetajamId = mSavedArtistMetajamId;
                mArtistArtUrl = mSavedArtistArtUrl;
                if(mSongList instanceof PlaylistSongList)
                {
                    PlaylistSongList playlistsonglist = (PlaylistSongList)mSongList;
                    if(playlistsonglist.getPlaylistType() == 71)
                        showOwnerProfilePicture(playlistsonglist.getOwnerProfilePhotoUrl());
                } else
                if(mSongList instanceof SharedWithMeSongList)
                {
                    SharedWithMeSongList sharedwithmesonglist = (SharedWithMeSongList)mSongList;
                    showOwnerProfilePicture(sharedwithmesonglist.getOwnerProfilePhotoUrl());
                }
                if(isArtistArtShown() && !shouldTryArtistSlideShow())
                {
                    if(!TextUtils.isEmpty(mSavedArtistArtUrl))
                        mArtistArtHelper1.requestBitmap(mArtistArtUrl);
                    mArtistArt1.setBackgroundResource(0x7f020257);
                    mArtistArt1.setOnTouchListener(new OnTouchListener() {

                        public boolean onTouch(View view, MotionEvent motionevent)
                        {
                            motionevent.getAction();
                            JVM INSTR tableswitch 0 3: default 36
                            //                                       0 38
                            //                                       1 86
                            //                                       2 36
                            //                                       3 86;
                            goto _L1 _L2 _L3 _L1 _L3
                            _L1:
                            return false;
                            _L2:
                            Drawable drawable1 = mArtistArt1.getDrawable();
                            if(drawable1 != null)
                            {
                                drawable1.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_OVER);
                                mArtistArt1.invalidate();
                            }
                            continue; /* Loop/switch isn't completed */
                            _L3:
                            Drawable drawable = mArtistArt1.getDrawable();
                            if(drawable != null)
                            {
                                drawable.clearColorFilter();
                                mArtistArt1.invalidate();
                            }
                            if(true) goto _L1; else goto _L4
                            _L4:
                        }

                        final int color;


                        {
                            color = getContext().getResources().getColor(0x7f0a0034);
                        }
                    });
                    mArtistArt1.setOnClickListener(new OnClickListener() {

                        public void onClick(View view)
                        {
                            if(mArtistId > -1L)
                                ArtistPageActivity.showArtist(context, mArtistId, mSecondaryTitle, true);
                            else
                            if(!TextUtils.isEmpty(mArtistMetajamId))
                            {
                                ArtistPageActivity.showNautilusArtist(context, mArtistMetajamId, mSecondaryTitle);
                                return;
                            }
                        }

                    });
                }
                if(mSongList instanceof SharedWithMeSongList)
                    mAlbumArt.setSharedPlaylistArt(((SharedWithMeSongList)mSongList).getArtUrl());
                else
                    mAlbumArt.setArtForSonglist(mSongList);
                mTitle.setText(mPrimaryTitle);
                mSubtitle.setText(mSecondaryTitle);
                mFragment.getActionBarController().setActionBarTitle(mPrimaryTitle);
            }

            private String mSavedArtistArtUrl;
            private long mSavedArtistId;
            private String mSavedArtistMetajamId;
            private String mSavedPrimaryTitle;
            private String mSavedSecondaryTitle;
            private final SongList mSavedSongList;


            {
                mSavedSongList = mSongList;
                mSavedArtistId = -1L;
            }
        });
    }

    private static final boolean LOGV;
    BaseActionButton mActionButtons[];
    private ViewGroup mActionsContainer;
    private ProgressBar mAddToLibSpinner;
    private BaseActionButton mAddToLibrary;
    private ImageView mAddedToLibBadge;
    private final ContentObserver mAddedToLibraryStateObserver = new ContentObserver(new Handler()) {

        public void onChange(boolean flag)
        {
            if(MusicPreferences.isHoneycombOrGreater())
                mActionsContainer.setLayoutTransition(new LayoutTransition());
            MusicUtils.runAsyncWithCallback(mUpdateAddToLibraryButtonRunner);
        }

    };
    private AsyncAlbumArtImageView mAlbumArt;
    private boolean mArtClearedOnStop;
    private ImageView mArtistArt1;
    private boolean mArtistArt1ShowingNonDefaultArt;
    private ImageView mArtistArt2;
    private final ArtHelper mArtistArtHelper1 = new ArtHelper(0x7f090081);
    private final ArtHelper mArtistArtHelper2 = new ArtHelper(0x7f090082);
    private String mArtistArtUrl;
    private ArrayList mArtistArtUrls;
    private long mArtistId;
    private String mArtistMetajamId;
    private View mBuyButton;
    private Document mContainerDocument;
    private int mCurrentArtistArtUrlIndex;
    private BaseActionButton mFollowPlaylist;
    private BaseListFragment mFragment;
    private boolean mIsAddedToLibraryObserverRegistered;
    private MusicPreferences mMusicPreferences;
    private View mOverflow;
    private AsyncAlbumArtImageView mOwnerPhoto;
    private KeepOnView mPinButton;
    private BaseActionButton mPlayRadio;
    private String mPrimaryTitle;
    private String mSecondaryTitle;
    private BaseActionButton mSharePlaylist;
    private boolean mShowingFirstSlide;
    private BaseActionButton mShuffle;
    private Runnable mSlideRefreshRunnable;
    private Handler mSlideShowHandler;
    private boolean mSlideShowInitialized;
    private Runnable mSlideSwitchRunnable;
    private TextView mSongCount;
    private SongList mSongList;
    private TextView mSubtitle;
    private TextView mTitle;
    private final AsyncRunner mUpdateAddToLibraryButtonRunner = new AsyncRunner() {

        public void backgroundTask()
        {
            mAllInLibrary = ((NautilusSongList)mSongList).isAllInLibrary(getContext());
        }

        public void taskCompleted()
        {
            if(mFragment.getActivity() != null)
            {
                HeaderView containerheaderview = HeaderView.this;
                boolean flag;
                if(!mAllInLibrary)
                    flag = true;
                else
                    flag = false;
                containerheaderview.updateAddToLibraryButtonVisibility(flag);
                if(mAllInLibrary)
                {
                    enablePinning();
                    return;
                }
            }
        }

        private boolean mAllInLibrary;

    };
    private ViewFlipper mViewFlipper;

    static
    {
        LOGV = DebugUtils.isLoggable(com.google.android.music.utils.DebugUtils.MusicTag.UI);
    }





/*
    static String access$1002(ContainerHeaderView containerheaderview, String s)
    {
        containerheaderview.mArtistArtUrl = s;
        return s;
    }

*/













/*
    static Document access$2002(ContainerHeaderView containerheaderview, Document document)
    {
        containerheaderview.mContainerDocument = document;
        return document;
    }

*/






/*
    static boolean access$2402(ContainerHeaderView containerheaderview, boolean flag)
    {
        containerheaderview.mShowingFirstSlide = flag;
        return flag;
    }

*/











/*
    static boolean access$3302(ContainerHeaderView containerheaderview, boolean flag)
    {
        containerheaderview.mArtistArt1ShowingNonDefaultArt = flag;
        return flag;
    }

*/





/*
    static String access$602(ContainerHeaderView containerheaderview, String s)
    {
        containerheaderview.mPrimaryTitle = s;
        return s;
    }

*/



/*
    static String access$702(ContainerHeaderView containerheaderview, String s)
    {
        containerheaderview.mSecondaryTitle = s;
        return s;
    }

*/



/*
    static long access$802(ContainerHeaderView containerheaderview, long l)
    {
        containerheaderview.mArtistId = l;
        return l;
    }

*/



/*
    static String access$902(ContainerHeaderView containerheaderview, String s)
    {
        containerheaderview.mArtistMetajamId = s;
        return s;
    }

*/
}
