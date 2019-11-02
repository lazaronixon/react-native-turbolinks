package com.lazaronixon.rnturbolinks.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.lazaronixon.rnturbolinks.R;
import com.lazaronixon.rnturbolinks.util.TurbolinksRoute;
import com.lazaronixon.rnturbolinks.util.TurbolinksViewFrame;

import java.net.MalformedURLException;
import java.net.URL;

import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_INJECTED_JAVASCRIPT;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_LOADING_VIEW;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_NAV_BAR_STYLE;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_ROUTE;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_USER_AGENT;

public class WebActivity extends ApplicationActivity implements TurbolinksAdapter {

    public static final int HTTP_FAILURE = 0;
    public static final int NETWORK_FAILURE = 1;
    public static final String TURBOLINKS_ERROR = "turbolinksError";
    public static final String TURBOLINKS_MESSAGE = "turbolinksMessage";
    public static final String TURBOLINKS_VISIT_COMPLETED = "turbolinksVisitCompleted";
    public static final String TURBOLINKS_VISIT = "turbolinksVisit";

    private TurbolinksViewFrame turbolinksViewFrame;
    private String messageHandler;
    private String userAgent;
    private String loadingView;
    private String injectedJavaScript;
    private ReactRootView progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        toolBar = findViewById(R.id.toolbar);
        route = getIntent().getParcelableExtra(INTENT_ROUTE);
        navBarStyle = getIntent().getParcelableExtra(INTENT_NAV_BAR_STYLE);

        turbolinksViewFrame = findViewById(R.id.turbolinks_view);

        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);
        loadingView = getIntent().getStringExtra(INTENT_LOADING_VIEW);
        injectedJavaScript = getIntent().getStringExtra(INTENT_INJECTED_JAVASCRIPT);

        renderToolBar();
        setupAppOptions();
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
    protected void onDestroy() {
        super.onDestroy();
        if (progressIndicator != null) {
            progressIndicator.unmountReactApplication();
            progressIndicator = null;
        }
    }

    @Override
    public void onReceivedError(int errorCode) {
        if (errorCode < 0) {
            WritableMap params = Arguments.createMap();
            params.putInt("code", NETWORK_FAILURE);
            params.putInt("statusCode", errorCode);
            params.putString("description", "Network Failure.");
            getEventEmitter().emit(TURBOLINKS_ERROR, params);
        }
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", HTTP_FAILURE);
        params.putInt("statusCode", statusCode);
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
        handleWebTitlePress();
        handleVisitCompleted();
    }

    @Override
    public void onPageFinished() {
        if (injectedJavaScript != null) {
            WebView webView = TurbolinksSession.getDefault(this).getWebView();
            webView.evaluateJavascript(injectedJavaScript, null);
        }
    }

    @Override
    public void pageInvalidated() {}

    @Override
    public void renderTitle(String title, String subtitle) {
        if (route.getTitleImage() == null) {
            WebView webView = TurbolinksSession.getDefault(this).getWebView();
            String mTitle = title != null ? title : webView.getTitle();
            getSupportActionBar().setTitle(mTitle);
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    @Override
    public void reloadVisitable() {
        turbolinksViewFrame.reload(TurbolinksSession.getDefault(this), route.getUrl());
    }

    @Override
    public void reloadSession() {
        refreshCookies();
        TurbolinksSession.getDefault(this).resetToColdBoot();
        TurbolinksSession.getDefault(this).visit(route.getUrl());
    }

    @Override
    public void renderComponent(TurbolinksRoute route) {
        turbolinksViewFrame.renderComponent(getReactInstanceManager(), route);
    }

    @Override
    public void injectJavaScript(String script) {
        WebView webView = TurbolinksSession.getDefault(this).getWebView();
        webView.evaluateJavascript(script, null);
    }

    @JavascriptInterface
    public void postMessage(String message) {
        getEventEmitter().emit(TURBOLINKS_MESSAGE, message);
    }

    private void setupAppOptions() {
        if (isInitial()) {
            TurbolinksSession session = TurbolinksSession.getDefault(this);
            WebSettings settings = session.getWebView().getSettings();
            if (userAgent != null) { settings.setUserAgentString(settings.getUserAgentString() + " " + userAgent); }
            if (loadingView != null) { setupProgressView(session, turbolinksViewFrame.getTurbolinksView(), loadingView); }
            if (messageHandler != null) session.addJavascriptInterface(this, messageHandler);
        }
    }

    private void setupProgressView(TurbolinksSession turbolinksSession, TurbolinksView turbolinksView, String loadingView) {
        View progressView = LayoutInflater.from(this).inflate(R.layout.custom_progress, turbolinksView, false);
        progressView.setBackground(new ColorDrawable(Color.WHITE));
        progressIndicator = progressView.findViewById(R.id.turbolinks_custom_progress_indicator);
        progressIndicator.startReactApplication(getReactInstanceManager(), loadingView, null);
        turbolinksSession.progressView(progressView, progressIndicator.getId(), 500);
    }

    private void handleVisitCompleted() {
        WebView webView = TurbolinksSession.getDefault(this).getWebView();
        try {
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(webView.getUrl());
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            getEventEmitter().emit(TURBOLINKS_VISIT_COMPLETED, params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void handleWebTitlePress() {
        try {
            WebView webView = TurbolinksSession.getDefault(this).getWebView();
            URL urlLocation = new URL(webView.getUrl());
            handleTitlePress(null, urlLocation.toString(), urlLocation.getPath());
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void visitTurbolinksView() {
        TurbolinksSession.getDefault(this)
                .activity(this)
                .adapter(this)
                .view(turbolinksViewFrame.getTurbolinksView())
                .visit(route.getUrl());
    }

    @SuppressWarnings("deprecation")
    private void refreshCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

}
