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
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;
import com.reactlibrary.util.TurbolinksViewPager;

import java.util.ArrayList;

import static com.reactlibrary.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.reactlibrary.RNTurbolinksModule.INTENT_NAVIGATION_BAR_HIDDEN;
import static com.reactlibrary.RNTurbolinksModule.INTENT_ROUTES;
import static com.reactlibrary.RNTurbolinksModule.INTENT_SELECTED_INDEX;
import static com.reactlibrary.RNTurbolinksModule.INTENT_USER_AGENT;

public class TabbedActivity extends ReactAppCompatActivity implements GenericActivity {

    private TurbolinksViewPager viewPager;
    private BottomNavigationView bottomNav;
    private Toolbar toolbar;

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

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        bottomNav = findViewById(R.id.navigation);

        helperAct = new HelperActivity(this);
        routes = getIntent().getParcelableArrayListExtra(INTENT_ROUTES);
        selectedIndex = getIntent().getIntExtra(INTENT_SELECTED_INDEX, 0);
        navigationBarHidden = getIntent().getBooleanExtra(INTENT_NAVIGATION_BAR_HIDDEN, false);
        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);

        helperAct.renderToolBar(toolbar);
        helperAct.renderTitle();
        setupViewPager();
        setupBottomNav();
    }

    @Override
    public void onBackPressed() {
        helperAct.backToHomeScreen(this);
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
        helperAct.handleTitlePress(toolbar, TabbedActivity.class.getSimpleName(), null, null);
    }

    @Override
    public Boolean getNavigationBarHidden() {
        return navigationBarHidden;
    }

    @Override
    public RCTDeviceEventEmitter getEventEmitter() { return helperAct.getEventEmitter(); }

    @Override
    public ReactInstanceManager getManager() { return getReactInstanceManager(); }

    @Override
    public TurbolinksRoute getRoute() {
        int index = viewPager.getCurrentItem();
        return new TurbolinksRoute(routes.get(index));
    }

    @Override
    public void renderComponent(TurbolinksRoute route, int tabIndex) {
        TabbedView tabbedView = tabIndex != -1 ? getTabbedViewByIndex(tabIndex) : getCurrentTabbedView();
        tabbedView.renderComponent(getReactInstanceManager(), route);
    }

    @Override
    public void reload() {
        getCurrentTabbedView().reload(getRoute().getUrl());
    }

    public String getMessageHandler() {
        return messageHandler;
    }

    public String getUserAgent() {
        return userAgent;
    }

    private void setupBottomNav() {
        Menu menu = bottomNav.getMenu();
        for (int i = 0; i < routes.size(); i++) {
            TurbolinksRoute route = new TurbolinksRoute(routes.get(i));
            MenuItem menuItem = menu.add(Menu.NONE, i, i, route.getTabTitle());
            renderTabIcon(menu, menuItem, route.getTabIcon());
        }
        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(MenuItem item) {
                        int index = item.getItemId();
                        viewPager.setCurrentItem(index, false);
                        return true;
                    }
                });
        bottomNav.setSelectedItemId(selectedIndex);
    }

    private void setupViewPager() {
        TurbolinksPagerAdapter adapter = new TurbolinksPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private void renderTabIcon(Menu menu, MenuItem menuItem, Bundle icon) {
        if (icon == null) return;
        Uri uri = Uri.parse(icon.getString("uri"));
        Drawable drawableIcon = Drawable.createFromPath(uri.getPath());
        menuItem.setIcon(drawableIcon);
    }

    private TabbedView getCurrentTabbedView() {
        return ((TurbolinksPagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem());
    }

    private TabbedView getTabbedViewByIndex(int index) {
        return ((TurbolinksPagerAdapter) viewPager.getAdapter()).getItem(index);
    }

    private class TurbolinksPagerAdapter extends PagerAdapter {

        private ArrayList<TabbedView> viewList = new ArrayList<>();

        public TurbolinksPagerAdapter(TabbedActivity tabbedActivity) {
            for (int i = 0; i < routes.size(); i++) {
                TurbolinksRoute tRoute = new TurbolinksRoute(routes.get(i));
                TabbedView tabView = new TabbedView(getReactInstanceManager(), tabbedActivity, tRoute, i);
                tabView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                viewList.add(tabView);
            }
        }

        public TabbedView getItem(int position) {
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
