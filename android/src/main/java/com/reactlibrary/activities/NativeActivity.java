package com.reactlibrary.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;

import static com.reactlibrary.RNTurbolinksModule.INTENT_INITIAL_VISIT;
import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_ROUTE;

public class NativeActivity extends ReactAppCompatActivity implements GenericNativeActivity {

    private HelperNativeActivity helperAct;
    private TurbolinksRoute route;
    private Boolean navigationBarHidden;
    private Boolean initialVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        helperAct = new HelperNativeActivity(this);
        route = getIntent().getParcelableExtra(INTENT_ROUTE);
        initialVisit = getIntent().getBooleanExtra(INTENT_INITIAL_VISIT, true);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);

        helperAct.renderToolBar((Toolbar) findViewById(R.id.toolbar));
        helperAct.renderTitle();

        ReactRootView rootView = findViewById(R.id.react_root_view);
        helperAct.visitComponent(rootView, getReactInstanceManager(), route);
    }

    @Override
    public void onBackPressed() {
        helperAct.onBackPressed();
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
        helperAct.handleTitlePress(toolbar);
    }

    @Override
    public void onSuperBackPressed() {
        super.onBackPressed();
    }

    @Override
    public RCTDeviceEventEmitter getEventEmitter() {
        return getReactInstanceManager().getCurrentReactContext().getJSModule(RCTDeviceEventEmitter.class);
    }

    @Override
    public TurbolinksRoute getRoute() {
        return route;
    }

    @Override
    public Boolean getInitialVisit() {
        return initialVisit;
    }

    @Override
    public Boolean getNavigationBarHidden() {
        return navigationBarHidden;
    }

}
