package com.lazaronixon.rnturbolinks.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TurbolinksViewPager extends ViewPager {

    public TurbolinksViewPager(Context context) {
        super(context);
    }

    public TurbolinksViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) { return false; }

}
