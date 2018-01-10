package com.reactlibrary;


import android.content.Context;
import android.util.AttributeSet;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.net.MalformedURLException;
import java.net.URL;

public class RNTurbolinksView extends TurbolinksView implements TurbolinksAdapter {

    public RNTurbolinksView(Context context) {
        super(context);
    }

    public RNTurbolinksView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPageFinished() {

    }

    @Override
    public void onReceivedError(int errorCode) {

    }

    @Override
    public void pageInvalidated() {

    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {

    }

    @Override
    public void visitCompleted() {

    }

    @Override
    public void visitProposedToLocationWithAction(String location, String action) {
        try {
            WritableMap dataParams = Arguments.createMap();
            URL urlLocation = new URL(location);
            dataParams.putString("url", urlLocation.toString());
            dataParams.putString("path", urlLocation.getPath());
            dataParams.putString("action", action);
            WritableMap params = Arguments.createMap();
            params.putMap("data", dataParams);
            sendEvent("topVisit", params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(String eventName, WritableMap params) {
        CustomActivity customActivity = (CustomActivity) getContext();
        customActivity.getReactContext().getJSModule(RCTEventEmitter.class).receiveEvent(getId(), eventName, params);
    }
}
