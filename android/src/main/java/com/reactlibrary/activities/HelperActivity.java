package com.reactlibrary.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.reactlibrary.util.TurbolinksAction;

import java.util.ArrayList;

public class HelperActivity {

    private GenericActivity act;

    public HelperActivity(GenericActivity genericActivity) {
        this.act = genericActivity;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        ArrayList<Bundle> actions = act.getRoute().getActions();
        if (actions != null) {
            for (int i = 0; i < actions.size(); i++) {
                TurbolinksAction action = new TurbolinksAction(actions.get(i));
                MenuItem menuItem = menu.add(Menu.NONE, action.getId(), i, action.getTitle());
                renderActionIcon(menu, menuItem, action.getIcon());
                if (action.getButton()) menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            act.onBackPressed();
            return false;
        } else {
            act.getEventEmitter().emit("turbolinksActionPress", item.getItemId());
            return true;
        }
    }

    public void renderToolBar(Toolbar toolbar) {
        act.setSupportActionBar(toolbar);
        act.getSupportActionBar().setDisplayHomeAsUpEnabled(!act.getInitialVisit());
        act.getSupportActionBar().setTitle(null);
        if (act.getNavigationBarHidden() || act.getRoute().getModal()) {
            act.getSupportActionBar().hide();
        }
        act.handleTitlePress(toolbar);
    }

    public void renderTitle() {
        act.getSupportActionBar().setTitle(act.getRoute().getTitle());
        act.getSupportActionBar().setSubtitle(act.getRoute().getSubtitle());
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
