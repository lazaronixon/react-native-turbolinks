package com.reactlibrary.activities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksPagerAdapter;
import com.reactlibrary.util.TurbolinksRoute;

import java.util.ArrayList;

import static com.reactlibrary.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_USER_AGENT;
import static com.reactlibrary.util.TurbolinksTabBar.INTENT_ROUTES;
import static com.reactlibrary.util.TurbolinksTabBar.INTENT_SELECTED_INDEX;

public class TabbedActivity extends ReactAppCompatActivity implements GenericNativeActivity, GenericWebActivity {


    private HelperNativeActivity helperNativeAct;
    private HelperWebActivity helperWebAct;
    private ArrayList<Bundle> routes;
    private String messageHandler;
    private String userAgent;
    private Boolean navigationBarHidden;
    private Integer selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        helperNativeAct = new HelperNativeActivity(this);
        helperWebAct = new HelperWebActivity(this);
        routes = getIntent().getParcelableArrayListExtra(INTENT_ROUTES);
        selectedIndex = getIntent().getIntExtra(INTENT_SELECTED_INDEX, 0);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);
        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);

        renderToolBar((Toolbar) findViewById(R.id.toolbar));
        renderTitle();
        renderViewPager((ViewPager) findViewById(R.id.viewpager));
        renderBottomNav((BottomNavigationView) findViewById(R.id.navigation));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return helperNativeAct.onSupportNavigateUp();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return helperNativeAct.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return helperNativeAct.onOptionsItemSelected(item);
    }

    @Override
    public void renderTitle() {
        helperNativeAct.renderTitle();
    }

    @Override
    public void renderToolBar(Toolbar toolbar) {
        helperNativeAct.renderToolBar(toolbar);
    }

    @Override
    public void handleTitlePress(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WritableMap params = Arguments.createMap();
                params.putString("component", "TabbedActivity");
                params.putString("url", null);
                params.putString("path", null);
                getEventEmitter().emit("turbolinksTitlePress", params);
            }
        });
    }

    @Override
    public boolean superOnOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Boolean getInitialVisit() {
        return true;
    }

    @Override
    public Boolean getNavigationBarHidden() {
        return navigationBarHidden;
    }


    @Override
    public TurbolinksRoute getRoute() {
        return new TurbolinksRoute(routes.get(selectedIndex));
    }

    @Override
    public void onSuperBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void visitComponent(ReactRootView view, ReactInstanceManager manager, TurbolinksRoute route) {
        helperNativeAct.visitComponent(view, manager, route);
    }

    @Override
    public TurbolinksView getTurbolinksView() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TurbolinksPagerAdapter adapter = (TurbolinksPagerAdapter) viewPager.getAdapter();
        return (TurbolinksView) adapter.getItem(viewPager.getCurrentItem());
    }

    @Override
    public String getMessageHandler() {
        return messageHandler;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    @Override
    @JavascriptInterface
    public void postMessage(String message) {
        helperWebAct.postMessage(message);
    }

    @Override
    public void reloadSession() {
        helperWebAct.reloadSession();
    }

    @Override
    public void visitTurbolinksView(TurbolinksView turbolinksView, String url) {
        helperWebAct.visitTurbolinksView(turbolinksView, url);
    }

    @Override
    public void onPageFinished() {
    }

    @Override
    public void onReceivedError(int errorCode) {
        helperWebAct.onReceivedError(errorCode);
    }

    @Override
    public void pageInvalidated() {
    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        helperWebAct.requestFailedWithStatusCode(statusCode);
    }

    @Override
    public void visitCompleted() {
        helperWebAct.visitCompleted();
    }

    @Override
    public void visitProposedToLocationWithAction(String location, String action) {
        helperWebAct.visitProposedToLocationWithAction(location, action);
    }

    @Override
    public RCTDeviceEventEmitter getEventEmitter() {
        return getReactInstanceManager().getCurrentReactContext().getJSModule(RCTDeviceEventEmitter.class);
    }

    private void renderBottomNav(BottomNavigationView bottomNav) {
        Menu menu = bottomNav.getMenu();
        setupNavigation(bottomNav);
        bottomNav.setSelectedItemId(selectedIndex);
        if (routes != null) {
            for (int i = 0; i < routes.size(); i++) {
                TurbolinksRoute route = new TurbolinksRoute(routes.get(i));
                MenuItem menuItem = menu.add(Menu.NONE, i, i, route.getTabTitle());
                renderTabIcon(menu, menuItem, route.getTabIcon());
            }
        }
    }

    private void renderViewPager(ViewPager viewPager) {
        TurbolinksPagerAdapter adapter = new TurbolinksPagerAdapter(getApplicationContext(), routes);
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getItem(i);
            TurbolinksRoute route = new TurbolinksRoute(routes.get(i));
            if (view instanceof ReactRootView) {
                ReactRootView rootView = (ReactRootView) view;
                visitComponent(rootView, getReactInstanceManager(), route);
            }
            if (view instanceof TurbolinksView) {
                TurbolinksView turbolinksView = (TurbolinksView) view;
                visitTurbolinksView(turbolinksView, route.getUrl());
                TurbolinksSession.resetDefault();
            }
        }
        viewPager.setAdapter(adapter);
    }

    private void renderTabIcon(Menu menu, MenuItem menuItem, Bundle icon) {
        if (icon == null) return;
        Uri uri = Uri.parse(icon.getString("uri"));
        Drawable drawableIcon = Drawable.createFromPath(uri.getPath());
        menuItem.setIcon(drawableIcon);
    }

    private void setupNavigation(BottomNavigationView bottomNav) {
        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(MenuItem item) {
                        ViewPager viewPager = findViewById(R.id.viewpager);
                        viewPager.setCurrentItem(item.getItemId(), false);
                        return true;
                    }
                });
    }

}
