package com.reactlibrary;


import android.content.Context;
import android.util.Log;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.net.MalformedURLException;
import java.net.URL;

public class RNTurbolinksView extends TurbolinksView implements TurbolinksAdapter {

    private TurbolinksSession session;

    public RNTurbolinksView(Context context) {
        super(context);
    }

    public TurbolinksSession getSession() {
        if (session != null) { return session; }
        return TurbolinksSession.getDefault(this.getContext()).adapter(this).view(this);
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
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), eventName, params);
    }
}
