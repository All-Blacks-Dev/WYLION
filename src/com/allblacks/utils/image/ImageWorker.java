package com.allblacks.utils.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.allblacks.utils.file.FileUtil;
import com.allblacks.utils.web.ApacheCredentialProvider;
import com.allblacks.utils.web.HttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

public class ImageWorker {

    private static final String TAG = ImageWorker.class.getCanonicalName();
    private BitmapReader mBitmapReader = null;
    private Options bgOptions = null;
    private Resources mResources = null;

    protected ImageWorker(Context context) {
        mResources = context.getResources();
        bgOptions = new Options();
        bgOptions.inPurgeable = true;
        bgOptions.inPreferredConfig = Config.RGB_565;
        mBitmapReader = new BitmapReader(0.25f);
    }

    /**
     * Returns true if the current work has been canceled or if there was no
     * work in progress on this image view. Returns false if the work in
     * progress deals with the same data. The work is not stopped in that case.
     */
    public static boolean cancelPotentialWork(Object key, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final Object bitmapData = bitmapWorkerTask.key;
            if (!bitmapData.equals(key)) {
                bitmapWorkerTask.cancel(true);
                Log.d(TAG, "cancelPotentialWork - cancelled work for " + key);
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active work task (if any) associated with
     *         this imageView. null if there is no such task.
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * Load an image specified by the data parameter into an ImageView.
     * A memory and disk cache will be used if neither are found, the image will be downloaded onto the device.
     *
     * @param data      The URL of the image to download.
     * @param imageView The ImageView to bind the downloaded image to.
     */
    public void loadImage(ImageView imageView, BitmapWorkerTask worker, String key, Object... data) {
        if (data == null) {
            return;
        }

        Bitmap bitmap = null;
        bitmap = mBitmapReader.getBitmapFromMemCache(key);
        if (bitmap != null) {
            // Bitmap found in memory cache
            imageView.setImageBitmap(bitmap);
        } else if (cancelPotentialWork(key, imageView)) {
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, worker);
            imageView.setImageDrawable(asyncDrawable);
            worker.execute(data);
        }
    }

    public static enum ImageType {
        SHOW_BANNER, SHOW_POSTER, SHOW_SEASON_POSTER, MOVIE_POSTER, MOVIE_BANNER
    }

    public class AsyncDrawable extends BitmapDrawable {

        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmapWorkerTask.getLoadingBitmap());
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public abstract class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {

        private static final int FADE_IN_TIME = 127;
        public String key = null;
        private WeakReference<ImageView> weakViewReference = null;
        private WeakReference<Bitmap> mLoadingBitmap = null;
        private boolean mFade = false;

        protected BitmapWorkerTask(WeakReference<ImageView> weakViewReference, WeakReference<Bitmap> bitmap, String key) {
            this.weakViewReference = weakViewReference;
            this.mLoadingBitmap = bitmap;
            this.key = key;
        }

        protected Bitmap getLoadingBitmap() {
            return mLoadingBitmap.get();
        }

        /**
         * This background tasks loads the needed bitmap. If the cache is
         * enabled, it loads it from the external storage, if not or if it does
         * not exists it will try to download it.
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            Options bgOptions = new Options();
            Bitmap bitmap = null;
            bgOptions.inSampleSize = 1;

            String folderPath = File.separator + params[1]
                    + File.separator;
            folderPath = folderPath.replace(":", "");

            String fileName = getFilename(params);

            /**
             * We try to read the file from the device
             */
            if (Debug.isDebuggerConnected()) {
                Log.i(getClass().getCanonicalName(), "Loading Bitmap for : " + params[1] + " ... trying to open file.");
            }
            FileUtil.createDirectory(folderPath);
            bitmap = mBitmapReader.getBitmapFromFile(folderPath, fileName, key);


            /**
             * The bitmap object is null if the BitmapFactory has been unable to
             * decode the Bitmap or if the File does not exists.
             */
            if (!isCancelled() && bitmap == null) {
                /**
                 * We get the banner from the server
                 */
                if (Debug.isDebuggerConnected()) {
                    Log.i(getClass().getCanonicalName(), "Bitmap for : " + params[1] + " not found ... trying to download file.");
                }
                String url = getImageURL(params);
                String savePath = folderPath + fileName;
                bitmap = mBitmapReader.getBitmapFromWeb(url, savePath, key);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (isCancelled()) {
                result = null;
            }
            if (weakViewReference != null && result != null) {
                final ImageView imageView = getAttachedImageView();
                if (imageView != null) {
                    setImage(imageView, result);
                }
            }
            weakViewReference.clear();
        }

        @Override
        protected void onCancelled(Bitmap result) {
            result = null;
            weakViewReference.clear();
            super.onCancelled(result);
        }

        private void setImage(ImageView imageView, Bitmap bitmap) {
            if (mFade) {
                // Create a transition and set the resulting image to be
                // displayed
                TransitionDrawable td = new TransitionDrawable(new Drawable[]{new BitmapDrawable(mResources, mLoadingBitmap.get()),
                        new BitmapDrawable(mResources, bitmap)});
                imageView.setImageDrawable(td);
                td.startTransition(FADE_IN_TIME);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }

        /**
         * Returns the ImageView associated with this task as long as the
         * ImageView's task still points to this task as well. Returns null
         * otherwise.
         */
        private ImageView getAttachedImageView() {
            final ImageView imageView = weakViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }

        protected abstract String getImageURL(Object... params);

        protected abstract String getFilename(Object... params);

        protected abstract ImageType getImageType();
    }

    public class BitmapReader {

        private LruCache<String, Bitmap> mMemoryCache;

        public BitmapReader(float percent) {
            if (percent < 0.05f || percent > 0.8f) {
                throw new IllegalArgumentException("setMemCacheSizePercent - percent must be " + "between 0.05 and 0.8 (inclusive)");
            }
            int memCacheSize = Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
            mMemoryCache = new LruCache<String, Bitmap>(memCacheSize) {

                /**
                 * Measure item size in kilobytes rather than units which is
                 * more practical for a bitmap cache
                 */
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    final int bitmapSize = getBitmapSize(bitmap) / 1024;
                    return bitmapSize == 0 ? 1 : bitmapSize;
                }
            };
        }

        /**
         * Get the size in bytes of a bitmap.
         *
         * @param bitmap
         * @return size in bytes
         */
        @TargetApi(12)
        public int getBitmapSize(Bitmap bitmap) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                return bitmap.getByteCount();
            }
            return bitmap.getRowBytes() * bitmap.getHeight();
        }

        /**
         * Get from memory cache.
         *
         * @param data Unique identifier for which item to get
         * @return The bitmap if found in cache, null otherwise
         */
        public Bitmap getBitmapFromMemCache(String data) {
            return mMemoryCache.get(data);
        }

        /**
         * This method reads the targeted Bitmap from the local storage.
         *
         * @param folderPath The path in where to open the file
         * @param fileName   The name of the file to open in the given folderPath
         * @param key        The key representing the file in the memory cache.
         * @return The bitmap that is stored in the memory cache.
         */
        public Bitmap getBitmapFromFile(String folderPath, String fileName, String key) {

            Bitmap bitmap = null;
            byte[] data;

            /**
             * Trying to find Image on Local System
             */
            try {
                data = FileUtil.getFileAsByteArray(folderPath + File.separator + fileName);
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bgOptions);
                if (bitmap != null) {
                    mMemoryCache.put(key, bitmap);
                }
            } catch (Throwable e) {
                Log.e(TAG, " " + e.getLocalizedMessage());
            }

            return mMemoryCache.get(key);
        }

        /**
         * This method downloads the targeted Bitmap from the web and saves it if needed.
         *
         * @param url      The url from the Bitmap
         * @param savePath The full path in where to save the file
         * @param key      The key representing the file in the memory cache.
         * @return The bitmap that is stored in the memory cache.
         */
        public Bitmap getBitmapFromWeb(String url, String savePath, String key) {

            Bitmap bitmap = null;
            byte[] data;
            try {
                data = HttpUtil.getInstance().getDataAsByteArray(url, ApacheCredentialProvider.getCredentialsProvider());
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bgOptions);

                /**
                 * ... save it on the device
                 */
                FileOutputStream fileOutputStream = new FileOutputStream(savePath);
                fileOutputStream.write(data);
                fileOutputStream.flush();
                fileOutputStream.close();

                if (bitmap != null) {
                    mMemoryCache.put(key, bitmap);
                }
            } catch (Throwable e) {
                Log.e(TAG, " " + e.getLocalizedMessage());
            }
            return mMemoryCache.get(key);
        }
    }
}
