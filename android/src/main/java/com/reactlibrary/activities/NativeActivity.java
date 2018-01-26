package com.reactlibrary.activities;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;

public class NativeActivity extends ReactActivity {

    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_INITIAL_VISIT = "intentInitialVisit";
    private static final String INTENT_PROPS = "intentProps";
    private static final String INTENT_MODAL = "intentModal";

    private Boolean initialVisit;
    private Boolean modal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialVisit = getIntent().getBooleanExtra(INTENT_INITIAL_VISIT, true);
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
        if (initialVisit) {
            moveTaskToBack(true);
        } else {
            if (!modal) super.onBackPressed();
        }
    }

}
