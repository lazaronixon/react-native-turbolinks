package com.reactlibrary;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

public class NativeActivity extends ReactActivity implements DefaultHardwareBackBtnHandler {

    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_PROPS = "intentProps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String component = getIntent().getStringExtra(INTENT_COMPONENT);
        Bundle props = getIntent().getBundleExtra(INTENT_PROPS);

        ReactRootView mReactRootView = new ReactRootView(this);
        ReactInstanceManager mReactInstanceManager = getReactInstanceManager();

        mReactRootView.startReactApplication(mReactInstanceManager, component, props);
        setContentView(mReactRootView);
    }

}
