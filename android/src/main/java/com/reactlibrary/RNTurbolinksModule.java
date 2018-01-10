
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
    private static final String INTENT_REACT_TAG = "intentReactTag";
    private static final String INTENT_FROM = "intentFrom";

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
    public void visit(final int reactTag, final ReadableMap routeParam) {
        final Activity activity = getCurrentActivity();
        final String url = routeParam.getString("url");
        Intent intent = new Intent(getReactApplicationContext(), CustomActivity.class);
        intent.putExtra(INTENT_URL, url);
        intent.putExtra(INTENT_REACT_TAG, reactTag);
        intent.putExtra(INTENT_FROM, activity.getClass().getSimpleName());
        activity.startActivity(intent);
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

}