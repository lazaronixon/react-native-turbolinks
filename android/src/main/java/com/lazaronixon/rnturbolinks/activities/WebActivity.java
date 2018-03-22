package com.lazaronixon.rnturbolinks.activities;

import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.lazaronixon.rnturbolinks.util.TurbolinksRoute;
import com.lazaronixon.rnturbolinks.util.TurbolinksViewFrame;
import com.lazaronixon.rnturbolinks.R;

import java.net.MalformedURLException;
import java.net.URL;

import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_ROUTE;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_USER_AGENT;

public class WebActivity extends GenericActivity implements TurbolinksAdapter {

    public static final int HTTP_FAILURE = 0;
    public static final int NETWORK_FAILURE = 1;
    public static final String TURBOLINKS_ERROR = "turbolinksError";
    public static final String TURBOLINKS_MESSAGE = "turbolinksMessage";
    public static final String TURBOLINKS_VISIT_COMPLETED = "turbolinksVisitCompleted";
    public static final String TURBOLINKS_VISIT = "turbolinksVisit";

    private TurbolinksViewFrame turbolinksViewFrame;
    private String messageHandler;
    private String userAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        toolBar = findViewById(R.id.toolbar);
        route = getIntent().getParcelableExtra(INTENT_ROUTE);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);

        turbolinksViewFrame = findViewById(R.id.turbolinks_view);

        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);

        renderToolBar();
        visitTurbolinksView();
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
        renderTitle(route.getTitle(), route.getSubtitle());
        handleTitlePress();
        handleVisitCompleted();
    }

    @Override
    public void onPageFinished() {
    }

    @Override
    public void pageInvalidated() {
    }

    @Override
    public void renderTitle(String title, String subtitle) {
        WebView webView = TurbolinksSession.getDefault(this).getWebView();
        String mTitle = title != null ? title : webView.getTitle();
        getSupportActionBar().setTitle(mTitle);
        getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void reloadVisitable() {
        turbolinksViewFrame.reload(TurbolinksSession.getDefault(this), route.getUrl());
    }

    @Override
    public void reloadSession() {
        TurbolinksSession.getDefault(this).resetToColdBoot();
        TurbolinksSession.getDefault(this).visit(route.getUrl());
    }

    @Override
    public void renderComponent(TurbolinksRoute route, int tabIndex) {
        turbolinksViewFrame.renderComponent(getReactInstanceManager(), route);
    }

    @Override
    public void evaluateJavaScript(String script, final Promise promise) {
        WebView webView = TurbolinksSession.getDefault(this).getWebView();
        webView.evaluateJavascript(script, new ValueCallback<String>() {
            public void onReceiveValue(String source) {
                promise.resolve(source);
            }
        });
    }

    @JavascriptInterface
    public void postMessage(String message) {
        getEventEmitter().emit(TURBOLINKS_MESSAGE, message);
    }

    private void handleVisitCompleted() {
        WebView webView = TurbolinksSession.getDefault(this).getWebView();
        try {
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(webView.getUrl());
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            params.putInt("tabIndex", 0);
            getEventEmitter().emit(TURBOLINKS_VISIT_COMPLETED, params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void handleTitlePress() {
        try {
            WebView webView = TurbolinksSession.getDefault(this).getWebView();
            URL urlLocation = new URL(webView.getUrl());
            handleTitlePress(null, urlLocation.toString(), urlLocation.getPath());
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void visitTurbolinksView() {
        TurbolinksSession session = TurbolinksSession.getDefault(this);
        WebSettings settings = session.getWebView().getSettings();
        String ua = settings.getUserAgentString();
        if (messageHandler != null) session.addJavascriptInterface(this, messageHandler);
        if (userAgent != null && !ua.endsWith(userAgent)) { settings.setUserAgentString(ua.concat(" " + userAgent)); }
        session.activity(this).adapter(this).view(turbolinksViewFrame.getTurbolinksView()).visit(route.getUrl());
    }

}
