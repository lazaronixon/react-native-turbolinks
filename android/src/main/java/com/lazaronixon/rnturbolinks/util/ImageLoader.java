package com.lazaronixon.rnturbolinks.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.react.views.imagehelper.ResourceDrawableIdHelper;

public class ImageLoader {
    private static final String FILE_SCHEME = "file";

    public static Drawable loadImage(Context context, String iconSource) {
        Uri uri = Uri.parse(iconSource);
        if (isLocalFile(uri)) {
            return loadFile(context, uri);
        } else {
            return loadResource(context, iconSource);
        }
    }

    private static boolean isLocalFile(Uri uri) {
        return FILE_SCHEME.equals(uri.getScheme());
    }

    private static Drawable loadFile(Context context, Uri uri) {
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private static Drawable loadResource(Context context, String iconSource) {
        return ResourceDrawableIdHelper.getInstance().getResourceDrawable(context, iconSource);
    }
}

