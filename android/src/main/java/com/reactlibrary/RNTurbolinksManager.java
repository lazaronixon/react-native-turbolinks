package com.reactlibrary;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class RNTurbolinksManager extends SimpleViewManager<RNTurbolinksView> {

    @Override
    public String getName() { return "RNTurbolinks"; }

    @Override
    protected RNTurbolinksView createViewInstance(ThemedReactContext reactContext) {
        return new RNTurbolinksView(reactContext);
    }

    @ReactProp(name = "userAgent")
    public void setUserAgent(RNTurbolinksView view, String userAgent) {
        view.setUserAgent(userAgent);
    }

    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of("topVisit", MapBuilder.of("registrationName", "onVisit"));
    }
}
