package com.reactlibrary.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;

public class TurbolinksViewFrame extends FrameLayout {

    private TurbolinksView turbolinksView;
    private ReactRootView customView;

    public TurbolinksViewFrame(Context context) {
        super(context);
        init();
    }

    public TurbolinksViewFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TurbolinksViewFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TurbolinksViewFrame(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        turbolinksView = new TurbolinksView(getContext());
        turbolinksView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(turbolinksView);
    }

    public TurbolinksView getTurbolinksView() {
        return turbolinksView;
    }

    public void renderComponent(ReactInstanceManager manager, TurbolinksRoute route) {
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
