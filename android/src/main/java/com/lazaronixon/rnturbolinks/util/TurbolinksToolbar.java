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

    private ImageView spinner;

    private static final int[] TEMP_ARRAY = new int[1];

    public TurbolinksToolbar(Context context) { super(context); }

    public TurbolinksToolbar(Context context, @Nullable AttributeSet attrs) { super(context, attrs); }

    public TurbolinksToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSpinner(boolean showSpinner) {
        if (showSpinner) {
            spinner = new ImageView(getContext());
            spinner.setColorFilter(getThemeAttrColor(getContext(), R.attr.colorControlNormal));
            spinner.setImageResource(R.drawable.abc_spinner_mtrl_am_alpha);

            addView(spinner, spinnerLayoutParam());
        } else if (spinner != null) {
            removeView(spinner);
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

    private LayoutParams spinnerLayoutParam() {
        int marginTop = getSubtitle() != null ? 76 : 0;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0, marginTop);
        return layoutParams;
    }
}
