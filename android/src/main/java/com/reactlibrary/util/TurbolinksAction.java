package com.reactlibrary.util;

import android.os.Parcel;
import android.os.Parcelable;

public class TurbolinksAction implements Parcelable {

    public static final Creator<TurbolinksAction> CREATOR = new Creator<TurbolinksAction>() {
        @Override
        public TurbolinksAction createFromParcel(Parcel in) {
            return new TurbolinksAction(in);
        }

        @Override
        public TurbolinksAction[] newArray(int size) {
            return new TurbolinksAction[size];
        }
    };
    private String title;
    private String icon;
    private Boolean asButton = false;

    protected TurbolinksAction(Parcel in) {
        title = in.readString();
        icon = in.readString();
        byte tmpAsButton = in.readByte();
        asButton = tmpAsButton == 0 ? null : tmpAsButton == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(icon);
        dest.writeByte((byte) (asButton == null ? 0 : asButton ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public Boolean getAsButton() {
        return asButton;
    }
}
