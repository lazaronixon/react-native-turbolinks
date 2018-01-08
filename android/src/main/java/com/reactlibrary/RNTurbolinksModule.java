
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

import java.util.Map;

public class RNTurbolinksModule extends ReactContextBaseJavaModule {

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() { return "RNTurbolinksModule"; }

    @ReactMethod
    public void replaceWith(ReadableMap routeParam) {
    }

    @ReactMethod
    public void visit(final int reactTag, final ReadableMap routeParam) {
        getNativeModule().addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nvhm) {
                String url = routeParam.getString("url");
                RNTurbolinksView turbolinksView = (RNTurbolinksView) nvhm.resolveView(reactTag);
                turbolinksView.getSession().activity(getCurrentActivity()).visit(url);
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
    public Map getConstants() {
        return MapBuilder.of(
                "ErrorCode", MapBuilder.of("httpFailure", 0, "networkFailure", 1),
                "Action", MapBuilder.of("advance", "advance", "replace", "replace", "restore", "restore")
        );
    }

    private UIManagerModule getNativeModule() {
        return getReactApplicationContext().getNativeModule(UIManagerModule.class);
    }

}