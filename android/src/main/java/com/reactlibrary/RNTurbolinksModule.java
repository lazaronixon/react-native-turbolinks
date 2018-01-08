
package com.reactlibrary;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RNTurbolinksModule extends ReactContextBaseJavaModule implements TurbolinksAdapter {

    private TurbolinksSession session;

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private TurbolinksSession getSession() {
        if (session == null) {
            session = TurbolinksSession.getDefault(getReactApplicationContext())
                    .activity(getCurrentActivity())
                    .adapter(this);
        }
        return session;
    }

    @Override
    public String getName() {
        return "RNTurbolinksModule";
    }

    @ReactMethod
    public void replaceWith(ReadableMap routeParam) {
    }

    @ReactMethod
    public void visit(final int reactTag, final ReadableMap routeParam) {
        getNativeModule().addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                String url = routeParam.getString("url");
                TurbolinksView turbolinksView = (TurbolinksView) nvhm.resolveView(reactTag);
                getSession().view(turbolinksView).visit(url);
            }
        });
    }

    @ReactMethod
    public void reloadVisitable() {
    }

    @ReactMethod
    public void reloadSession() {
    }

    @ReactMethod
    public void dismiss() {
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
            sendEvent("onVisit", params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map getConstants() {
        return MapBuilder.of(
                "ErrorCode", MapBuilder.of("httpFailure", 0, "networkFailure", 1),
                "Action", MapBuilder.of("advance", "advance", "replace", "replace", "restore", "restore")
        );
    }

    private UIManagerModule getNativeModule() {
        return getReactApplicationContext().getNativeModule(UIManagerModule.class);
    }

    private void sendEvent(String eventName, WritableMap params) {
        getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class).emit(eventName, params);
    }

}