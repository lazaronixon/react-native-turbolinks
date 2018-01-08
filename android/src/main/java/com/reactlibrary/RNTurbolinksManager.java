package com.reactlibrary;


import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;

public class RNTurbolinksManager extends SimpleViewManager<RNTurbolinksView> {

    @Override
    public String getName() { return "RNTurbolinks"; }

    @Override
    protected RNTurbolinksView createViewInstance(ThemedReactContext reactContext) {
        return new RNTurbolinksView(reactContext);
    }

    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of("topVisit", MapBuilder.of("registrationName", "onVisit"));
    }
}
