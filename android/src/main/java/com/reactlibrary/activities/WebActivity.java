package com.reactlibrary.activities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.reactlibrary.RNTurbolinksModule.INTENT_INITIAL_VISIT;
import static com.reactlibrary.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_USER_AGENT;
import static com.reactlibrary.util.TurbolinksRoute.INTENT_ACTIONS;

public class WebActivity extends ReactAppCompatActivity implements TurbolinksAdapter {

    private static final Integer HTTP_FAILURE = 0;
    private static final Integer NETWORK_FAILURE = 1;

    private TurbolinksRoute route;
    private String messageHandler;
    private String userAgent;
    private Boolean initialVisit;
    private Boolean navigationBarHidden;
    private TurbolinksView turbolinksView;
    private ArrayList<Bundle> actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        route = new TurbolinksRoute(getIntent());
        initialVisit = getIntent().getBooleanExtra(INTENT_INITIAL_VISIT, true);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);
        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);
        actions = getIntent().getParcelableArrayListExtra(INTENT_ACTIONS);

        setContentView(R.layout.activity_web);
        renderToolBar();

        turbolinksView = (TurbolinksView) findViewById(R.id.turbolinks_view);

        if (messageHandler != null) {
            TurbolinksSession.getDefault(this).addJavascriptInterface(this, messageHandler);
        }
        if (userAgent != null) {
            TurbolinksSession.getDefault(this).getWebView().getSettings().setUserAgentString(userAgent);
        }
        TurbolinksSession.getDefault(this).activity(this).adapter(this).view(turbolinksView).visit(route.getUrl());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        TurbolinksSession.getDefault(this)
                .activity(this)
                .adapter(this)
                .restoreWithCachedSnapshot(true)
                .view(turbolinksView)
                .visit(route.getUrl());
    }

    @Override
    public void onReceivedError(int errorCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", NETWORK_FAILURE);
        params.putInt("statusCode", 0);
        params.putString("description", "Network Failure.");
        WebViewClient wb = new WebViewClient();
        getEventEmitter().emit("turbolinksError", params);
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", HTTP_FAILURE);
        params.putInt("statusCode", statusCode);
        params.putString("description", "HTTP Failure. Code:" + statusCode);
        getEventEmitter().emit("turbolinksError", params);
    }

    @Override
    public void visitProposedToLocationWithAction(String location, String action) {
        try {
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(location);
            params.putString("component", null);
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            params.putString("action", action);
            getEventEmitter().emit("turbolinksVisit", params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    @Override
    public void visitCompleted() {
        renderTitle();
    }

    @Override
    public void onPageFinished() {
    }

    @Override
    public void pageInvalidated() {
    }

    @Override
    public void onBackPressed() {
        if (initialVisit) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (actions == null) return true;
        getMenuInflater().inflate(R.menu.turbolinks_menu, menu);
        for (int i = 0; i < actions.size(); i++) {
            Bundle bundle = actions.get(i);
            String title = bundle.getString("title");
            Bundle icon = bundle.getBundle("icon");
            Boolean asButton = bundle.getBoolean("button");
            MenuItem menuItem = menu.add(Menu.NONE, Menu.NONE, i, title);
            renderActionIcon(menuItem, icon);
            if (asButton) menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            WebView webView = TurbolinksSession.getDefault(this).getWebView();
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(webView.getUrl());
            params.putString("component", null);
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            params.putInt("position", item.getOrder());
            getEventEmitter().emit("turbolinksActionSelected", params);
            return true;
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
            return super.onOptionsItemSelected(item);
        }
    }

    @JavascriptInterface
    public void postMessage(String message) {
        getEventEmitter().emit("turbolinksMessage", message);
    }

    public void reloadSession() {
        TurbolinksSession.getDefault(this).getWebView().reload();
    }

    private RCTDeviceEventEmitter getEventEmitter() {
        return getReactInstanceManager().getCurrentReactContext().getJSModule(RCTDeviceEventEmitter.class);
    }

    private void renderToolBar() {
        Toolbar turbolinksToolbar = (Toolbar) findViewById(R.id.turbolinks_toolbar);
        setSupportActionBar(turbolinksToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!initialVisit);
        getSupportActionBar().setDisplayShowHomeEnabled(!initialVisit);
        getSupportActionBar().setTitle(null);
        handleTitlePress(turbolinksToolbar);
        if (navigationBarHidden) getSupportActionBar().hide();
    }

    private void renderTitle() {
        WebView webView = TurbolinksSession.getDefault(this).getWebView();
        String title = route.getTitle() != null ? route.getTitle() : webView.getTitle();
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(route.getSubtitle());
    }

    private void handleTitlePress(Toolbar toolbar) {
        final WebView webView = TurbolinksSession.getDefault(this).getWebView();
        toolbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    WritableMap params = Arguments.createMap();
                    URL urlLocation = new URL(webView.getUrl());
                    params.putString("component", null);
                    params.putString("url", urlLocation.toString());
                    params.putString("path", urlLocation.getPath());
                    getEventEmitter().emit("turbolinksTitlePress", params);
                } catch (MalformedURLException e) {
                    Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
                }
            }
        });
    }

    private void renderActionIcon(MenuItem menuItem, Bundle icon) {
        if (icon == null)  return;
        Uri uri = Uri.parse(icon.getString("uri"));
        Drawable drawableIcon = Drawable.createFromPath(uri.getPath());
        menuItem.setIcon(drawableIcon);
    }
}
