package com.lazaronixon.rnturbolinks.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.lazaronixon.rnturbolinks.activities.TabbedActivity;

import java.net.MalformedURLException;
import java.net.URL;

import static com.lazaronixon.rnturbolinks.activities.WebActivity.HTTP_FAILURE;
import static com.lazaronixon.rnturbolinks.activities.WebActivity.NETWORK_FAILURE;
import static com.lazaronixon.rnturbolinks.activities.WebActivity.TURBOLINKS_ERROR;
import static com.lazaronixon.rnturbolinks.activities.WebActivity.TURBOLINKS_MESSAGE;
import static com.lazaronixon.rnturbolinks.activities.WebActivity.TURBOLINKS_VISIT;

public class TabbedView extends FrameLayout implements TurbolinksAdapter {

    private TabbedActivity act;
    private TurbolinksRoute route;
    private int index;
    private TurbolinksViewFrame turbolinksViewFrame;
    private TurbolinksSession session;

    public TabbedView(Context context) {
        super(context);
    }

    public TabbedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabbedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabbedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TabbedView(ReactInstanceManager manager, TabbedActivity act, TurbolinksRoute route, int index) {
        super(act.getApplicationContext());
        this.act = act;
        this.route = route;
        this.index = index;
        this.session = TurbolinksSession.getNew(getContext());
        if (route.getUrl() != null) {
            this.turbolinksViewFrame = new TurbolinksViewFrame(getContext());
            this.turbolinksViewFrame.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            visitTurbolinksView(this.turbolinksViewFrame.getTurbolinksView(), route.getUrl());
            addView(this.turbolinksViewFrame);
        } else {
            ReactRootView nativeView = new ReactRootView(getContext());
            nativeView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            nativeView.startReactApplication(manager, route.getComponent(), route.getPassProps());
            addView(nativeView);
        }
    }

    public void reload() {
        turbolinksViewFrame.reload(session, route.getUrl());
    }

    public void renderComponent(ReactInstanceManager manager, TurbolinksRoute route) {
        turbolinksViewFrame.renderComponent(manager, route);
    }

    public void evaluateJavaScript(String script, final Promise promise) {
        WebView webView = session.getWebView();
        webView.evaluateJavascript(script, new ValueCallback<String>() {
            public void onReceiveValue(String source) {
                promise.resolve(source);
            }
        });
    }

    private void visitTurbolinksView(TurbolinksView turbolinksView, String url) {
        WebSettings settings = session.getWebView().getSettings();
        if (act.getMessageHandler() != null)
            session.addJavascriptInterface(this, act.getMessageHandler());
        if (act.getUserAgent() != null) settings.setUserAgentString(act.getUserAgent());
        session.activity(act).adapter(this).view(turbolinksView).visit(url);
    }

    @Override
    public void onReceivedError(int errorCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", errorCode < 0 ? NETWORK_FAILURE : HTTP_FAILURE);
        params.putInt("statusCode", errorCode);
        params.putInt("tabIndex", index);
        params.putString("description", "Network Failure. Code: " + errorCode);
        act.getEventEmitter().emit(TURBOLINKS_ERROR, params);
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", HTTP_FAILURE);
        params.putInt("statusCode", statusCode);
        params.putInt("tabIndex", index);
        params.putString("description", "HTTP Failure. Code: " + statusCode);
        act.getEventEmitter().emit(TURBOLINKS_ERROR, params);
    }

    @Override
    public void onPageFinished() {
    }

    @Override
    public void pageInvalidated() {
    }

    @Override
    public void visitCompleted() {
    }

    @JavascriptInterface
    public void postMessage(String message) {
        act.getEventEmitter().emit(TURBOLINKS_MESSAGE, message);
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
            act.getEventEmitter().emit(TURBOLINKS_VISIT, params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }
}
