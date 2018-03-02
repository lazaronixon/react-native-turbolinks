package com.reactlibrary.activities;

import android.os.Bundle;

import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.ReactRootView;
import com.reactlibrary.R;
import com.reactlibrary.util.TurbolinksActivity;

import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_ROUTE;

public class NativeActivity extends TurbolinksActivity {

    private ReactRootView rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        toolBar = findViewById(R.id.toolbar);
        rootView = findViewById(R.id.react_root_view);
        route = getIntent().getParcelableExtra(INTENT_ROUTE);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);

        renderToolBar();
        handleTitlePress(route.getComponent(), null, null);

        rootView.startReactApplication(getReactInstanceManager(), route.getComponent(), route.getPassProps());
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() || route.getModal()) {
            backToHomeScreen(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void reloadSession() {
        TurbolinksSession.getDefault(this).resetToColdBoot();
    }

}
