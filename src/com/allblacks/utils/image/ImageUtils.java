package com.allblacks.utils.image;

import android.content.Context;

public class ImageUtils {
 
    private static ImageWorker imageWorker = null;
    
    public static synchronized void initImageWorker(Context context) {
        if (imageWorker == null) {
            imageWorker = new ImageWorker(context);
        }
    }
    
    public static ImageWorker getImageWorker() {
        return imageWorker;
    }
}
