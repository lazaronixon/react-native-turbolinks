package com.lazaronixon.rnturbolinks.util;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;

public class NavBarStyle implements Parcelable {

    private static String TITLE_TEXT_COLOR = "titleTextColor";
    private static String SUBTITLE_TEXT_COLOR = "subtitleTextColor";
    private static String BAR_TINT_COLOR = "barTintColor";
    private static String TINT_COLOR = "tintColor";

    private int titleTextColor;
    private int subtitleTextColor;
    private int barTintColor;
    private int tintColor;

    public NavBarStyle(ReadableMap rp) {
        this.titleTextColor = rp.hasKey(TITLE_TEXT_COLOR) && !rp.isNull(TITLE_TEXT_COLOR) ? rp.getInt(TITLE_TEXT_COLOR) : 0;
        this.subtitleTextColor = rp.hasKey(SUBTITLE_TEXT_COLOR) && !rp.isNull(SUBTITLE_TEXT_COLOR) ? rp.getInt(SUBTITLE_TEXT_COLOR) : 0;
        this.barTintColor = rp.hasKey(BAR_TINT_COLOR) && !rp.isNull(BAR_TINT_COLOR) ? rp.getInt(BAR_TINT_COLOR) : 0;
        this.tintColor = rp.hasKey(TINT_COLOR) && !rp.isNull(TINT_COLOR) ? rp.getInt(TINT_COLOR) : 0;
    }

    protected NavBarStyle(Parcel in) {
        titleTextColor = in.readInt();
        subtitleTextColor = in.readInt();
        barTintColor = in.readInt();
        tintColor = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(titleTextColor);
        dest.writeInt(subtitleTextColor);
        dest.writeInt(barTintColor);
        dest.writeInt(tintColor);
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

}
