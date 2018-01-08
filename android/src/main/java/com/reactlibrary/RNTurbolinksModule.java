
package com.reactlibrary;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

import java.util.HashMap;
import java.util.Map;

public class RNTurbolinksModule extends ReactContextBaseJavaModule implements TurbolinksAdapter {

    private TurbolinksSession session;

    private TurbolinksSession getSession() {
        if (session == null) {
            session = TurbolinksSession.getDefault(getReactApplicationContext())
                    .activity(getCurrentActivity())
                    .adapter(this);
        }
        return session;
    }

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
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
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap();
        constants.put("ErrorCode", getErrorCodeConstants());
        constants.put("Action", getActionConstants());
        return constants;
    }

    private Map<String, Object> getErrorCodeConstants() {
        final Map<String, Object> errorCodeConstants = new HashMap();
        errorCodeConstants.put("httpFailure", 0);
        errorCodeConstants.put("networkFailure", 1);
        return errorCodeConstants;
    }

    private Map<String, Object> getActionConstants() {
        final Map<String, Object> actionConstants = new HashMap();
        actionConstants.put("advance", "advance");
        actionConstants.put("replace", "replace");
        actionConstants.put("restore", "restore");
        return actionConstants;
    }

    private UIManagerModule getNativeModule() {
        return getReactApplicationContext().getNativeModule(UIManagerModule.class);
    }

}