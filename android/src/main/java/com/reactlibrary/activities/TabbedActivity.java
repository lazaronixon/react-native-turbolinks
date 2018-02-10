package com.reactlibrary.activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.reactlibrary.R;
import com.reactlibrary.react.ReactAppCompatActivity;
import com.reactlibrary.util.TurbolinksRoute;

public class TabbedActivity extends ReactAppCompatActivity implements GenericActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        renderBottomNav((BottomNavigationView) findViewById(R.id.navigation));
    }

    private void renderBottomNav(BottomNavigationView bottomNav) {
        Menu menu = bottomNav.getMenu();
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "teste");
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "teste1");
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "teste2");
    }

    @Override
    public void renderTitle() {

    }

    @Override
    public void renderToolBar(Toolbar toolbar) {

    }

    @Override
    public void handleTitlePress(Toolbar toolbar) {

    }

    @Override
    public boolean superOnOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public Boolean getInitialVisit() {
        return null;
    }

    @Override
    public Boolean getNavigationBarHidden() {
        return null;
    }

    @Override
    public DeviceEventManagerModule.RCTDeviceEventEmitter getEventEmitter() {
        return null;
    }

    @Override
    public TurbolinksRoute getRoute() {
        return null;
    }
}
