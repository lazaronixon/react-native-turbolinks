package com.reactlibrary.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.util.TurbolinksRoute;

public interface GenericActivity {

    void renderTitle();
    void renderToolBar(Toolbar toolbar);
    void onBackPressed();
    void handleTitlePress(Toolbar toolbar);
    void setSupportActionBar(Toolbar toolbar);
    boolean superOnOptionsItemSelected(MenuItem item);
    Boolean getInitialVisit();
    Boolean getNavigationBarHidden();
    RCTDeviceEventEmitter getEventEmitter();
    TurbolinksRoute getRoute();
    MenuInflater getMenuInflater();
    ActionBar getSupportActionBar();
}
