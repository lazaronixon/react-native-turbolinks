
package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.ReactConstants;
import com.reactlibrary.activities.NativeActivity;
import com.reactlibrary.activities.WebActivity;
import com.reactlibrary.util.TurbolinksRoute;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class RNTurbolinksModule extends ReactContextBaseJavaModule {

    private static final String INTENT_URL = "intentUrl";
    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_INITIAL_VISIT = "intentInitialVisit";
    private static final String INTENT_PROPS = "intentProps";
    private static final String INTENT_MESSAGE_HANDLER = "intentMessageHandler";
    private static final String INTENT_USER_AGENT = "intentUserAgent";
    private static final String INTENT_MODAL = "intentModal";

    private static final String ACTION_ADVANCE = "advance";
    private static final String ACTION_REPLACE = "replace";

    private TurbolinksRoute prevRoute;
    private String messageHandler;
    private String userAgent;
    private Boolean initialVisit = true;

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNTurbolinksModule";
    }

    @ReactMethod
    public void visit(ReadableMap route) {
        TurbolinksRoute tRoute = new TurbolinksRoute(route);
        if (tRoute.getUrl() != null) {
            presentActivityForSession(tRoute);
            this.initialVisit = false;
        } else {
            presentNativeView(tRoute);
            this.initialVisit = false;
        }
    }

    @ReactMethod
    public void setMessageHandler(String messageHandler) {
        this.messageHandler = messageHandler;
    }

    @ReactMethod
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @ReactMethod
    public void reloadVisitable() {
        prevRoute.setAction(ACTION_REPLACE);
        presentActivityForSession(prevRoute);
    }

    @ReactMethod
    public void reloadSession() {
        ((WebActivity) getCurrentActivity()).reloadSession();
    }

    @ReactMethod
    public void dismiss() { getCurrentActivity().finish(); }

    @Override
    public Map getConstants() {
        return MapBuilder.of(
                "ErrorCode", MapBuilder.of("httpFailure", 0, "networkFailure", 1),
                "Action", MapBuilder.of("advance", "advance", "replace", "replace", "restore", "restore")
        );
    }

    private void presentActivityForSession(TurbolinksRoute route) {
        try {
            Activity activity = getCurrentActivity();
            URL prevUrl = prevRoute != null ? new URL(prevRoute.getUrl()) : new URL(route.getUrl());
            URL nextUrl = new URL(route.getUrl());
            if (Objects.equals(prevUrl.getHost(), nextUrl.getHost())) {
                Intent intent = new Intent(getReactApplicationContext(), WebActivity.class);
                intent.putExtra(INTENT_URL, route.getUrl());
                intent.putExtra(INTENT_MESSAGE_HANDLER, messageHandler);
                intent.putExtra(INTENT_USER_AGENT, userAgent);
                intent.putExtra(INTENT_INITIAL_VISIT, initialVisit);
                activity.startActivity(intent);
                if (route.getAction().equals(ACTION_REPLACE)) activity.finish();
                this.prevRoute = route;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(route.getUrl()));
                activity.startActivity(intent);
            }
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void presentNativeView(TurbolinksRoute route) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(getReactApplicationContext(), NativeActivity.class);
        intent.putExtra(INTENT_COMPONENT, route.getComponent());
        intent.putExtra(INTENT_PROPS, route.getPassProps());
        intent.putExtra(INTENT_MODAL, route.getModal());
        intent.putExtra(INTENT_INITIAL_VISIT, initialVisit);
        activity.startActivity(intent);
        if (route.getAction().equals(ACTION_REPLACE)) activity.finish();
    }

}