package com.reactlibrary.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.reactlibrary.util.TurbolinksRoute;

public interface GenericActivity {

    void renderTitle();

    void onBackPressed();

    void handleTitlePress(Toolbar toolbar);

    boolean isTaskRoot();

    boolean getNavigationBarHidden();

    RCTDeviceEventEmitter getEventEmitter();

    ReactInstanceManager getManager();

    TurbolinksRoute getRoute();

    ActionBar getSupportActionBar();

    void setSupportActionBar(Toolbar toolbar);

    void renderComponent(TurbolinksRoute route, int tabIndex);

    void reload();

}
