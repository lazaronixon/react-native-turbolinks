package com.reactlibrary.activities;

import android.annotation.SuppressLint;
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
import com.reactlibrary.R;
import com.reactlibrary.util.TurbolinksAction;

public class HelperActivity {

    private GenericActivity act;

    public HelperActivity(GenericActivity genericActivity) {
        this.act = genericActivity;
    }

    public boolean onSupportNavigateUp() {
        act.onBackPressed();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (act.getRoute().getActions() == null) return true;
        act.getMenuInflater().inflate(R.menu.turbolinks_menu, menu);
        for (Bundle bundle : act.getRoute().getActions()) {
            TurbolinksAction action = new TurbolinksAction(bundle);
            MenuItem menuItem = menu.add(Menu.NONE, action.getId(), Menu.NONE, action.getTitle());
            renderActionIcon(menu, menuItem, action.getIcon());
            if (action.getButton()) menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) return act.superOnOptionsItemSelected(item);
        act.getEventEmitter().emit("turbolinksActionPress", item.getItemId());
        return true;
    }

    public void renderToolBar(Toolbar toolbar) {
        act.setSupportActionBar(toolbar);
        act.getSupportActionBar().setDisplayHomeAsUpEnabled(!act.getInitialVisit());
        act.getSupportActionBar().setDisplayShowHomeEnabled(!act.getInitialVisit());
        act.getSupportActionBar().setTitle(null);
        if (act.getNavigationBarHidden() || act.getRoute().getModal()) act.getSupportActionBar().hide();
        act.handleTitlePress(toolbar);
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
