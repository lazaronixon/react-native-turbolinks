package com.reactlibrary.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.FrameLayout.LayoutParams;

import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.reactlibrary.util.TurbolinksRoute;

import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

public class HelperWebActivity extends HelperActivity {

    private static final int HTTP_FAILURE = 0;
    private static final int NETWORK_FAILURE = 1;

    private GenericWebActivity act;
    private ReactRootView customView;

    public HelperWebActivity(GenericWebActivity genericWebActivity) {
        super(genericWebActivity);
        this.act = genericWebActivity;
    }

    public void onRestart() {
        Context context = act.getApplicationContext();
        Activity activity = (Activity) act;
        TurbolinksSession.getDefault(context)
                .activity(activity)
                .adapter(act)
                .restoreWithCachedSnapshot(true)
                .view(act.getTurbolinksView())
                .visit(act.getRoute().getUrl());
    }

    public void onReceivedError(int errorCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", NETWORK_FAILURE);
        params.putInt("statusCode", 0);
        params.putString("description", "Network Failure.");
        params.putInt("tabIndex", 0);
        act.getEventEmitter().emit("turbolinksError", params);
    }

    public void requestFailedWithStatusCode(int statusCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", HTTP_FAILURE);
        params.putInt("statusCode", statusCode);
        params.putString("description", "HTTP Failure. Code:" + statusCode);
        params.putInt("tabIndex", 0);
        act.getEventEmitter().emit("turbolinksError", params);
    }

    public void visitCompleted() {
        renderTitle();
        handleVisitCompleted();
    }

    public void visitProposedToLocationWithAction(String location, String action) {
        try {
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(location);
            params.putString("component", null);
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            params.putString("action", action);
            act.getEventEmitter().emit("turbolinksVisit", params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    public void handleVisitCompleted() {
        String javaScript = "document.documentElement.outerHTML";
        final WebView webView = TurbolinksSession.getDefault(act.getApplicationContext()).getWebView();
        webView.evaluateJavascript(javaScript, new ValueCallback<String>() {
            public void onReceiveValue(String source) {
                try {
                    WritableMap params = Arguments.createMap();
                    URL urlLocation = new URL(webView.getUrl());
                    params.putString("url", urlLocation.toString());
                    params.putString("path", urlLocation.getPath());
                    params.putString("source", unescapeJava(source));
                    act.getEventEmitter().emit("turbolinksVisitCompleted", params);
                } catch (MalformedURLException e) {
                    Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
                }
            }
        });
    }

    public void renderTitle() {
        WebView webView = TurbolinksSession.getDefault(act.getApplicationContext()).getWebView();
        String title = act.getRoute().getTitle() != null ? act.getRoute().getTitle() : webView.getTitle();
        act.getSupportActionBar().setTitle(title);
        act.getSupportActionBar().setSubtitle(act.getRoute().getSubtitle());
    }

    public void onBackPressed() {
        if (act.getInitialVisit()) {
            act.moveTaskToBack(true);
        } else {
            act.onSuperBackPressed();
        }
    }

    public void postMessage(String message) {
        act.getEventEmitter().emit("turbolinksMessage", message);
    }

    public void handleTitlePress(Toolbar toolbar) {
        final WebView webView = TurbolinksSession.getDefault(act.getApplicationContext()).getWebView();
        toolbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    WritableMap params = Arguments.createMap();
                    URL urlLocation = new URL(webView.getUrl());
                    params.putString("component", null);
                    params.putString("url", urlLocation.toString());
                    params.putString("path", urlLocation.getPath());
                    act.getEventEmitter().emit("turbolinksTitlePress", params);
                } catch (MalformedURLException e) {
                    Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
                }
            }
        });
    }

    public void visitTurbolinksView(TurbolinksView turbolinksView, String url) {
        Context context = act.getApplicationContext();
        Activity activity = (Activity) act;

        if (act.getMessageHandler() != null) {
            TurbolinksSession.getDefault(context).addJavascriptInterface(act, act.getMessageHandler());
        }
        if (act.getUserAgent() != null) {
            TurbolinksSession.getDefault(context).getWebView().getSettings().setUserAgentString(act.getUserAgent());
        }

        TurbolinksSession.getDefault(context).activity(activity).adapter(act).view(turbolinksView).visit(url);
    }

    public void renderComponent(TurbolinksRoute route, ReactInstanceManager manager) {
        Context context = act.getApplicationContext();
        TurbolinksView turbolinksView = act.getTurbolinksView();
        ViewGroup webViewParent = (ViewGroup) turbolinksView.getParent();
        turbolinksView.setVisibility(View.GONE);
        customView = new ReactRootView(context);
        customView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        customView.startReactApplication(manager, route.getComponent(), route.getPassProps());
        webViewParent.addView(customView);
    }

    public void reload() {
        TurbolinksView turbolinksView = act.getTurbolinksView();
        ViewGroup webViewParent = (ViewGroup) turbolinksView.getParent();
        visitTurbolinksView(turbolinksView, act.getRoute().getUrl());
        webViewParent.removeView(customView);
        turbolinksView.setVisibility(View.VISIBLE);
    }

}
