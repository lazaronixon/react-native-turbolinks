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

    private Integer titleTextColor;
    private Integer subtitleTextColor;
    private Integer barTintColor;
    private Bundle menuIcon;

    private NavBarStyle() {
    }

    public NavBarStyle setData(ReadableMap rp) {
        ReadableMap menuIcon = rp.hasKey(MENU_ICON) ? rp.getMap(MENU_ICON) : null;
        this.titleTextColor = rp.hasKey(TITLE_TEXT_COLOR) && !rp.isNull(TITLE_TEXT_COLOR) ? rp.getInt(TITLE_TEXT_COLOR) : null;
        this.subtitleTextColor = rp.hasKey(SUBTITLE_TEXT_COLOR) && !rp.isNull(SUBTITLE_TEXT_COLOR) ? rp.getInt(SUBTITLE_TEXT_COLOR) : null;
        this.barTintColor = rp.hasKey(BAR_TINT_COLOR) && !rp.isNull(BAR_TINT_COLOR) ? rp.getInt(BAR_TINT_COLOR) : null;
        this.menuIcon = menuIcon != null ? Arguments.toBundle(menuIcon) : null;
        return this;
    }

    public Integer getTitleTextColor() {
        return titleTextColor;
    }

    public Integer getSubtitleTextColor() {
        return subtitleTextColor;
    }

    public Integer getBarTintColor() {
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
