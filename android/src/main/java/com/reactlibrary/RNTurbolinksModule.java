
package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.ReactConstants;

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


    private String messageHandler;
    private String userAgent;
    private String prevLocation;
    private Boolean initialVisit = true;

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNTurbolinksModule";
    }

    @ReactMethod
    public void visit(ReadableMap rp) {
        String url = rp.hasKey("url") ? rp.getString("url") : null;
        String action = rp.hasKey("action") ? rp.getString("action") : "advance";
        if (url != null) {
            presentActivityForSession(url, action);
            this.initialVisit = false;
        } else {
            String component = rp.getString("component");
            Boolean modal = rp.hasKey("modal") ? rp.getBoolean("modal") : false;
            ReadableMap props = rp.hasKey("passProps") ? rp.getMap("passProps") : null;
            Bundle bundleProps = props != null ? Arguments.toBundle(props) : null;
            presentNativeView(component, modal, bundleProps, action);
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
        presentActivityForSession(prevLocation, "replace");
    }

    @ReactMethod
    public void reloadSession() {
        ((WebActivity) getCurrentActivity()).reloadSession();
    }

    @ReactMethod
    public void dismiss() throws InterruptedException {
        getCurrentActivity().finish();
    }

    @Override
    public Map getConstants() {
        return MapBuilder.of(
                "ErrorCode", MapBuilder.of("httpFailure", 0, "networkFailure", 1),
                "Action", MapBuilder.of("advance", "advance", "replace", "replace", "restore", "restore")
        );
    }

    private void presentActivityForSession(String url, String action) {
        try {
            Activity activity = getCurrentActivity();
            URL prevUrl = prevLocation != null ? new URL(prevLocation) : new URL(url);
            URL nextUrl = new URL(url);
            if (Objects.equals(prevUrl.getHost(), nextUrl.getHost())) {
                Intent intent = new Intent(getReactApplicationContext(), WebActivity.class);
                intent.putExtra(INTENT_URL, url);
                intent.putExtra(INTENT_MESSAGE_HANDLER, messageHandler);
                intent.putExtra(INTENT_USER_AGENT, userAgent);
                intent.putExtra(INTENT_INITIAL_VISIT, initialVisit);
                activity.startActivity(intent);
                if (action.equals("replace")) activity.finish();
                this.prevLocation = url;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);
            }
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void presentNativeView(String component, Boolean modal, Bundle props, String action) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(getReactApplicationContext(), NativeActivity.class);
        intent.putExtra(INTENT_COMPONENT, component);
        intent.putExtra(INTENT_PROPS, props);
        intent.putExtra(INTENT_MODAL, modal);
        intent.putExtra(INTENT_INITIAL_VISIT, initialVisit);
        activity.startActivity(intent);
        if (action.equals("replace")) activity.finish();
    }

    private String getCurrentActivityName() {
        return getCurrentActivity().getClass().getSimpleName();
    }

}