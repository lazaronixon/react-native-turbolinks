package com.reactlibrary.util;

import android.content.Intent;

import com.facebook.react.bridge.ReadableMap;

public class ToolBarDesign {

    public static final String INTENT_TOOL_BAR_HIDDEN = "intentToolBarHidden";
    public static final String INTENT_BAR_TINT_COLOR = "intentBarTintColor";
    public static final String INTENT_TINT_COLOR = "intentTintColor";
    public static final String INTENT_TITLE_TEXT_COLOR = "intentTitleTextColor";
    public static final String INTENT_SUBTITLE_TEXT_COLOR = "intentSubtitleTextColor";

    private Boolean hidden;
    private String barTintColor;
    private String tintColor;
    private String titleTextColor;
    private String subTitleTextColor;

    public ToolBarDesign() {
    }

    public ToolBarDesign(ReadableMap rp) {
        this.hidden = rp.hasKey("hidden") ? rp.getBoolean("hidden") : false;
        this.barTintColor = rp.hasKey("barTintColor") ? rp.getString("barTintColor") : null;
        this.tintColor = rp.hasKey("tintColor") ? rp.getString("tintColor") : null;
        this.titleTextColor = rp.hasKey("titleTextColor") ? rp.getString("titleTextColor") : null;
        this.subTitleTextColor = rp.hasKey("subTitleTextColor") ? rp.getString("subTitleTextColor") : null;
    }

    public ToolBarDesign(Intent intent) {
        this.hidden = intent.getBooleanExtra(INTENT_TOOL_BAR_HIDDEN, false);
        this.barTintColor = intent.getStringExtra(INTENT_BAR_TINT_COLOR);
        this.tintColor = intent.getStringExtra(INTENT_TINT_COLOR);
        this.titleTextColor = intent.getStringExtra(INTENT_TITLE_TEXT_COLOR);
        this.subTitleTextColor = intent.getStringExtra(INTENT_SUBTITLE_TEXT_COLOR);
    }

    public Boolean getHidden() {
        return hidden;
    }

    public String getBarTintColor() {
        return barTintColor;
    }

    public String getTintColor() {
        return tintColor;
    }

    public String getTitleTextColor() {
        return titleTextColor;
    }

    public String getSubTitleTextColor() {
        return subTitleTextColor;
    }
}
