package com.reactlibrary.util;

import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;

import java.util.ArrayList;

public class TurbolinksTabBar {

    public static final String INTENT_SELECTED_INDEX = "intentSelectedIndex";
    public static final String INTENT_ROUTES = "intentRoutes";

    private int selectedIndex;
    private ArrayList<Bundle> routes;

    public TurbolinksTabBar(ReadableArray routes, Integer selectedIndex) {
        this.routes = Arguments.toList(routes);
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public ArrayList<Bundle> getRoutes() {
        return routes;
    }
}
