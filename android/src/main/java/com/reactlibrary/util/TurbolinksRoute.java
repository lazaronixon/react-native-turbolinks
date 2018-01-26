package com.reactlibrary.util;

import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;

public class TurbolinksRoute {

    private String url;
    private String component;
    private String action;
    private Boolean modal;
    private Bundle passProps;

    public TurbolinksRoute(ReadableMap rp) {
        ReadableMap props = rp.hasKey("passProps") ? rp.getMap("passProps") : null;
        this.url = rp.hasKey("url") ? rp.getString("url") : null;
        this.component = rp.hasKey("component") ? rp.getString("component") : null;
        this.action = rp.hasKey("action") ? rp.getString("action") : "advance";
        this.modal = rp.hasKey("modal") ? rp.getBoolean("modal") : false;
        this.passProps = props != null ? Arguments.toBundle(props) : null;
    }

    public String getUrl() {
        return url;
    }

    public String getComponent() {
        return component;
    }

    public String getAction() {
        return action;
    }

    public Boolean getModal() {
        return modal;
    }

    public Bundle getPassProps() {
        return passProps;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
