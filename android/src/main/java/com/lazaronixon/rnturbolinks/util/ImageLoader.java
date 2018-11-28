package com.lazaronixon.rnturbolinks.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

import com.facebook.react.common.ReactConstants;
import com.facebook.react.views.imagehelper.ResourceDrawableIdHelper;
import com.lazaronixon.rnturbolinks.BuildConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageLoader {

    private static final String FILE_SCHEME = "file";

    public static Drawable loadImage(Context context, String source) {
        Drawable drawable;

        try {
            if (isLocalFile(Uri.parse(source))) {
                drawable = loadFile(context, source);
            } else {
                drawable = loadResource(context, source);
                if (drawable == null || BuildConfig.DEBUG) {
                    drawable = readJsDevImage(context, source);
                }
            }
            return drawable;
        } catch (IOException e) {
            Log.e(ReactConstants.TAG, "Error loading image." + e.toString());
            return null;
        }
    }

    private static boolean isLocalFile(Uri uri) {
        return FILE_SCHEME.equals(uri.getScheme());
    }

    private static Drawable loadFile(Context context, String uri) {
        Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(uri).getPath());
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private static Drawable loadResource(Context context, String iconSource) {
        return ResourceDrawableIdHelper.getInstance().getResourceDrawable(context, iconSource);
    }

    private static Drawable readJsDevImage(Context context, String source) throws IOException {
        StrictMode.ThreadPolicy threadPolicy = adjustThreadPolicyDebug();
        InputStream is = openStream(context, source);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        restoreThreadPolicyDebug(threadPolicy);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private static InputStream openStream(Context context, String uri) throws IOException {
        return uri.contains("http") ? remoteUrl(uri) : localFile(context, uri);
    }

    private static InputStream remoteUrl(String uri) throws IOException {
        return new URL(uri).openStream();
    }

    private static InputStream localFile(Context context, String uri) throws FileNotFoundException {
        return context.getContentResolver().openInputStream(Uri.parse(uri));
    }

    private static StrictMode.ThreadPolicy adjustThreadPolicyDebug() {
        StrictMode.ThreadPolicy threadPolicy = null;
        if (BuildConfig.DEBUG) {
            threadPolicy = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        }
        return threadPolicy;
    }

    private static void restoreThreadPolicyDebug(StrictMode.ThreadPolicy threadPolicy) {
        if (BuildConfig.DEBUG && threadPolicy != null) {
            StrictMode.setThreadPolicy(threadPolicy);
        }
    }

}

