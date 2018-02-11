package com.reactlibrary.activities;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactlibrary.util.TurbolinksRoute;

public class HelperNativeActivity extends HelperActivity {

    private GenericNativeActivity act;

    public HelperNativeActivity(GenericNativeActivity genericNativeActivity) {
        super(genericNativeActivity);
        this.act = genericNativeActivity;
    }

    public void visitComponent(ReactRootView view, ReactInstanceManager manager, TurbolinksRoute route) {
        view.startReactApplication(manager, route.getComponent(), route.getPassProps());
    }

    public void onBackPressed() {
        if (act.getInitialVisit()) {
            act.moveTaskToBack(true);
        } else {
            if (!act.getRoute().getModal()) act.onSuperBackPressed();
        }
    }

    public void handleTitlePress(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WritableMap params = Arguments.createMap();
                params.putString("component", act.getRoute().getComponent());
                params.putString("url", null);
                params.putString("path", null);
                act.getEventEmitter().emit("turbolinksTitlePress", params);
            }
        });
    }

}
