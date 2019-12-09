package com.lazaronixon.rnturbolinks.util;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.RequiresApi;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;

public class TurbolinksViewFrame extends FrameLayout {

    private TurbolinksView turbolinksView;
    private ReactRootView customView;

    public TurbolinksViewFrame(Context context) {
        super(context);
        resolveAttribute(context);
    }

    public TurbolinksViewFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        resolveAttribute(context);
    }

    public TurbolinksViewFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttribute(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TurbolinksViewFrame(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        resolveAttribute(context);
    }

    private void resolveAttribute(Context context) {
        turbolinksView = new TurbolinksView(context);
        turbolinksView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(turbolinksView);
    }

    public TurbolinksView getTurbolinksView() {
        return turbolinksView;
    }

    public void renderComponent(ReactInstanceManager manager, TurbolinksRoute route) {
        removeView(customView);
        turbolinksView.setVisibility(View.GONE);
        customView = new ReactRootView(getContext());
        customView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        customView.startReactApplication(manager, route.getComponent(), route.getPassProps());
        addView(customView);
    }

    public void reload(TurbolinksSession session, String location) {
        turbolinksView.setVisibility(View.VISIBLE);
        removeView(customView);
        session.visit(location);
    }

}
