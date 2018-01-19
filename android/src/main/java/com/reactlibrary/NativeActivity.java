package com.reactlibrary;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

public class NativeActivity extends ReactActivity implements DefaultHardwareBackBtnHandler {

    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_PROPS = "intentProps";
    private static final String INTENT_HANDLE_BACK = "intentHandleBack";

    private Boolean handleBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleBack = getIntent().getBooleanExtra(INTENT_HANDLE_BACK, true);

        String component = getIntent().getStringExtra(INTENT_COMPONENT);
        Bundle props = getIntent().getBundleExtra(INTENT_PROPS);

        ReactRootView mReactRootView = new ReactRootView(this);
        ReactInstanceManager mReactInstanceManager = getReactInstanceManager();

        mReactRootView.startReactApplication(mReactInstanceManager, component, props);
        setContentView(mReactRootView);
    }

    @Override
    public void onBackPressed() {
        if (handleBack) super.onBackPressed();
    }

}
