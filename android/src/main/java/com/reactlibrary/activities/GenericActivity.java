package com.reactlibrary.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.react.modules.core.DeviceEventManagerModule;

public interface GenericActivity {

    void renderTitle();
    void renderToolBar();
    void renderActionIcon(Menu menu, MenuItem menuItem, Bundle icon);
    DeviceEventManagerModule.RCTDeviceEventEmitter getEventEmitter();
}
