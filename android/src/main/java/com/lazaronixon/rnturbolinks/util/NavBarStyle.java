package com.lazaronixon.rnturbolinks.util;

import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;

public class NavBarStyle {
    static volatile NavBarStyle defaultInstance;

    private static final String TITLE_TEXT_COLOR = "titleTextColor";
    private static final String SUBTITLE_TEXT_COLOR = "subtitleTextColor";
    private static final String BAR_TINT_COLOR = "barTintColor";
    private static final String MENU_ICON = "menuIcon";

    private int titleTextColor;
    private int subtitleTextColor;
    private int barTintColor;
    private Bundle menuIcon;

    public NavBarStyle() {
        this.titleTextColor = 0;
        this.subtitleTextColor = 0;
        this.barTintColor = 0;
        this.menuIcon = null;
    }

    public NavBarStyle build(ReadableMap rp) {
        ReadableMap menuIcon = rp.hasKey(MENU_ICON) ? rp.getMap(MENU_ICON) : null;
        this.titleTextColor = rp.hasKey(TITLE_TEXT_COLOR) && !rp.isNull(TITLE_TEXT_COLOR) ? rp.getInt(TITLE_TEXT_COLOR) : 0;
        this.subtitleTextColor = rp.hasKey(SUBTITLE_TEXT_COLOR) && !rp.isNull(SUBTITLE_TEXT_COLOR) ? rp.getInt(SUBTITLE_TEXT_COLOR) : 0;
        this.barTintColor = rp.hasKey(BAR_TINT_COLOR) && !rp.isNull(BAR_TINT_COLOR) ? rp.getInt(BAR_TINT_COLOR) : 0;
        this.menuIcon = menuIcon != null ? Arguments.toBundle(menuIcon) : null;
        return this;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public int getSubtitleTextColor() {
        return subtitleTextColor;
    }

    public int getBarTintColor() {
        return barTintColor;
    }

    public Bundle getMenuIcon() {
        return menuIcon;
    }

    public static NavBarStyle getInstance() {
        if (defaultInstance == null) {
            synchronized (NavBarStyle.class) {
                if (defaultInstance == null) {
                    defaultInstance = new NavBarStyle();
                }
            }
        }
        return defaultInstance;
    }

}
