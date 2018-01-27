package com.reactlibrary.util;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;

public class TurbolinksRoute {

    private static final String INTENT_URL = "intentUrl";
    private static final String INTENT_COMPONENT = "intentComponent";
    private static final String INTENT_PROPS = "intentProps";
    private static final String INTENT_MODAL = "intentModal";
    private static final String INTENT_TITLE = "intentTitle";
    private static final String INTENT_SUBTITLE = "intentSubTitle";

    private String url;
    private String component;
    private String action;
    private Boolean modal;
    private Bundle passProps;
    private String title;
    private String subtitle;

    public TurbolinksRoute(ReadableMap rp) {
        ReadableMap props = rp.hasKey("passProps") ? rp.getMap("passProps") : null;
        this.url = rp.hasKey("url") ? rp.getString("url") : null;
        this.component = rp.hasKey("component") ? rp.getString("component") : null;
        this.action = rp.hasKey("action") ? rp.getString("action") : "advance";
        this.modal = rp.hasKey("modal") ? rp.getBoolean("modal") : false;
        this.passProps = props != null ? Arguments.toBundle(props) : null;
        this.title = rp.hasKey("title") ? rp.getString("title") : null;
        this.subtitle = rp.hasKey("subtitle") ? rp.getString("subtitle") : null;
    }

    public TurbolinksRoute(Intent intent) {
        this.url = intent.getStringExtra(INTENT_URL);
        this.component = intent.getStringExtra(INTENT_COMPONENT);
        this.passProps = intent.getBundleExtra(INTENT_PROPS);
        this.title = intent.getStringExtra(INTENT_TITLE);
        this.subtitle = intent.getStringExtra(INTENT_SUBTITLE);
        this.modal = intent.getBooleanExtra(INTENT_MODAL, false);
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

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getModal() {
        return modal;
    }

    public Bundle getPassProps() {
        return passProps;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() { return subtitle; }

}
