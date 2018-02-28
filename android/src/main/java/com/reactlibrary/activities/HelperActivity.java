package com.reactlibrary.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.util.TurbolinksAction;

import java.util.ArrayList;

public class HelperActivity {

    public static final String TURBOLINKS_ACTION_PRESS = "turbolinksActionPress";
    public static final String TURBOLINKS_TITLE_PRESS = "turbolinksTitlePress";

    private GenericActivity act;

    public HelperActivity(GenericActivity genericActivity) {
        this.act = genericActivity;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (act.getRoute().getActions() == null) return true;
        ArrayList<Bundle> actions = act.getRoute().getActions();
        for (int i = 0; i < actions.size(); i++) {
            TurbolinksAction action = new TurbolinksAction(actions.get(i));
            MenuItem menuItem = menu.add(Menu.NONE, action.getId(), i, action.getTitle());
            renderActionIcon(menu, menuItem, action.getIcon());
            if (action.getButton()) menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            act.onBackPressed();
            return false;
        } else {
            act.getEventEmitter().emit(TURBOLINKS_ACTION_PRESS, item.getItemId());
            return true;
        }
    }

    public void renderToolBar(Toolbar toolbar) {
        act.setSupportActionBar(toolbar);
        act.getSupportActionBar().setDisplayHomeAsUpEnabled(!act.isTaskRoot());
        act.getSupportActionBar().setDisplayShowHomeEnabled(!act.isTaskRoot());
        act.getSupportActionBar().setTitle(null);
        act.handleTitlePress(toolbar);
        if (act.getNavigationBarHidden() || act.getRoute().getModal()) {
            act.getSupportActionBar().hide();
        }
    }

    public void renderTitle() {
        act.getSupportActionBar().setTitle(act.getRoute().getTitle());
        act.getSupportActionBar().setSubtitle(act.getRoute().getSubtitle());
    }

    public RCTDeviceEventEmitter getEventEmitter() {
        return act.getManager().getCurrentReactContext().getJSModule(RCTDeviceEventEmitter.class);
    }

    public void handleTitlePress(Toolbar toolbar, final String component, final String url, final String path) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WritableMap params = Arguments.createMap();
                params.putString("component", component);
                params.putString("url", url);
                params.putString("path", path);
                getEventEmitter().emit(TURBOLINKS_TITLE_PRESS, params);
            }
        });
    }

    public void backToHomeScreen(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    private void renderActionIcon(Menu menu, MenuItem menuItem, Bundle icon) {
        if (icon == null) return;
        if (menu instanceof MenuBuilder) ((MenuBuilder) menu).setOptionalIconsVisible(true);
        Uri uri = Uri.parse(icon.getString("uri"));
        Drawable drawableIcon = Drawable.createFromPath(uri.getPath());
        menuItem.setIcon(drawableIcon);
    }
}
