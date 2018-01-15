package com.reactlibrary;

import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.basecamp.turbolinks.TurbolinksSession;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactContext;

public class CustomActivity extends ReactActivity {

    private static final String INTENT_URL = "intentUrl";
    private static final String INTENT_FROM = "intentFrom";
    private static final String INTENT_ACTION = "intentAction";
    private static final String INTENT_USER_AGENT = "intentUserAgent";
    private static final String INTENT_REACT_TAG = "intentReactTag";

    private String location;
    private String fromActivity;
    private String action;
    private String userAgent;
    private Integer reactTag;
    private RNTurbolinksView turbolinksView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = getIntent().getStringExtra(INTENT_URL);
        fromActivity = getIntent().getStringExtra(INTENT_FROM);
        reactTag = getIntent().getIntExtra(INTENT_REACT_TAG, 0);
        action = getIntent().getStringExtra(INTENT_ACTION);
        userAgent = getIntent().getStringExtra(INTENT_USER_AGENT);

        handleAnimation(true);
        setContentView(R.layout.activity_custom);
        turbolinksView = (RNTurbolinksView) findViewById(R.id.turbolinks_view);
        turbolinksView.setId(reactTag);

        TurbolinksSession.getDefault(this).getWebView().getSettings().setUserAgentString(userAgent);
        TurbolinksSession.getDefault(this).addJavascriptInterface(turbolinksView, "RNTurbolinks");
        TurbolinksSession.getDefault(this).activity(this).adapter(turbolinksView).view(turbolinksView).visit(location);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handleAnimation(false);
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

    private void handleAnimation(Boolean isForward) {
        if (fromActivity.equals("MainActivity")) {
            overridePendingTransition(R.anim.nothing, R.anim.nothing);
        } else if (action.equals("advance")) {
            if (isForward) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else if (action.equals("replace")) {
            overridePendingTransition(R.anim.nothing, R.anim.nothing);
        }
    }
}
