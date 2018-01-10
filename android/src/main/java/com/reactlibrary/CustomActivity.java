package com.reactlibrary;

import android.os.Bundle;

import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactContext;

public class CustomActivity extends ReactActivity {

    private static final String INTENT_URL = "intentUrl";
    private static final String INTENT_REACT_TAG = "intentReactTag";
    private static final String INTENT_FROM = "intentFrom";

    private String location;
    private String fromActivity;
    private RNTurbolinksView turbolinksView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = getIntent().getStringExtra(INTENT_URL);
        fromActivity = getIntent().getStringExtra(INTENT_FROM);
        int reactTag = getIntent().getIntExtra(INTENT_REACT_TAG, 0);
        setContentView(R.layout.activity_custom);
        turbolinksView = (RNTurbolinksView) findViewById(R.id.turbolinks_view);
        turbolinksView.setId(reactTag);
        TurbolinksSession.getDefault(this)
                .activity(this)
                .adapter(turbolinksView)
                .view(turbolinksView)
                .visit(location);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        TurbolinksSession.getDefault(this)
                .activity(this)
                .adapter(turbolinksView)
                .restoreWithCachedSnapshot(true)
                .view(turbolinksView)
                .visit(location);
    }

    @Override
    public void onBackPressed() {
        if (fromActivity.equals("MainActivity")) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    public ReactContext getReactContext() {
        return getReactInstanceManager().getCurrentReactContext();
    }
}
