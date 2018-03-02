package com.reactlibrary.util;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.ReactConstants;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TurbolinksRoute implements Parcelable {

    public static final String ACTION_ADVANCE = "advance";
    public static final String ACTION_REPLACE = "replace";

    private String url;
    private String component;
    private String action;
    private boolean modal;
    private Bundle passProps;
    private String title;
    private String subtitle;
    private ArrayList<Bundle> actions;
    private String tabTitle;
    private Bundle tabIcon;

    public TurbolinksRoute(ReadableMap rp) {
        ReadableMap props = rp.hasKey("passProps") ? rp.getMap("passProps") : null;
        ReadableArray actions = rp.hasKey("actions") ? rp.getArray("actions") : null;
        ReadableMap tabIcon = rp.hasKey("tabIcon") ? rp.getMap("tabIcon") : null;
        this.url = rp.hasKey("url") ? rp.getString("url") : null;
        this.component = rp.hasKey("component") ? rp.getString("component") : null;
        this.action = rp.hasKey("action") ? rp.getString("action") : ACTION_ADVANCE;
        this.modal = rp.hasKey("modal") ? rp.getBoolean("modal") : false;
        this.passProps = props != null ? Arguments.toBundle(props) : null;
        this.title = rp.hasKey("title") ? rp.getString("title") : null;
        this.subtitle = rp.hasKey("subtitle") ? rp.getString("subtitle") : null;
        this.actions = rp.hasKey("actions") ? Arguments.toList(actions) : null;
        this.tabTitle = rp.hasKey("tabTitle") ? rp.getString("tabTitle") : null;
        this.tabIcon = rp.hasKey("tabIcon") ? Arguments.toBundle(tabIcon) : null;
    }

    public TurbolinksRoute(Bundle bundle) {
        this.url = bundle.getString("url");
        this.component = bundle.getString("component");
        this.action = bundle.getString("action");
        this.modal = bundle.getBoolean("modal");
        this.passProps = bundle.getBundle("passProps");
        this.title = bundle.getString("title");
        this.subtitle = bundle.getString("subtitle");
        this.actions = bundle.getParcelableArrayList("actions");
        this.tabTitle = bundle.getString("tabTitle");
        this.tabIcon = bundle.getBundle("tabIcon");
    }

    protected TurbolinksRoute(Parcel in) {
        url = in.readString();
        component = in.readString();
        action = in.readString();
        modal = in.readByte() != 0;
        passProps = in.readBundle();
        title = in.readString();
        subtitle = in.readString();
        actions = in.createTypedArrayList(Bundle.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(component);
        dest.writeString(action);
        dest.writeByte((byte) (modal ? 1 : 0));
        dest.writeBundle(passProps);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeTypedList(actions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TurbolinksRoute> CREATOR = new Creator<TurbolinksRoute>() {
        @Override
        public TurbolinksRoute createFromParcel(Parcel in) {
            return new TurbolinksRoute(in);
        }

        @Override
        public TurbolinksRoute[] newArray(int size) {
            return new TurbolinksRoute[size];
        }
    };

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

    public boolean getModal() {
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

    public String getTabTitle() {
        return tabTitle;
    }

    public Bundle getTabIcon() {
        return tabIcon;
    }

}
