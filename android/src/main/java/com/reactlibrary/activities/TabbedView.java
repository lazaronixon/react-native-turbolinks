package com.reactlibrary.activities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.widget.FrameLayout;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.reactlibrary.util.TurbolinksRoute;

import java.net.MalformedURLException;
import java.net.URL;

public class TabbedView extends FrameLayout implements TurbolinksAdapter {

    private TabbedActivity act;

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

    public TabbedView(TabbedActivity act, TurbolinksRoute route) {
        super(act.getApplicationContext());
        this.act = act;

        if (route.getUrl() != null) {
            TurbolinksView turbolinksView = new TurbolinksView(getContext());
            turbolinksView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            visitTurbolinksView(turbolinksView, route.getUrl());
            addView(turbolinksView);
        } else {
            ReactRootView nativeView = new ReactRootView(getContext());
            nativeView.startReactApplication(act.getManager(), route.getComponent(), route.getPassProps());
            addView(nativeView);
        }
    }

    private void visitTurbolinksView(TurbolinksView turbolinksView, String url) {
        TurbolinksSession session = TurbolinksSession.getDefault(getContext());
        WebSettings settings = session.getWebView().getSettings();
        if (act.getMessageHandler() != null) session.addJavascriptInterface(this, act.getMessageHandler());
        if (act.getUserAgent() != null) settings.setUserAgentString(act.getUserAgent());
        session.activity(act).adapter(this).view(turbolinksView).visit(url);
        session.resetDefault();
    }

    @Override
    public void onPageFinished() {

    }

    @Override
    public void onReceivedError(int errorCode) {

    }

    @Override
    public void pageInvalidated() {

    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {

    }

    @Override
    public void visitCompleted() {

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
