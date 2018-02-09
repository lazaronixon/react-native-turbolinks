package com.reactlibrary.util;

import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;

public class TurbolinksTabBar {

    public static final String INTENT_SELECTED_INDEX = "intentSelectedIndex";
    public static final String INTENT_ROUTES = "intentRoutes";

    private int selectedIndex;
    private ArrayList<Bundle> routes;

    public TurbolinksTabBar(ReadableMap rp) {
        ReadableArray routes = rp.hasKey("routes") ? rp.getArray("routes") : null;
        this.selectedIndex = rp.hasKey("selectedIndex") ? rp.getInt("selectedIndex") : 0;
        this.routes = rp.hasKey("routes") ? Arguments.toList(routes) : null;
    }


    public int getSelectedIndex() {
        return selectedIndex;
    }

    public ArrayList<Bundle> getRoutes() {
        return routes;
    }
}
