package com.reactlibrary;


import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class RNTurbolinksManager extends SimpleViewManager<TurbolinksView> {

    public static final String REACT_CLASS = "RNTurbolinks";

    @Override
    public String getName() { return REACT_CLASS; }

    @Override
    protected TurbolinksView createViewInstance(ThemedReactContext reactContext) {
        return new TurbolinksView(reactContext);
    }
}
