package com.reactlibrary.util;

import android.os.Bundle;
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
    private int id;
    private String title;
    private Bundle icon;
    private Boolean button = false;

    public TurbolinksAction(Bundle bundle) {
        this.id = (int) bundle.getDouble("id");
        this.title = bundle.getString("title");
        this.icon = bundle.getBundle("icon");
        this.button = bundle.getBoolean("button");
    }

    protected TurbolinksAction(Parcel in) {
        id = in.readInt();
        title = in.readString();
        icon = in.readBundle();
        byte tmpButton = in.readByte();
        button = tmpButton == 0 ? null : tmpButton == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeBundle(icon);
        dest.writeByte((byte) (button == null ? 0 : button ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Bundle getIcon() {
        return icon;
    }

    public Boolean getButton() {
        return button;
    }
}
