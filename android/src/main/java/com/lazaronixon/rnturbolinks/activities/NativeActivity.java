package com.lazaronixon.rnturbolinks.activities;

import android.os.Bundle;
import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.lazaronixon.rnturbolinks.R;
import com.lazaronixon.rnturbolinks.util.TurbolinksRoute;

public class NativeActivity extends ApplicationActivity {

    private ReactRootView rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        setupToolBar();

        rootView = findViewById(R.id.react_root_view);
        rootView.startReactApplication(getReactInstanceManager(), route.getComponent(), route.getPassProps());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rootView.unmountReactApplication();
        rootView = null;
    }

    @Override
    public void reloadSession() { TurbolinksSession.getDefault(this).resetToColdBoot(); }

    @Override
    public void renderComponent(TurbolinksRoute route) {}

    @Override
    public void reloadVisitable() {}

    @Override
    public void injectJavaScript(String script) {}

    @Override
    public void handleActionPress(int itemId) {
        WritableMap params = Arguments.createMap();
        params.putString("component", route.getComponent());
        params.putString("url", null);
        params.putString("path", null);
        params.putInt("actionId", itemId);
        getEventEmitter().emit(TURBOLINKS_ACTION_PRESS, params);
    }

}
