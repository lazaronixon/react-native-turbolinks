package com.lazaronixon.rnturbolinks;

import android.view.KeyEvent;
import com.facebook.react.ReactActivity;

public abstract class RNTurbolinksActivity extends ReactActivity {
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode == KeyEvent.KEYCODE_VOLUME_UP ? KeyEvent.KEYCODE_MENU : keyCode, event);
    }
}
