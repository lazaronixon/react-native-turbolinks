package com.reactlibrary.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;

import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_ROUTE;

public class NativeActivity extends ReactAppCompatActivity implements GenericActivity {

    private Toolbar toolBar;
    private ReactRootView rootView;
    private HelperActivity helperAct;
    private TurbolinksRoute route;
    private Boolean navigationBarHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        toolBar = findViewById(R.id.toolbar);
        rootView = findViewById(R.id.react_root_view);

        helperAct = new HelperActivity(this);
        route = getIntent().getParcelableExtra(INTENT_ROUTE);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);

        helperAct.renderToolBar(toolBar);
        helperAct.renderTitle();

        rootView.startReactApplication(getReactInstanceManager(), route.getComponent(), route.getPassProps());
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() || route.getModal()) {
            helperAct.backToHomeScreen(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return helperAct.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return helperAct.onOptionsItemSelected(item);
    }

    @Override
    public void renderTitle() {
        helperAct.renderTitle();
    }

    @Override
    public void handleTitlePress(Toolbar toolbar) {
        helperAct.handleTitlePress(toolbar, route.getComponent(), null, null);
    }

    @Override
    public RCTDeviceEventEmitter getEventEmitter() { return helperAct.getEventEmitter(); }

    @Override
    public ReactInstanceManager getManager() { return getReactInstanceManager(); }

    @Override
    public TurbolinksRoute getRoute() {
        return route;
    }

    @Override
    public void renderComponent(TurbolinksRoute route, int tabIndex) {
    }

    @Override
    public void reload() {
    }

    @Override
    public Boolean getNavigationBarHidden() {
        return navigationBarHidden;
    }

}
