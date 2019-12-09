package com.lazaronixon.rnturbolinks.util;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;

public class TurbolinksViewFrame extends FrameLayout {

    private TurbolinksView turbolinksView;
    private ReactRootView customView;

    public TurbolinksViewFrame(@NonNull Context context) {
        super(context);
        resolveAttribute(context, null, 0, 0);
    }

    public TurbolinksViewFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        resolveAttribute(context, attrs, 0, 0);
    }

    public TurbolinksViewFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttribute(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TurbolinksViewFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        resolveAttribute(context, attrs, defStyleAttr, defStyleRes);
    }
    
    private void resolveAttribute(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        turbolinksView = new TurbolinksView(context, attrs, defStyleAttr, defStyleRes);
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
