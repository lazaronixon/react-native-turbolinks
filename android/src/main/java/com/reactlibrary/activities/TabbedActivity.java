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

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;

import java.util.ArrayList;

import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.util.TurbolinksTabBar.INTENT_ROUTES;
import static com.reactlibrary.util.TurbolinksTabBar.INTENT_SELECTED_INDEX;

public class TabbedActivity extends ReactAppCompatActivity implements GenericActivity {


    private HelperActivity helperAct;
    private ArrayList<Bundle> routes;
    private Boolean navigationBarHidden;
    private Integer selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        helperAct = new HelperActivity(this);
        routes = getIntent().getParcelableArrayListExtra(INTENT_ROUTES);
        selectedIndex = getIntent().getIntExtra(INTENT_SELECTED_INDEX, 0);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);

        renderToolBar((Toolbar) findViewById(R.id.toolbar));
        renderTitle();
        renderBottomNav((BottomNavigationView) findViewById(R.id.navigation));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return helperAct.onSupportNavigateUp();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return helperAct.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return helperAct.onOptionsItemSelected(item);
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

    @Override
    public void renderTitle() {
        getSupportActionBar().setTitle(getRoute().getTitle());
        getSupportActionBar().setSubtitle(getRoute().getSubtitle());
    }

    @Override
    public void renderToolBar(Toolbar toolbar) {
        helperAct.renderToolBar(toolbar);
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
    public RCTDeviceEventEmitter getEventEmitter() {
        return getReactInstanceManager().getCurrentReactContext().getJSModule(RCTDeviceEventEmitter.class);
    }

    @Override
    public TurbolinksRoute getRoute() {
        return new TurbolinksRoute(routes.get(selectedIndex));
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
                        viewPager.setCurrentItem(item.getItemId());
                        return true;
                    }
                });
    }
}
