package com.lazaronixon.rnturbolinks.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.react.bridge.ReadableMap;

public class TabBarStyle implements Parcelable {

    private static final String BAR_TINT_COLOR = "barTintColor";
    private static final String TINT_COLOR = "tintColor";

    private int barTintColor;
    private int tintColor;

    public TabBarStyle(ReadableMap rp) {
        this.barTintColor = rp.hasKey(BAR_TINT_COLOR) && !rp.isNull(BAR_TINT_COLOR) ? rp.getInt(BAR_TINT_COLOR) : 0;
        this.tintColor = rp.hasKey(TINT_COLOR) && !rp.isNull(TINT_COLOR) ? rp.getInt(TINT_COLOR) : 0;
    }

    protected TabBarStyle(Parcel in) {
        barTintColor = in.readInt();
        tintColor = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(barTintColor);
        dest.writeInt(tintColor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TabBarStyle> CREATOR = new Creator<TabBarStyle>() {
        @Override
        public TabBarStyle createFromParcel(Parcel in) {
            return new TabBarStyle(in);
        }

        @Override
        public TabBarStyle[] newArray(int size) {
            return new TabBarStyle[size];
        }
    };

    public int getBarTintColor() {
        return barTintColor;
    }

    public int getTintColor() {
        return tintColor;
    }

}
