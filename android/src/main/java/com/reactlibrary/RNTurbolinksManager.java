package com.reactlibrary;


import android.view.View;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;

public class RNTurbolinksManager extends SimpleViewManager<View> {

    @Override
    public String getName() { return "RNTurbolinks"; }

    @Override
    protected View createViewInstance(ThemedReactContext reactContext) {
        return new View(reactContext);
    }

    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of("topVisit", MapBuilder.of("registrationName", "onVisit"));
    }
}
