package com.reactlibrary.util;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;

public class TurbolinksRoute {

    public static final String INTENT_URL = "intentUrl";
    public static final String INTENT_COMPONENT = "intentComponent";
    public static final String INTENT_PROPS = "intentProps";
    public static final String INTENT_MODAL = "intentModal";
    public static final String INTENT_TITLE = "intentTitle";
    public static final String INTENT_SUBTITLE = "intentSubtitle";
    public static final String INTENT_ACTIONS = "intentActions";

    public static final String ACTION_ADVANCE = "advance";
    public static final String ACTION_REPLACE = "replace";

    private String url;
    private String component;
    private String action;
    private Boolean modal;
    private Bundle passProps;
    private String title;
    private String subtitle;
    private ArrayList<Bundle> actions;

    public TurbolinksRoute(ReadableMap rp) {
        ReadableMap props = rp.hasKey("passProps") ? rp.getMap("passProps") : null;
        ReadableArray actions = rp.hasKey("actions") ? rp.getArray("actions") : null;
        this.url = rp.hasKey("url") ? rp.getString("url") : null;
        this.component = rp.hasKey("component") ? rp.getString("component") : null;
        this.action = rp.hasKey("action") ? rp.getString("action") : ACTION_ADVANCE;
        this.modal = rp.hasKey("modal") ? rp.getBoolean("modal") : false;
        this.passProps = props != null ? Arguments.toBundle(props) : null;
        this.title = rp.hasKey("title") ? rp.getString("title") : null;
        this.subtitle = rp.hasKey("subtitle") ? rp.getString("subtitle") : null;
        this.actions = rp.hasKey("actions") ? Arguments.toList(actions) : null;
    }

    public TurbolinksRoute(Intent intent) {
        this.url = intent.getStringExtra(INTENT_URL);
        this.component = intent.getStringExtra(INTENT_COMPONENT);
        this.modal = intent.getBooleanExtra(INTENT_MODAL, false);
        this.passProps = intent.getBundleExtra(INTENT_PROPS);
        this.title = intent.getStringExtra(INTENT_TITLE);
        this.subtitle = intent.getStringExtra(INTENT_SUBTITLE);
        this.actions = intent.getParcelableArrayListExtra(INTENT_ACTIONS);
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Bundle> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Bundle> actions) {
        this.actions = actions;
    }
}
