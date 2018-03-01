package com.reactlibrary.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;
import com.reactlibrary.util.TurbolinksViewFrame;

import java.net.MalformedURLException;
import java.net.URL;

import static com.reactlibrary.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_ROUTE;
import static com.reactlibrary.RNTurbolinksModule.INTENT_USER_AGENT;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

public class WebActivity extends ReactAppCompatActivity implements GenericActivity, TurbolinksAdapter {

    public static final int HTTP_FAILURE = 0;
    public static final int NETWORK_FAILURE = 1;
    public static final String TURBOLINKS_ERROR = "turbolinksError";
    public static final String TURBOLINKS_MESSAGE = "turbolinksMessage";
    public static final String TURBOLINKS_VISIT_COMPLETED = "turbolinksVisitCompleted";
    public static final String TURBOLINKS_VISIT = "turbolinksVisit";

    private TurbolinksViewFrame turbolinksViewFrame;
    private Toolbar toolbar;
    private HelperActivity helperAct;
    private TurbolinksRoute route;
    private String messageHandler;
    private String userAgent;
    private boolean navigationBarHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        toolbar = findViewById(R.id.toolbar);
        turbolinksViewFrame = findViewById(R.id.turbolinks_view);

        helperAct = new HelperActivity(this);
        route = getIntent().getParcelableExtra(INTENT_ROUTE);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);
        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);

        helperAct.renderToolBar(toolbar);
        visitTurbolinksView();
    }

    private void visitTurbolinksView() {
        TurbolinksSession session = TurbolinksSession.getDefault(this);
        WebSettings settings = session.getWebView().getSettings();
        if (messageHandler != null) session.addJavascriptInterface(this, messageHandler);
        if (userAgent != null) settings.setUserAgentString(userAgent);
        session.activity(this).adapter(this).view(turbolinksViewFrame.getTurbolinksView()).visit(route.getUrl());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        TurbolinksSession.getDefault(this)
                .activity(this)
                .adapter(this)
                .restoreWithCachedSnapshot(true)
                .view(turbolinksViewFrame.getTurbolinksView())
                .visit(route.getUrl());
    }

    @Override
    public void onReceivedError(int errorCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", errorCode < 0 ? NETWORK_FAILURE : HTTP_FAILURE);
        params.putInt("statusCode", errorCode);
        params.putInt("tabIndex", 0);
        params.putString("description", "Network Failure.");
        getEventEmitter().emit(TURBOLINKS_ERROR, params);
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", HTTP_FAILURE);
        params.putInt("statusCode", statusCode);
        params.putInt("tabIndex", 0);
        params.putString("description", "HTTP Failure. Code:" + statusCode);
        getEventEmitter().emit(TURBOLINKS_ERROR, params);
    }

    @Override
    public void visitProposedToLocationWithAction(String location, String action) {
        try {
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(location);
            params.putString("component", null);
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            params.putString("action", action);
            getEventEmitter().emit(TURBOLINKS_VISIT, params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    @Override
    public void visitCompleted() {
        renderTitle();
        handleVisitCompleted();
    }

    @Override
    public void onPageFinished() {
    }

    @Override
    public void pageInvalidated() {
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
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
        WebView webView = TurbolinksSession.getDefault(this).getWebView();
        String title = route.getTitle() != null ? route.getTitle() : webView.getTitle();
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(route.getSubtitle());
    }

    @Override
    public RCTDeviceEventEmitter getEventEmitter() {
        return helperAct.getEventEmitter();
    }

    @Override
    public ReactInstanceManager getManager() {
        return getReactInstanceManager();
    }

    @Override
    public void handleTitlePress(Toolbar toolbar) {
        try {
            WebView webView = TurbolinksSession.getDefault(this).getWebView();
            URL urlLocation = new URL(webView.getUrl());
            helperAct.handleTitlePress(toolbar, null, urlLocation.toString(), urlLocation.getPath());
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    @Override
    public void reloadSession() {
        TurbolinksSession.getDefault(this).resetToColdBoot();
        TurbolinksSession.getDefault(this).visit(route.getUrl());
    }

    private void handleVisitCompleted() {
        String javaScript = "document.documentElement.outerHTML";
        final WebView webView = TurbolinksSession.getDefault(this).getWebView();
        webView.evaluateJavascript(javaScript, new ValueCallback<String>() {
            public void onReceiveValue(String source) {
                try {
                    WritableMap params = Arguments.createMap();
                    URL urlLocation = new URL(webView.getUrl());
                    params.putString("url", urlLocation.toString());
                    params.putString("path", urlLocation.getPath());
                    params.putString("source", unescapeJava(source));
                    params.putInt("tabIndex", 0);
                    getEventEmitter().emit(TURBOLINKS_VISIT_COMPLETED, params);
                } catch (MalformedURLException e) {
                    Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
                }
            }
        });
    }

    @Override
    public TurbolinksRoute getRoute() {
        return route;
    }

    @Override
    public void renderComponent(TurbolinksRoute route, int tabIndex) {
        turbolinksViewFrame.renderComponent(getReactInstanceManager(), route);
    }

    @Override
    public void reload() {
        turbolinksViewFrame.reload(TurbolinksSession.getDefault(this), route.getUrl());
    }

    @Override
    public boolean getNavigationBarHidden() {
        return navigationBarHidden;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        getEventEmitter().emit(TURBOLINKS_MESSAGE, message);
    }
}
