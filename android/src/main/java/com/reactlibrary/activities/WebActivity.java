package com.reactlibrary.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;

import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;

import static com.reactlibrary.RNTurbolinksModule.INTENT_INITIAL_VISIT;
import static com.reactlibrary.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_ROUTE;
import static com.reactlibrary.RNTurbolinksModule.INTENT_USER_AGENT;

public class WebActivity extends ReactAppCompatActivity implements GenericWebActivity {

    private HelperWebActivity helperAct;
    private TurbolinksRoute route;
    private String messageHandler;
    private String userAgent;
    private Boolean initialVisit;
    private Boolean navigationBarHidden;
    private TurbolinksView turbolinksView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        helperAct = new HelperWebActivity(this);
        route = getIntent().getParcelableExtra(INTENT_ROUTE);
        initialVisit = getIntent().getBooleanExtra(INTENT_INITIAL_VISIT, true);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);
        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);

        helperAct.renderToolBar((Toolbar) findViewById(R.id.toolbar));
        turbolinksView = findViewById(R.id.turbolinks_view);
        helperAct.visitTurbolinksView(turbolinksView, route.getUrl());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        helperAct.onRestart();
    }

    @Override
    public void onReceivedError(int errorCode) {
        helperAct.onReceivedError(errorCode);
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        helperAct.requestFailedWithStatusCode(statusCode);
    }

    @Override
    public void visitProposedToLocationWithAction(String location, String action) {
        helperAct.visitProposedToLocationWithAction(location, action);
    }

    @Override
    public void visitCompleted() {
        helperAct.visitCompleted();
    }

    @Override
    public void onPageFinished() {
    }

    @Override
    public void pageInvalidated() {
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
    public boolean onOptionsItemSelected(MenuItem item) { return helperAct.onOptionsItemSelected(item); }

    @Override
    public void renderTitle() {
        helperAct.renderTitle();
    }

    @Override
    public RCTDeviceEventEmitter getEventEmitter() {
        return getReactInstanceManager().getCurrentReactContext().getJSModule(RCTDeviceEventEmitter.class);
    }

    @Override
    public void reloadSession() {
        helperAct.reloadSession();
    }

    @Override
    public void handleTitlePress(Toolbar toolbar) {
        helperAct.handleTitlePress(toolbar);
    }

    private void handleVisitCompleted() {
        helperAct.handleVisitCompleted();
    }

    @Override
    public TurbolinksRoute getRoute() {
        return route;
    }

    @Override
    public void onSuperBackPressed() {
        super.onBackPressed();
    }

    @Override
    public Boolean getInitialVisit() {
        return initialVisit;
    }

    @Override
    public Boolean getNavigationBarHidden() {
        return navigationBarHidden;
    }

    @Override
    public TurbolinksView getTurbolinksView() {
        return turbolinksView;
    }

    @Override
    public String getMessageHandler() {
        return messageHandler;
    }

    @Override
    public String getUserAgent() { return userAgent; }

    @Override
    public void renderComponent(TurbolinksRoute route) { helperAct.renderComponent(route, getReactInstanceManager()); }

    @Override
    public void reload() { helperAct.reload(); }

    @JavascriptInterface
    public void postMessage(String message) {
        helperAct.postMessage(message);
    }
}
