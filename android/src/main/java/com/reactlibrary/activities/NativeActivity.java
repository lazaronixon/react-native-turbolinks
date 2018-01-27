package com.reactlibrary.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;

public class NativeActivity extends ReactAppCompatActivity {

    private static final String INTENT_INITIAL_VISIT = "intentInitialVisit";

    private TurbolinksRoute route;
    private Boolean initialVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        route = new TurbolinksRoute(getIntent());
        initialVisit = getIntent().getBooleanExtra(INTENT_INITIAL_VISIT, true);

        setContentView(R.layout.activity_native);
        renderToolBar();
        renderReactRootView();
    }

    @Override
    public void onBackPressed() {
        if (initialVisit) {
            moveTaskToBack(true);
        } else {
            if (!route.getModal()) super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void renderToolBar() {
        Toolbar turbolinksToolbar = (Toolbar) findViewById(R.id.native_toolbar);
        setSupportActionBar(turbolinksToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!initialVisit);
        getSupportActionBar().setDisplayShowHomeEnabled(!initialVisit);
        getSupportActionBar().setTitle(route.getTitle());
        getSupportActionBar().setSubtitle(route.getSubtitle());
    }

    private void renderReactRootView() {
        ReactRootView mReactRootView = (ReactRootView) findViewById(R.id.native_view);
        ReactInstanceManager mReactInstanceManager = getReactInstanceManager();
        mReactRootView.startReactApplication(mReactInstanceManager, route.getComponent(), route.getPassProps());
    }

}
