
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

public class RNTurbolinksModule extends ReactContextBaseJavaModule {

    public RNTurbolinksModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() { return "RNTurbolinksModule"; }

    @ReactMethod
    public void teste() {
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

}