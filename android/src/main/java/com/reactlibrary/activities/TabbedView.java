package com.reactlibrary.activities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.FrameLayout;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.reactlibrary.util.TurbolinksRoute;
import com.reactlibrary.util.TurbolinksViewFrame;

import java.net.MalformedURLException;
import java.net.URL;

import static com.reactlibrary.activities.WebActivity.HTTP_FAILURE;
import static com.reactlibrary.activities.WebActivity.NETWORK_FAILURE;

public class TabbedView extends FrameLayout implements TurbolinksAdapter {

    private TabbedActivity act;
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

    public TabbedView(TabbedActivity act, TurbolinksRoute route, int index) {
        super(act.getApplicationContext());
        this.act = act;
        this.index = index;
        this.session = TurbolinksSession.getNew(getContext());

        if (route.getUrl() != null) {
            this.turbolinksViewFrame = new TurbolinksViewFrame(getContext());
            this.turbolinksViewFrame.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            visitTurbolinksView(this.turbolinksViewFrame.getTurbolinksView(), route.getUrl());
            addView(this.turbolinksViewFrame);
        } else {
            ReactRootView nativeView = new ReactRootView(getContext());
            nativeView.startReactApplication(act.getManager(), route.getComponent(), route.getPassProps());
            addView(nativeView);
        }
    }

    public void reload(String location) {
        turbolinksViewFrame.reload(session, location);
    }

    public void renderComponent(ReactInstanceManager manager, TurbolinksRoute route) {
        turbolinksViewFrame.renderComponent(manager, route);
    }

    private void visitTurbolinksView(TurbolinksView turbolinksView, String url) {
        WebSettings settings = session.getWebView().getSettings();
        if (act.getMessageHandler() != null) session.addJavascriptInterface(this, act.getMessageHandler());
        if (act.getUserAgent() != null) settings.setUserAgentString(act.getUserAgent());
        session.activity(act).adapter(this).view(turbolinksView).visit(url);
    }

    @Override
    public void onPageFinished() {
    }

    @Override
    public void onReceivedError(int errorCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", NETWORK_FAILURE);
        params.putInt("statusCode", 0);
        params.putString("description", "Network Failure.");
        params.putInt("tabIndex", index);
        act.getEventEmitter().emit("turbolinksError", params);
    }

    @Override
    public void pageInvalidated() {
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", HTTP_FAILURE);
        params.putInt("statusCode", statusCode);
        params.putString("description", "HTTP Failure. Code:" + statusCode);
        params.putInt("tabIndex", index);
        act.getEventEmitter().emit("turbolinksError", params);
    }

    @Override
    public void visitCompleted() {
    }

    @JavascriptInterface
    public void postMessage(String message) {
        act.getEventEmitter().emit("turbolinksMessage", message);
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
            act.getEventEmitter().emit("turbolinksVisit", params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }
}
