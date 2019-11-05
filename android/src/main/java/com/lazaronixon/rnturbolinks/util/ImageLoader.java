package com.lazaronixon.rnturbolinks.util;


import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout.LayoutParams;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.views.imagehelper.ImageSource;

public class ImageLoader {

    public static void bitmapFor(Bundle image, Context context, BaseBitmapDataSubscriber baseBitmapDataSubscriber) {
        ImageSource source = new ImageSource(context, image.getString("uri"));
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(source.getUri()).build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);

        dataSource.subscribe(baseBitmapDataSubscriber, UiThreadImmediateExecutorService.getInstance());
    }

    public static SimpleDraweeView imageViewFor(Bundle image, Context context) {
        SimpleDraweeView result = new SimpleDraweeView(context);
        result.setImageURI(image.getString("uri"));
        result.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return result;
    }

}

