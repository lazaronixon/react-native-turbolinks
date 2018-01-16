
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
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

import java.util.Map;

public class RNTurbolinksModule extends ReactContextBaseJavaModule {

    private static final String INTENT_URL = "intentUrl";
    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_PROPS = "intentProps";
    private static final String INTENT_FROM = "intentFrom";
    private static final String INTENT_ACTION = "intentAction";
    private static final String INTENT_USER_AGENT = "intentUserAgent";
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
        String url = rp.hasKey("url") ? rp.getString("url") : null;
        String action = rp.hasKey("action") ? rp.getString("action") : "advance";
        if (url != null) {
            presentActivityForSession(reactTag, url, action);
        } else {
            String component = rp.getString("component");
            ReadableMap props = rp.hasKey("passProps") ? rp.getMap("passProps") : null;
            Bundle bundleProps =  props != null ? Arguments.toBundle(props) : null;
            presentNativeView(component, bundleProps, action);
        }
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

    private void presentActivityForSession(final Integer reactTag, final String url, final String action) {
        getNativeModule().addUIBlock(new UIBlock() {
            public void execute(NativeViewHierarchyManager nvhm) {
                RNTurbolinksView view = (RNTurbolinksView) nvhm.resolveView(reactTag);
                Activity activity = getCurrentActivity();
                Intent intent = new Intent(getReactApplicationContext(), CustomActivity.class);
                intent.putExtra(INTENT_URL, url);
                intent.putExtra(INTENT_FROM, activity.getClass().getSimpleName());
                intent.putExtra(INTENT_ACTION, action);
                intent.putExtra(INTENT_USER_AGENT, view.getUserAgent());
                intent.putExtra(INTENT_REACT_TAG, reactTag);
                if (action.equals("replace")) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });
    }

    private void presentNativeView(String component, Bundle props, String action) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(getReactApplicationContext(), NativeActivity.class);
        intent.putExtra(INTENT_COMPONENT, component);
        intent.putExtra(INTENT_PROPS, props);
        intent.putExtra(INTENT_ACTION, action);
        activity.startActivity(intent);
    }

    private UIManagerModule getNativeModule() {
        return getReactApplicationContext().getNativeModule(UIManagerModule.class);
    }
}