package com.reactlibrary;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

public class NativeActivity extends ReactActivity implements DefaultHardwareBackBtnHandler {

    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_PROPS = "intentProps";

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    private String component;
    private Bundle props;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component = getIntent().getStringExtra(INTENT_COMPONENT);
        props = getIntent().getBundleExtra(INTENT_PROPS);

        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = getReactInstanceManager();

        mReactRootView.startReactApplication(mReactInstanceManager, component, props);
        setContentView(mReactRootView);
    }

}
