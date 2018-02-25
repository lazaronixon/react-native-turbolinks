package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
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

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNTurbolinksModule";
    }

    @ReactMethod
    public void visit(ReadableMap route, Boolean initial) {
        TurbolinksRoute tRoute = new TurbolinksRoute(route);
        if (tRoute.getUrl() != null) {
            presentActivityForSession(tRoute, initial);
        } else {
            presentNativeView(tRoute, initial);
        }
    }

    @ReactMethod
    public void replaceWith(ReadableMap route, Integer tabIndex) {
        TurbolinksRoute tRoute = new TurbolinksRoute(route);
        tRoute.setAction(ACTION_REPLACE);
        presentNativeView(tRoute, false);
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
    public void visitTabBar(ReadableArray routes, Integer selectedIndex) {
        presentTabbedView(new TurbolinksTabBar(routes, selectedIndex));
    }

    @ReactMethod
    public void reloadVisitable() {
        prevRoute.setAction(ACTION_REPLACE);
        presentActivityForSession(prevRoute, false);
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
    public void setTabBarStyle(ReadableMap style) {
    }

    @ReactMethod
    public void renderTitle(final String title, final String subtitle, Integer tabIndex) {
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
    public void renderActions(final ReadableArray actions, Integer tabIndex) {
        runOnUiThread(new Runnable() {
            public void run() {
                GenericActivity act = (GenericActivity) getCurrentActivity();
                Activity activity = (Activity) act;
                act.getRoute().setActions(Arguments.toList(actions));
                activity.invalidateOptionsMenu();
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

    private void presentActivityForSession(TurbolinksRoute route, Boolean initial) {
        try {
            ReactContext context = getReactApplicationContext();
            Boolean isActionReplace = route.getAction().equals(ACTION_REPLACE);
            URL prevUrl = initial || prevRoute == null ? new URL(route.getUrl()) : new URL(prevRoute.getUrl());
            URL nextUrl = new URL(route.getUrl());
            if (Objects.equals(prevUrl.getHost(), nextUrl.getHost())) {
                Intent intent = new Intent(getReactApplicationContext(), WebActivity.class);
                intent.putExtra(INTENT_MESSAGE_HANDLER, messageHandler);
                intent.putExtra(INTENT_USER_AGENT, userAgent);
                intent.putExtra(INTENT_INITIAL_VISIT, isActionReplace ? getCurrInitVisit() : initial);
                intent.putExtra(INTENT_NAVIGATION_BAR_HIDDEN, navigationBarHidden);
                intent.putExtra(INTENT_ROUTE, route);
                context.startActivity(intent);
                if (isActionReplace) getCurrentActivity().finish();
                this.prevRoute = route;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(route.getUrl()));
                context.startActivity(intent);
            }
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void presentNativeView(TurbolinksRoute route, Boolean initial) {
        ReactContext context = getReactApplicationContext();
        Boolean isActionReplace = route.getAction().equals(ACTION_REPLACE);
        Intent intent = new Intent(getReactApplicationContext(), NativeActivity.class);
        intent.putExtra(INTENT_INITIAL_VISIT, isActionReplace ? getCurrInitVisit() : initial);
        intent.putExtra(INTENT_NAVIGATION_BAR_HIDDEN, navigationBarHidden);
        intent.putExtra(INTENT_ROUTE, route);
        context.startActivity(intent);
        if (isActionReplace) getCurrentActivity().finish();
    }

    private void presentTabbedView(TurbolinksTabBar tabBar) {
        ReactContext context = getReactApplicationContext();
        Intent intent = new Intent(getReactApplicationContext(), TabbedActivity.class);
        intent.putExtra(INTENT_MESSAGE_HANDLER, messageHandler);
        intent.putExtra(INTENT_USER_AGENT, userAgent);
        intent.putExtra(INTENT_NAVIGATION_BAR_HIDDEN, navigationBarHidden);
        intent.putExtra(INTENT_SELECTED_INDEX, tabBar.getSelectedIndex());
        intent.putParcelableArrayListExtra(INTENT_ROUTES, tabBar.getRoutes());
        context.startActivity(intent);
    }

    private Boolean getCurrInitVisit() {
        return getCurrentActivity().getIntent().getBooleanExtra(INTENT_INITIAL_VISIT, true);
    }

}