package com.reactlibrary;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

public class NativeActivity extends ReactActivity implements DefaultHardwareBackBtnHandler {

    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_PROPS = "intentProps";
    private static final String INTENT_ACTION = "intentAction";

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    private String action;
    private String component;
    private Bundle props;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component = getIntent().getStringExtra(INTENT_COMPONENT);
        action = getIntent().getStringExtra(INTENT_ACTION);
        props = getIntent().getBundleExtra(INTENT_PROPS);

        handleAnimation(true);
        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = getReactInstanceManager();

        mReactRootView.startReactApplication(mReactInstanceManager, component, props);
        setContentView(mReactRootView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handleAnimation(false);
    }

    private void handleAnimation(Boolean isForward) {
        if (action.equals("advance")) {
            if (isForward) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else if (action.equals("replace")) {
            overridePendingTransition(R.anim.nothing, R.anim.nothing);
        }
    }

}
