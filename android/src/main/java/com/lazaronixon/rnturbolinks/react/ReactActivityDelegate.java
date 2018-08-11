package com.lazaronixon.rnturbolinks.react;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionListener;

import javax.annotation.Nullable;

public class ReactActivityDelegate {

    private final @Nullable Activity mActivity;

    private @Nullable PermissionListener mPermissionListener;
    private @Nullable Callback mPermissionsCallback;

    public ReactActivityDelegate(Activity activity) {
        mActivity = activity;
    }

    protected ReactNativeHost getReactNativeHost() {
        return ((ReactApplication) getPlainActivity().getApplication()).getReactNativeHost();
    }

    public ReactInstanceManager getReactInstanceManager() {
        return getReactNativeHost().getReactInstanceManager();
    }

    protected void onPause() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostPause(getPlainActivity());
        }
    }

    protected void onResume() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostResume(
                    getPlainActivity(),
                    (DefaultHardwareBackBtnHandler) getPlainActivity());
        }

        if (mPermissionsCallback != null) {
            mPermissionsCallback.invoke();
            mPermissionsCallback = null;
        }
    }

    protected void onDestroy() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostDestroy(getPlainActivity());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager()
                    .onActivityResult(getPlainActivity(), requestCode, resultCode, data);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance()
                && getReactNativeHost().getUseDeveloperSupport()
                && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            event.startTracking();
            return true;
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance() && getReactNativeHost().getUseDeveloperSupport()) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
                return true;
            }
        }
        return false;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance()
                && getReactNativeHost().getUseDeveloperSupport()
                && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
            return true;
        }
        return false;
    }

    public boolean onBackPressed() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onBackPressed();
            return true;
        }
        return false;
    }

    public boolean onNewIntent(Intent intent) {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onNewIntent(intent);
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(
            String[] permissions,
            int requestCode,
            PermissionListener listener) {
        mPermissionListener = listener;
        getPlainActivity().requestPermissions(permissions, requestCode);
    }

    public void onRequestPermissionsResult(
            final int requestCode,
            final String[] permissions,
            final int[] grantResults) {
        mPermissionsCallback = new Callback() {
            @Override
            public void invoke(Object... args) {
                if (mPermissionListener != null && mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                    mPermissionListener = null;
                }
            }
        };
    }

    private Context getContext() {
        return mActivity;
    }

    private Activity getPlainActivity() {
        return ((Activity) getContext());
    }
}
