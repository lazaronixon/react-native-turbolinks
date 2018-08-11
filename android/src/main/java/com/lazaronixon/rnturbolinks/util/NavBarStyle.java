package com.lazaronixon.rnturbolinks.util;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;

public class NavBarStyle implements Parcelable {

    private static final String TITLE_TEXT_COLOR = "titleTextColor";
    private static final String SUBTITLE_TEXT_COLOR = "subtitleTextColor";
    private static final String BAR_TINT_COLOR = "barTintColor";
    private static final String TINT_COLOR = "tintColor";
    private static final String MENU_ICON = "menuIcon";

    private int titleTextColor;
    private int subtitleTextColor;
    private int barTintColor;
    private int tintColor;
    private Bundle menuIcon;

    public NavBarStyle(ReadableMap rp) {
        ReadableMap menuIcon = rp.hasKey(MENU_ICON) ? rp.getMap(MENU_ICON) : null;
        this.titleTextColor = rp.hasKey(TITLE_TEXT_COLOR) && !rp.isNull(TITLE_TEXT_COLOR) ? rp.getInt(TITLE_TEXT_COLOR) : 0;
        this.subtitleTextColor = rp.hasKey(SUBTITLE_TEXT_COLOR) && !rp.isNull(SUBTITLE_TEXT_COLOR) ? rp.getInt(SUBTITLE_TEXT_COLOR) : 0;
        this.barTintColor = rp.hasKey(BAR_TINT_COLOR) && !rp.isNull(BAR_TINT_COLOR) ? rp.getInt(BAR_TINT_COLOR) : 0;
        this.tintColor = rp.hasKey(TINT_COLOR) && !rp.isNull(TINT_COLOR) ? rp.getInt(TINT_COLOR) : 0;
        this.menuIcon = menuIcon != null ? Arguments.toBundle(menuIcon) : null;
    }

    protected NavBarStyle(Parcel in) {
        titleTextColor = in.readInt();
        subtitleTextColor = in.readInt();
        barTintColor = in.readInt();
        tintColor = in.readInt();
        menuIcon = in.readBundle();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(titleTextColor);
        dest.writeInt(subtitleTextColor);
        dest.writeInt(barTintColor);
        dest.writeInt(tintColor);
        dest.writeBundle(menuIcon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NavBarStyle> CREATOR = new Creator<NavBarStyle>() {
        @Override
        public NavBarStyle createFromParcel(Parcel in) {
            return new NavBarStyle(in);
        }

        @Override
        public NavBarStyle[] newArray(int size) {
            return new NavBarStyle[size];
        }
    };

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public int getSubtitleTextColor() {
        return subtitleTextColor;
    }

    public int getBarTintColor() {
        return barTintColor;
    }

    public int getTintColor() {
        return tintColor;
    }

    public Bundle getMenuIcon() {
        return menuIcon;
    }

}
