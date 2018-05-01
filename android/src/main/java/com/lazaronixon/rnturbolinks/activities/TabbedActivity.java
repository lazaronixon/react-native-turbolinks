package com.lazaronixon.rnturbolinks.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.facebook.react.bridge.Promise;
import com.lazaronixon.rnturbolinks.R;
import com.lazaronixon.rnturbolinks.util.ImageLoader;
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
    private AHBottomNavigation bottomNav;

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
    public void notifyTabItem(String value, int tabIndex) {
        bottomNav.setNotification(value, tabIndex);
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
        for (int i = 0; i < routes.size(); i++) {
            TurbolinksRoute route = new TurbolinksRoute(routes.get(i));
            bottomNav.addItem(new AHBottomNavigationItem(route.getTabTitle(), getTabIcon(route.getTabIcon())));
            bottomNav.setNotification(route.getTabBadge(), i);
        }

        bottomNav.setOnTabSelectedListener(
                new AHBottomNavigation.OnTabSelectedListener() {
                    public boolean onTabSelected(int position, boolean wasSelected) {
                        viewPager.setCurrentItem(position, false);
                        return true;
                    }
                }
        );

        if (tabBarStyle != null) {
            if (tabBarStyle.getBarTintColor() != 0) { bottomNav.setDefaultBackgroundColor(tabBarStyle.getBarTintColor()); }
            if (tabBarStyle.getTintColor() != 0) { bottomNav.setAccentColor(tabBarStyle.getTintColor()); }
        }

        bottomNav.setForceTint(true);
        bottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNav.setCurrentItem(selectedIndex);
    }

    private void setupViewPager() {
        TurbolinksPagerAdapter adapter = new TurbolinksPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private Drawable getTabIcon(Bundle icon) {
        if (icon == null) return new ColorDrawable(Color.TRANSPARENT);
        String uri = icon.getString("uri");
        return ImageLoader.loadImage(getApplicationContext(), uri);
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
