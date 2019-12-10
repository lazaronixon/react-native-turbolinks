package com.lazaronixon.rnturbolinks.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.*;
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

import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.*;

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
        setupToolBar();

        turbolinksViewFrame = findViewById(R.id.turbolinks_view);

        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);
        loadingView = getIntent().getStringExtra(INTENT_LOADING_VIEW);
        injectedJavaScript = getIntent().getStringExtra(INTENT_INJECTED_JAVASCRIPT);

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
            sessionWebView().evaluateJavascript(injectedJavaScript, null);
        }
    }

    @Override
    public void pageInvalidated() {}

    @Override
    public void renderTitle(String title, String subtitle) {
        toolBar.setTitle(title != null ? title : sessionWebView().getTitle());
        toolBar.setSubtitle(subtitle);
    }

    @Override
    public void reloadVisitable() {
        turbolinksViewFrame.reload(TurbolinksSession.getDefault(this), route.getUrl());
    }

    @Override
    public void reloadSession() {
        flushCookies();
        TurbolinksSession.getDefault(this).resetToColdBoot();
        TurbolinksSession.getDefault(this).visit(route.getUrl());
    }

    @Override
    public void renderComponent(TurbolinksRoute route) {
        turbolinksViewFrame.renderComponent(getReactInstanceManager(), route);
    }

    @Override
    public void injectJavaScript(String script) {
        sessionWebView().evaluateJavascript(script, null);
    }

    @JavascriptInterface
    public void postMessage(String message) {
        getEventEmitter().emit(TURBOLINKS_MESSAGE, message);
    }

    private WebView sessionWebView() {
        return TurbolinksSession.getDefault(this).getWebView();
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
        try {
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(sessionWebView().getUrl());
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            getEventEmitter().emit(TURBOLINKS_VISIT_COMPLETED, params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void handleWebTitlePress() {
        try {
            URL urlLocation = new URL(sessionWebView().getUrl());
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
    private void flushCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

}
