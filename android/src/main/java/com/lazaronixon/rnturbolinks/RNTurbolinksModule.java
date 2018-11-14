package com.lazaronixon.rnturbolinks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.ReactConstants;
import com.lazaronixon.rnturbolinks.activities.ApplicationActivity;
import com.lazaronixon.rnturbolinks.activities.NativeActivity;
import com.lazaronixon.rnturbolinks.activities.WebActivity;
import com.lazaronixon.rnturbolinks.util.NavBarStyle;
import com.lazaronixon.rnturbolinks.util.TurbolinksRoute;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;
import static com.lazaronixon.rnturbolinks.util.TurbolinksRoute.ACTION_REPLACE;

public class RNTurbolinksModule extends ReactContextBaseJavaModule {

    public static final String INTENT_MESSAGE_HANDLER = "intentMessageHandler";
    public static final String INTENT_USER_AGENT = "intentUserAgent";
    public static final String INTENT_ROUTE = "intentRoute";
    public static final String INTENT_LOADING_VIEW = "intentLoadingView";
    public static final String INTENT_INITIAL = "intentInitial";
    public static final String INTENT_NAV_BAR_STYLE = "intentNavBarStyle";

    private TurbolinksRoute prevRoute;
    private String messageHandler;
    private String userAgent;
    private String loadingView;
    private Intent initialIntent;
    private ReadableMap navBarStyle;

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNTurbolinksModule";
    }

    @ReactMethod
    public void startSingleScreenApp(ReadableMap route, ReadableMap options) {
        setAppOptions(options);
        visit(route, true);
    }

    @ReactMethod
    public void visit(ReadableMap route) { visit(route, false); }

    @ReactMethod
    public void replaceWith(final ReadableMap route) {
        runOnUiThread(new Runnable() {
            public void run() {
                TurbolinksRoute tRoute = new TurbolinksRoute(route);
                ((ApplicationActivity) getCurrentActivity()).renderComponent(tRoute);
            }
        });
    }

    @ReactMethod
    public void reloadVisitable() {
        runOnUiThread(new Runnable() {
            public void run() {
                ((ApplicationActivity) getCurrentActivity()).reloadVisitable();
            }
        });
    }

    @ReactMethod
    public void reloadSession() {
        runOnUiThread(new Runnable() {
            public void run() {
                CookieSyncManager.getInstance().sync();
                ((ApplicationActivity) getCurrentActivity()).reloadSession();
            }
        });
    }

    @ReactMethod
    public void dismiss(Boolean animated) {
        getCurrentActivity().finish();
    }

    @ReactMethod
    public void popToRoot(Boolean animated) { getCurrentActivity().startActivity(initialIntent); }

    @ReactMethod
    public void back(Boolean animated) { getCurrentActivity().finish(); }

    @ReactMethod
    public void renderTitle(final String title, final String subtitle) {
        runOnUiThread(new Runnable() {
            public void run() {
                ((ApplicationActivity) getCurrentActivity()).renderTitle(title, subtitle);
            }
        });
    }

    @ReactMethod
    public void renderActions(final ReadableArray actions) {
        runOnUiThread(new Runnable() {
            public void run() {
                ApplicationActivity act = (ApplicationActivity) getCurrentActivity();
                act.setActions(Arguments.toList(actions));
                act.invalidateOptionsMenu();
            }
        });
    }

    @ReactMethod
    public void evaluateJavaScript(final String script, final Promise promise) {
        runOnUiThread(new Runnable() {
            public void run() {
                ((ApplicationActivity) getCurrentActivity()).evaluateJavaScript(script, promise);
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

    private void setAppOptions(ReadableMap opts) {
        this.messageHandler = opts.hasKey("messageHandler") ? opts.getString("messageHandler") : null;
        this.userAgent = opts.hasKey("userAgent") ? opts.getString("userAgent") : null;
        this.loadingView = opts.hasKey("loadingView") ? opts.getString("loadingView") : null;
        this.navBarStyle = opts.hasKey("navBarStyle") ? opts.getMap("navBarStyle") : null;
    }

    private void visit(ReadableMap route, boolean initial ) {
        TurbolinksRoute tRoute = new TurbolinksRoute(route);
        if (tRoute.getUrl() != null) {
            presentActivityForSession(tRoute, initial);
        } else {
            presentNativeView(tRoute, initial);
        }
    }

    private void presentActivityForSession(TurbolinksRoute route, boolean initial) {
        try {
            Activity act = getCurrentActivity();
            boolean isActionReplace = route.getAction().equals(ACTION_REPLACE);
            boolean isInitial = isActionReplace ? getIntentInitial(act) : initial;
            URL prevUrl = initial || prevRoute == null ? new URL(route.getUrl()) : new URL(prevRoute.getUrl());
            URL nextUrl = new URL(route.getUrl());
            if (Objects.equals(prevUrl.getHost(), nextUrl.getHost())) {
                Intent intent = new Intent(getReactApplicationContext(), WebActivity.class);
                intent.putExtra(INTENT_MESSAGE_HANDLER, messageHandler);
                intent.putExtra(INTENT_USER_AGENT, userAgent);
                intent.putExtra(INTENT_INITIAL, isInitial);
                intent.putExtra(INTENT_LOADING_VIEW, loadingView);
                intent.putExtra(INTENT_ROUTE, route);
                intent.putExtra(INTENT_NAV_BAR_STYLE, navBarStyle != null ? new NavBarStyle(navBarStyle) : null);
                act.startActivity(intent);

                if (isInitial) initialIntent = intent;
                if (isInitial) TurbolinksSession.resetDefault();
                if (isActionReplace) act.finish();
                this.prevRoute = route;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(route.getUrl()));
                act.startActivity(intent);
            }
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    private void presentNativeView(TurbolinksRoute route, boolean initial) {
        Activity act = getCurrentActivity();
        boolean isActionReplace = route.getAction().equals(ACTION_REPLACE);
        boolean isInitial = isActionReplace ? getIntentInitial(act) : initial;
        Intent intent = new Intent(getReactApplicationContext(), NativeActivity.class);
        intent.putExtra(INTENT_ROUTE, route);
        intent.putExtra(INTENT_INITIAL, isInitial);
        intent.putExtra(INTENT_NAV_BAR_STYLE, navBarStyle != null ? new NavBarStyle(navBarStyle) : null);
        act.startActivity(intent);

        if (isInitial) initialIntent = intent;
        if (isInitial) TurbolinksSession.resetDefault();
        if (isActionReplace) act.finish();
    }

    private boolean getIntentInitial(Activity act) {
        return act.getIntent().getBooleanExtra(INTENT_INITIAL, true);
    }

}