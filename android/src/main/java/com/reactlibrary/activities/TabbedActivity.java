package com.reactlibrary.activities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;
import com.reactlibrary.util.TurbolinksViewPager;

import java.util.ArrayList;

import static com.reactlibrary.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_USER_AGENT;
import static com.reactlibrary.util.TurbolinksTabBar.INTENT_ROUTES;
import static com.reactlibrary.util.TurbolinksTabBar.INTENT_SELECTED_INDEX;

public class TabbedActivity extends ReactAppCompatActivity implements GenericActivity {

    private HelperActivity helperAct;
    private ArrayList<Bundle> routes;
    private String messageHandler;
    private String userAgent;
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
        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);

        helperAct.renderToolBar((Toolbar) findViewById(R.id.toolbar));
        helperAct.renderTitle();
        renderViewPager((TurbolinksViewPager) findViewById(R.id.viewpager));
        renderBottomNav((BottomNavigationView) findViewById(R.id.navigation));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return helperAct.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return helperAct.onOptionsItemSelected(item);
    }

    @Override
    public void renderTitle() {
        helperAct.renderTitle();
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

    public String getMessageHandler() {
        return messageHandler;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public ReactInstanceManager getManager() {
        return getReactInstanceManager();
    }

    private void renderBottomNav(BottomNavigationView bottomNav) {
        Menu menu = bottomNav.getMenu();
        setupNavigation(bottomNav);
        bottomNav.setSelectedItemId(selectedIndex);
        for (int i = 0; i < routes.size(); i++) {
            TurbolinksRoute route = new TurbolinksRoute(routes.get(i));
            MenuItem menuItem = menu.add(Menu.NONE, i, i, route.getTabTitle());
            renderTabIcon(menu, menuItem, route.getTabIcon());
        }
    }

    private void renderViewPager(TurbolinksViewPager viewPager) {
        TurbolinksPagerAdapter adapter = new TurbolinksPagerAdapter(this);
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
                        TurbolinksViewPager viewPager = findViewById(R.id.viewpager);
                        viewPager.setCurrentItem(item.getItemId(), false);
                        return true;
                    }
                });
    }

    private class TurbolinksPagerAdapter extends PagerAdapter {

        private ArrayList<TabbedView> viewList = new ArrayList<>();

        public TurbolinksPagerAdapter(TabbedActivity tabbedActivity) {
            for(Bundle route: routes) {
                TurbolinksRoute tRoute = new TurbolinksRoute(route);
                TabbedView tabView =  new TabbedView(tabbedActivity, tRoute);
                tabView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                viewList.add(tabView);
            }
        }

        public View getItem(int position) {
            return viewList.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = viewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
