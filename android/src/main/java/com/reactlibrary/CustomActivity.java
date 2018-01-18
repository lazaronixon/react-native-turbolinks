package com.reactlibrary;

import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;

import java.net.MalformedURLException;
import java.net.URL;

public class CustomActivity extends ReactActivity implements TurbolinksAdapter {

    private static final String INTENT_URL = "intentUrl";
    private static final String INTENT_FROM = "intentFrom";
    private static final String INTENT_ACTION = "intentAction";
    private static final String INTENT_MESSAGE_HANDLER = "intentMessageHandler";
    private static final String INTENT_USER_AGENT = "intentUserAgent";
    private static final Integer HTTP_FAILURE = 0;
    private static final Integer NETWORK_FAILURE = 1;

    private String location;
    private String fromActivity;
    private String action;
    private String messageHandler;
    private String userAgent;
    private TurbolinksView turbolinksView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        location = getIntent().getStringExtra(INTENT_URL);
        fromActivity = getIntent().getStringExtra(INTENT_FROM);
        action = getIntent().getStringExtra(INTENT_ACTION);
        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);

        handleAnimation(true);
        setContentView(R.layout.activity_custom);
        turbolinksView = (TurbolinksView) findViewById(R.id.turbolinks_view);

        if (messageHandler != null)
            TurbolinksSession.getDefault(this).addJavascriptInterface(this, messageHandler);
        if (userAgent != null)
            TurbolinksSession.getDefault(this).getWebView().getSettings().setUserAgentString(userAgent);
        TurbolinksSession.getDefault(this).activity(this).adapter(this).view(turbolinksView).visit(location);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handleAnimation(false);
        TurbolinksSession.getDefault(this)
                .activity(this)
                .adapter(this)
                .restoreWithCachedSnapshot(true)
                .view(turbolinksView)
                .visit(location);
    }

    @Override
    public void onBackPressed() {
        if (fromActivity.equals("MainActivity")) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageFinished() {

    }

    @Override
    public void onReceivedError(int errorCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", NETWORK_FAILURE);
        params.putInt("statusCode", 0);
        getEventEmitter().emit("turbolinksError", params);
    }

    @Override
    public void pageInvalidated() {
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", HTTP_FAILURE);
        params.putInt("statusCode", statusCode);
        getEventEmitter().emit("turbolinksError", params);
    }

    @Override
    public void visitCompleted() {
    }

    @Override
    public void visitProposedToLocationWithAction(String location, String action) {
        try {
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(location);
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            params.putString("action", action);
            getEventEmitter().emit("turbolinksVisit", params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void postMessage(String message) {
        getEventEmitter().emit("turbolinksMessage", message);
    }

    private void handleAnimation(Boolean isForward) {
        if (fromActivity.equals("MainActivity") && isForward) {
            overridePendingTransition(R.anim.nothing, R.anim.nothing);
        } else if (action.equals("advance")) {
            if (isForward) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else if (action.equals("replace")) {
            overridePendingTransition(R.anim.nothing, R.anim.nothing);
        }
    }

    private RCTDeviceEventEmitter getEventEmitter() {
        return getReactInstanceManager().getCurrentReactContext().getJSModule(RCTDeviceEventEmitter.class);
    }

}
