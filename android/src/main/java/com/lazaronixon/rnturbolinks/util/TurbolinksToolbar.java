package com.lazaronixon.rnturbolinks.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.R;

public class TurbolinksToolbar extends Toolbar {

    private ImageView dropdownIcon;

    private static final int[] TEMP_ARRAY = new int[1];

    public TurbolinksToolbar(Context context) { super(context); }

    public TurbolinksToolbar(Context context, @Nullable AttributeSet attrs) { super(context, attrs); }

    public TurbolinksToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVisibleDropDown(boolean visibleDropDown) {
        if (visibleDropDown) {
            dropdownIcon = new ImageView(getContext());
            dropdownIcon.setColorFilter(getThemeAttrColor(getContext(), R.attr.colorControlNormal));
            dropdownIcon.setImageResource(com.lazaronixon.rnturbolinks.R.drawable.ic_caret);

            addView(dropdownIcon, dropDownLayoutParam());
        } else if (dropdownIcon != null) {
            removeView(dropdownIcon);
        }
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

    private LayoutParams dropDownLayoutParam() {
        int marginBottom = getSubtitle() != null ? 76 : 0;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0, marginBottom);
        return layoutParams;
    }
}
