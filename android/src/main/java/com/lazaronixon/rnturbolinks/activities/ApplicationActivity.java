package com.lazaronixon.rnturbolinks.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.lazaronixon.rnturbolinks.util.ImageLoader;
import com.lazaronixon.rnturbolinks.util.NavBarStyle;
import com.lazaronixon.rnturbolinks.util.TurbolinksAction;
import com.lazaronixon.rnturbolinks.util.TurbolinksRoute;

import java.util.ArrayList;

import static com.lazaronixon.rnturbolinks.RNTurbolinksModule.INTENT_INITIAL;

public abstract class ApplicationActivity extends ReactActivity {

    private static final String TURBOLINKS_ACTION_PRESS = "turbolinksActionPress";
    private static final String TURBOLINKS_TITLE_PRESS = "turbolinksTitlePress";

    protected Toolbar toolBar;
    protected TurbolinksRoute route;
    protected NavBarStyle navBarStyle;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode == KeyEvent.KEYCODE_VOLUME_UP ? KeyEvent.KEYCODE_MENU : keyCode, event);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (route.getActions() != null) {
            ArrayList<Bundle> actions = route.getActions();
            for (int i = 0; i < actions.size(); i++) {
                TurbolinksAction action = new TurbolinksAction(actions.get(i));
                MenuItem menuItem = menu.add(Menu.NONE, action.getId(), i, action.getTitle());
                renderActionIcon(menu, menuItem, action.getIcon());
                if (action.getButton()) menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        } else {
            getEventEmitter().emit(TURBOLINKS_ACTION_PRESS, item.getItemId());
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isInitial() || isModal()) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    public void setActions(ArrayList<Bundle> actions) { route.setActions(actions); }

    public void renderTitle(String title, String subtitle) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
    }

    public abstract void renderComponent(TurbolinksRoute route);

    public abstract void reloadVisitable();

    public abstract void reloadSession();

    public abstract void injectJavaScript(String script);

    protected RCTDeviceEventEmitter getEventEmitter() {
        return getReactInstanceManager().getCurrentReactContext().getJSModule(RCTDeviceEventEmitter.class);
    }

    protected void renderToolBar() {
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(!isInitial());
        getSupportActionBar().setTitle(route.getTitle());
        getSupportActionBar().setSubtitle(route.getSubtitle());
        setActionBarNavIcon(route.getNavIcon(), getSupportActionBar());

        setupNavBarStyle(navBarStyle);

        renderTitleImage(route.getTitleImage());

        if (route.getNavBarHidden() || route.getModal()) getSupportActionBar().hide();
    }

    protected void handleTitlePress(final String component, final String url, final String path) {
        toolBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WritableMap params = Arguments.createMap();
                params.putString("component", component);
                params.putString("url", url);
                params.putString("path", path);
                getEventEmitter().emit(TURBOLINKS_TITLE_PRESS, params);
            }
        });
    }

    protected boolean isInitial() { return getIntent().getBooleanExtra(INTENT_INITIAL, true); }

    private void renderTitleImage(Bundle image) {
        if (image != null) toolBar.addView(ImageLoader.imageViewFor(image, getApplicationContext()));
    }

    @SuppressLint("RestrictedApi")
    private void renderActionIcon(Menu menu, MenuItem menuItem, Bundle icon) {
        if (icon != null && menu instanceof MenuBuilder) ((MenuBuilder) menu).setOptionalIconsVisible(true);
        if (icon != null) setMenuItemIcon(icon, menuItem);
    }

    private void setupNavBarStyle(NavBarStyle style) {
        if (style != null) {
            if (style.getBarTintColor() != 0) toolBar.setBackgroundColor(style.getBarTintColor());
            if (style.getTitleTextColor() != 0) toolBar.setTitleTextColor(style.getTitleTextColor());
            if (style.getSubtitleTextColor() != 0) toolBar.setSubtitleTextColor(style.getSubtitleTextColor());
            if (style.getMenuIcon() != null) setToolbarOverFlowIcon(style.getMenuIcon(), toolBar);
        }
    }

    private boolean isModal() {
        return route.getModal();
    }

    private void setToolbarOverFlowIcon(Bundle icon, final Toolbar toolBar) {
        ImageLoader.bitmapFor(icon, getApplicationContext(), new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                toolBar.setOverflowIcon(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Log.e(ApplicationActivity.class.getName(), "Invalid bitmap: ", dataSource.getFailureCause());
            }
        });
    }

    private void setActionBarNavIcon(Bundle icon, final ActionBar actionBar) {
        if (icon != null) {
            ImageLoader.bitmapFor(icon, getApplicationContext(), new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    actionBar.setIcon(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {
                    Log.e(ApplicationActivity.class.getName(), "Invalid bitmap: ", dataSource.getFailureCause());
                }
            });
        }
    }

    private void setMenuItemIcon(Bundle icon, final MenuItem menuItem) {
        ImageLoader.bitmapFor(icon, getApplicationContext(), new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                menuItem.setIcon(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Log.e(ApplicationActivity.class.getName(), "Invalid bitmap: ", dataSource.getFailureCause());
            }
        });
    }

}
