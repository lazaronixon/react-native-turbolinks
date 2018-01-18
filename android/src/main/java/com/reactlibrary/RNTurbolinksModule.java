
package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;

import java.util.Map;

public class RNTurbolinksModule extends ReactContextBaseJavaModule {

    private static final String INTENT_URL = "intentUrl";
    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_PROPS = "intentProps";
    private static final String INTENT_FROM = "intentFrom";
    private static final String INTENT_ACTION = "intentAction";
    private static final String INTENT_MESSAGE_HANDLER = "intentMessageHandler";
    private static final String INTENT_USER_AGENT = "intentUserAgent";

    private String messageHandler;
    private String userAgent;

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNTurbolinksModule";
    }

    @ReactMethod
    public void replaceWith(ReadableMap rp) {
        String component = rp.getString("component");
        ReadableMap props = rp.hasKey("passProps") ? rp.getMap("passProps") : null;
        Bundle bundleProps = props != null ? Arguments.toBundle(props) : null;
        presentNativeView(component, bundleProps, "replace");
    }

    @ReactMethod
    public void visit(ReadableMap rp) {
        String url = rp.hasKey("url") ? rp.getString("url") : null;
        String action = rp.hasKey("action") ? rp.getString("action") : "advance";
        if (url != null) {
            presentActivityForSession(url, action);
        } else {
            String component = rp.getString("component");
            ReadableMap props = rp.hasKey("passProps") ? rp.getMap("passProps") : null;
            Bundle bundleProps = props != null ? Arguments.toBundle(props) : null;
            presentNativeView(component, bundleProps, action);
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
    }

    @ReactMethod
    public void reloadSession() {
    }

    @ReactMethod
    public void dismiss() {
    }

    @Override
    public Map getConstants() {
        return MapBuilder.of(
                "ErrorCode", MapBuilder.of("httpFailure", 0, "networkFailure", 1),
                "Action", MapBuilder.of("advance", "advance", "replace", "replace", "restore", "restore")
        );
    }

    private void presentActivityForSession(String url, String action) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(getReactApplicationContext(), CustomActivity.class);
        intent.putExtra(INTENT_URL, url);
        intent.putExtra(INTENT_FROM, activity.getClass().getSimpleName());
        intent.putExtra(INTENT_ACTION, action);
        intent.putExtra(INTENT_MESSAGE_HANDLER, messageHandler);
        intent.putExtra(INTENT_USER_AGENT, userAgent);
        activity.startActivity(intent);
        if (action.equals("replace")) activity.finish();
    }

    private void presentNativeView(String component, Bundle props, String action) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(getReactApplicationContext(), NativeActivity.class);
        intent.putExtra(INTENT_COMPONENT, component);
        intent.putExtra(INTENT_PROPS, props);
        intent.putExtra(INTENT_ACTION, action);
        activity.startActivity(intent);
        if (action.equals("replace")) activity.finish();
    }

}