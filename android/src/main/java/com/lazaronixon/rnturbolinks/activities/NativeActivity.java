package com.lazaronixon.rnturbolinks.activities;

import android.os.Bundle;

import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.ReactRootView;
import com.lazaronixon.rnturbolinks.R;

import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_NAV_BAR_STYLE;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_ROUTE;

public class NativeActivity extends ApplicationActivity {

    private ReactRootView rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        toolBar = findViewById(R.id.toolbar);
        route = getIntent().getParcelableExtra(INTENT_ROUTE);
        navBarStyle = getIntent().getParcelableExtra(INTENT_NAV_BAR_STYLE);

        rootView = findViewById(R.id.react_root_view);
        rootView.startReactApplication(getReactInstanceManager(), route.getComponent(), route.getPassProps());

        handleTitlePress(route.getComponent(), null, null);

        renderToolBar();
        setupTransitionOnEnter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rootView.unmountReactApplication();
        rootView = null;
    }

    @Override
    public void reloadSession() {
        TurbolinksSession.getDefault(this).resetToColdBoot();
    }

}
