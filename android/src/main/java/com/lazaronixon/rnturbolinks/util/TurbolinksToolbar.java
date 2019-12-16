package com.lazaronixon.rnturbolinks.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import com.lazaronixon.rnturbolinks.R;

public class TurbolinksToolbar extends Toolbar {

    private boolean visibleDropDown = false;

    private static final int[] TEMP_ARRAY = new int[1];

    public TurbolinksToolbar(Context context) { super(context); }

    public TurbolinksToolbar(Context context, @Nullable AttributeSet attrs) { super(context, attrs); }

    public TurbolinksToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVisibleDropDown(boolean visibleDropDown) {
        this.visibleDropDown = visibleDropDown;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (visibleDropDown) {
            this.setTitleWithDropDown(title);
        } else {
            super.setTitle(title);
        }
    }

    private void setTitleWithDropDown(CharSequence title) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(title).append(" ");
        builder.setSpan(dropDownIcon(), builder.length() - 1, builder.length(), 0);
        super.setTitle(builder);
    }

    private ImageSpan dropDownIcon() {
        ImageSpan imageSpan = new ImageSpan(getContext(), R.drawable.ic_caret);
        DrawableCompat.setTint(imageSpan.getDrawable(), getThemeAttrColor(getContext(), R.attr.colorControlNormal));
        return imageSpan;
    }

    @SuppressLint("RestrictedApi")
    private int getThemeAttrColor(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, TEMP_ARRAY);
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }
}
