
package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;

import java.util.Map;

public class RNTurbolinksModule extends ReactContextBaseJavaModule {

    private static final String INTENT_URL = "intentUrl";
    private static final String INTENT_FROM = "intentFrom";
    private static final String INTENT_ACTION = "intentAction";
    private static final String INTENT_REACT_TAG = "intentReactTag";

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNTurbolinksModule";
    }

    @ReactMethod
    public void replaceWith(ReadableMap routeParam) {
    }

    @ReactMethod
    public void visit(Integer reactTag, ReadableMap rp) {
        String url = rp.getString("url");
        String action = rp.hasKey("action") ? rp.getString("action") : "advance";
        presentActivityForSession(reactTag, url, action);
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

    private void presentActivityForSession(Integer reactTag, String url, String action) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(getReactApplicationContext(), CustomActivity.class);
        intent.putExtra(INTENT_URL, url);
        intent.putExtra(INTENT_FROM, activity.getClass().getSimpleName());
        intent.putExtra(INTENT_ACTION, action);
        intent.putExtra(INTENT_REACT_TAG, reactTag);
        if (action.equals("replace")) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
}