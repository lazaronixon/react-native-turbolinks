package com.lazaronixon.rnturbolinks.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.facebook.react.bridge.Promise;
import com.lazaronixon.rnturbolinks.R;
import com.lazaronixon.rnturbolinks.util.ImageLoader;
import com.lazaronixon.rnturbolinks.util.NavBarStyle;
import com.lazaronixon.rnturbolinks.util.TabBarStyle;
import com.lazaronixon.rnturbolinks.util.TabbedView;
import com.lazaronixon.rnturbolinks.util.TurbolinksRoute;
import com.lazaronixon.rnturbolinks.util.TurbolinksViewPager;

import java.util.ArrayList;

import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_LOADING_VIEW;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_MESSAGE_HANDLER;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_NAV_BAR_STYLE;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_ROUTES;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_SELECTED_INDEX;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_TAB_BAR_STYLE;
import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_USER_AGENT;

public class TabbedActivity extends GenericActivity {

    private TurbolinksViewPager viewPager;
    private BottomNavigationView bottomNav;

    private ArrayList<Bundle> routes;
    private String messageHandler;
    private String userAgent;
    private String loadingView;
    private int selectedIndex;
    private TabBarStyle tabBarStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        routes = getIntent().getParcelableArrayListExtra(INTENT_ROUTES);

        toolBar = findViewById(R.id.toolbar);
        route = new TurbolinksRoute(routes.get(selectedIndex));
        navBarStyle = getIntent().getParcelableExtra(INTENT_NAV_BAR_STYLE);
        tabBarStyle = getIntent().getParcelableExtra(INTENT_TAB_BAR_STYLE);

        viewPager = findViewById(R.id.viewpager);
        bottomNav = findViewById(R.id.navigation);

        selectedIndex = getIntent().getIntExtra(INTENT_SELECTED_INDEX, 0);
        messageHandler = getIntent().getStringExtra(INTENT_MESSAGE_HANDLER);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);
        loadingView = getIntent().getStringExtra(INTENT_LOADING_VIEW);

        renderToolBar();
        handleTitlePress(TabbedActivity.class.getSimpleName(), null, null);

        setupViewPager();
        setupBottomNav();
        setupTransitionOnEnter();
    }

    @Override
    public void renderComponent(TurbolinksRoute route, int tabIndex) {
        TabbedView tabbedView = getTabbedViewByIndex(tabIndex);
        tabbedView.renderComponent(getReactInstanceManager(), route);
    }

    @Override
    public void reloadVisitable() {
        getCurrentTabbedView().reload();
    }

    @Override
    public void evaluateJavaScript(String script, final Promise promise) {
        getCurrentTabbedView().evaluateJavaScript(script, promise);
    }

    @Override
    public void notifyTabItem(int tabIndex, boolean enabled) {
        BottomNavigationMenuView bottomNavMenu = (BottomNavigationMenuView) bottomNav.getChildAt(0);
        BottomNavigationItemView bottomNavItem = (BottomNavigationItemView) bottomNavMenu.getChildAt(tabIndex);
        if (enabled) {
            View badge = LayoutInflater.from(this).inflate(R.layout.badge_layout, bottomNavMenu, false);
            bottomNavItem.addView(badge);
        } else {
            View badge = bottomNavItem.findViewById(R.id.badge_view);
            bottomNavItem.removeView(badge);
        }
    }

    public void reloadSession() {
        restart();
    }

    public String getMessageHandler() {
        return messageHandler;
    }

    public String getUserAgent() {  return userAgent; }

    public String getLoadingView() { return loadingView; }

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
        setupTabBarStyle(tabBarStyle);
    }

    private void setupViewPager() {
        TurbolinksPagerAdapter adapter = new TurbolinksPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private void renderTabIcon(Menu menu, MenuItem menuItem, Bundle icon) {
        if (icon == null) return;
        String uri = icon.getString("uri");
        menuItem.setIcon(ImageLoader.loadImage(getApplicationContext(), uri));
    }

    private TabbedView getCurrentTabbedView() {
        return ((TurbolinksPagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem());
    }

    private TabbedView getTabbedViewByIndex(int index) {
        return ((TurbolinksPagerAdapter) viewPager.getAdapter()).getItem(index);
    }


    protected void setupTabBarStyle(TabBarStyle style) {
        if (style == null) return;
        if (style.getBarTintColor() != 0) { bottomNav.setBackgroundColor(style.getBarTintColor()); }
        if (style.getTintColor() != 0) {
            int[][] state = new int[][] { new int[] {-android.R.attr.state_checked}, new int[] {android.R.attr.state_checked} };
            int[] color = new int[] { Color.GRAY, style.getTintColor() };
            bottomNav.setItemTextColor(new ColorStateList(state, color));
            bottomNav.setItemIconTintList(new ColorStateList(state, color));
        }
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
