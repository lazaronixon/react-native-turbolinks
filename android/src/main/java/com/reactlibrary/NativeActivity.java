package com.reactlibrary;

import android.os.Bundle;

import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

public class NativeActivity extends ReactActivity implements DefaultHardwareBackBtnHandler {

    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_PROPS = "intentProps";
    private static final String INTENT_MODAL = "intentModal";

    private Boolean modal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modal = getIntent().getBooleanExtra(INTENT_MODAL, false);

        String component = getIntent().getStringExtra(INTENT_COMPONENT);
        Bundle props = getIntent().getBundleExtra(INTENT_PROPS);

        ReactRootView mReactRootView = new ReactRootView(this);
        ReactInstanceManager mReactInstanceManager = getReactInstanceManager();

        mReactRootView.startReactApplication(mReactInstanceManager, component, props);
        setContentView(mReactRootView);
    }

    @Override
    public void onBackPressed() {
        if (!modal) super.onBackPressed();
    }

}
