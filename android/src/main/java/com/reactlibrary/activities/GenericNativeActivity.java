package com.reactlibrary.activities;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.reactlibrary.util.TurbolinksRoute;

public interface GenericNativeActivity extends GenericActivity {

    void visitComponent(ReactRootView view, ReactInstanceManager manager, TurbolinksRoute route);

}
