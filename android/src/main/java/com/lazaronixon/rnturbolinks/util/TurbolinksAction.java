package com.lazaronixon.rnturbolinks.util;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class TurbolinksAction implements Parcelable {


    private int id;
    private String title;
    private Bundle icon;
    private boolean button;

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
        button = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeBundle(icon);
        dest.writeByte((byte) (button ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Bundle getIcon() {
        return icon;
    }

    public boolean getButton() {
        return button;
    }
}
