
package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.ReactConstants;
import com.reactlibrary.activities.GenericActivity;
import com.reactlibrary.activities.NativeActivity;
import com.reactlibrary.activities.TabbedActivity;
import com.reactlibrary.activities.WebActivity;
import com.reactlibrary.util.TurbolinksRoute;
import com.reactlibrary.util.TurbolinksTabBar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;
import static com.reactlibrary.util.TurbolinksRoute.ACTION_REPLACE;
import static com.reactlibrary.util.TurbolinksTabBar.INTENT_ROUTES;
import static com.reactlibrary.util.TurbolinksTabBar.INTENT_SELECTED_INDEX;

public class RNTurbolinksModule extends ReactContextBaseJavaModule {

    public static final String INTENT_NAVIGATION_BAR_HIDDEN = "intentNavigationBarHidden";
    public static final String INTENT_MESSAGE_HANDLER = "intentMessageHandler";
    public static final String INTENT_USER_AGENT = "intentUserAgent";
    public static final String INTENT_INITIAL_VISIT = "intentInitialVisit";
    public static final String INTENT_ROUTE = "intentRoute";

    private TurbolinksRoute prevRoute;
    private String messageHandler;
    private String userAgent;
    private Boolean navigationBarHidden = false;
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
        } else {
            presentNativeView(tRoute);
        }
    }

    @ReactMethod
    public void replaceWith(ReadableMap route) {
        TurbolinksRoute tRoute = new TurbolinksRoute(route);
        tRoute.setAction(ACTION_REPLACE);
        presentNativeView(tRoute);
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
    public void setNavigationBarHidden(Boolean navigationBarHidden) {
        this.navigationBarHidden = navigationBarHidden;
    }

    @ReactMethod
    public void setTabBar(ReadableMap tabBar) {
        TurbolinksTabBar tTabBar = new TurbolinksTabBar(tabBar);
        presentTabbedView(tTabBar);
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
    public void dismiss() {
        getCurrentActivity().finish();
    }

    @ReactMethod
    public void back() {
        getCurrentActivity().onBackPressed();
    }

    @ReactMethod
    public void setNavigationBarStyle(ReadableMap style) {
    }

    @ReactMethod
    public void setLoadingStyle(ReadableMap style) {
    }

    @ReactMethod
    public void renderTitle(final String title, final String subtitle) {
        runOnUiThread(new Runnable() {
            public void run() {
                GenericActivity activity = (GenericActivity) getCurrentActivity();
                activity.getRoute().setTitle(title);
                activity.getRoute().setSubtitle(subtitle);
                activity.renderTitle();
            }
        });
    }

    @ReactMethod
    public void renderActions(final ReadableArray actions) {
        runOnUiThread(new Runnable() {
            public void run() {
                GenericActivity activity = (GenericActivity) getCurrentActivity();
                activity.getRoute().setActions(Arguments.toList(actions));
                ((Activity) activity).invalidateOptionsMenu();
            }
        });
    }

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
                intent.putExtra(INTENT_MESSAGE_HANDLER, messageHandler);
                intent.putExtra(INTENT_USER_AGENT, userAgent);
                intent.putExtra(INTENT_INITIAL_VISIT, initialVisit);
                intent.putExtra(INTENT_NAVIGATION_BAR_HIDDEN, navigationBarHidden);
                intent.putExtra(INTENT_ROUTE, route);
                activity.startActivity(intent);
                if (route.getAction().equals(ACTION_REPLACE)) activity.finish();
                this.prevRoute = route;
                this.initialVisit = false;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(route.getUrl()));
                activity.startActivity(intent);
                this.initialVisit = false;
            }
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void presentNativeView(TurbolinksRoute route) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(getReactApplicationContext(), NativeActivity.class);
        intent.putExtra(INTENT_INITIAL_VISIT, initialVisit);
        intent.putExtra(INTENT_NAVIGATION_BAR_HIDDEN, navigationBarHidden);
        intent.putExtra(INTENT_ROUTE, route);
        activity.startActivity(intent);
        if (route.getAction().equals(ACTION_REPLACE)) activity.finish();
        this.initialVisit = false;
    }

    private void presentTabbedView(TurbolinksTabBar tabBar) {
        Activity activity = getCurrentActivity();
        Intent intent = new Intent(getReactApplicationContext(), TabbedActivity.class);
        intent.putExtra(INTENT_MESSAGE_HANDLER, messageHandler);
        intent.putExtra(INTENT_USER_AGENT, userAgent);
        intent.putExtra(INTENT_NAVIGATION_BAR_HIDDEN, navigationBarHidden);
        intent.putExtra(INTENT_SELECTED_INDEX, tabBar.getSelectedIndex());
        intent.putParcelableArrayListExtra(INTENT_ROUTES, tabBar.getRoutes());
        activity.startActivity(intent);
        this.initialVisit = false;
    }

}