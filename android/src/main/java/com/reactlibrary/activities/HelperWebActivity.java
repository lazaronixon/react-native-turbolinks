package com.reactlibrary.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.basecamp.turbolinks.TurbolinksSession;
import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;

import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

public class HelperWebActivity extends HelperActivity {

    private static final int HTTP_FAILURE = 0;
    private static final int NETWORK_FAILURE = 1;

    private GenericWebActivity act;

    private static final int REQUEST_SELECT_FILE = 100;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private Boolean mUploadingFile = false;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> uploadMessage;

    public HelperWebActivity(GenericWebActivity genericWebActivity) {
        super(genericWebActivity);
        this.act = genericWebActivity;
    }

    public void onRestart() {
        Activity activity = (Activity) act;
        if (mUploadingFile) {
            TurbolinksSession.getDefault(act.getApplicationContext())
                    .activity(activity)
                    .adapter(act)
                    .view(act.getTurbolinksView());
        } else {
            TurbolinksSession.getDefault(act.getApplicationContext())
                    .activity(activity)
                    .adapter(act)
                    .restoreWithCachedSnapshot(true)
                    .view(act.getTurbolinksView())
                    .visit(act.getRoute().getUrl());
        }
    }

    public void onReceivedError(int errorCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", NETWORK_FAILURE);
        params.putInt("statusCode", 0);
        params.putString("description", "Network Failure.");
        act.getEventEmitter().emit("turbolinksError", params);
    }

    public void requestFailedWithStatusCode(int statusCode) {
        WritableMap params = Arguments.createMap();
        params.putInt("code", HTTP_FAILURE);
        params.putInt("statusCode", statusCode);
        params.putString("description", "HTTP Failure. Code:" + statusCode);
        act.getEventEmitter().emit("turbolinksError", params);
    }

    public void visitCompleted() {
        renderTitle();
        handleVisitCompleted();
    }

    public void visitProposedToLocationWithAction(String location, String action) {
        try {
            WritableMap params = Arguments.createMap();
            URL urlLocation = new URL(location);
            params.putString("component", null);
            params.putString("url", urlLocation.toString());
            params.putString("path", urlLocation.getPath());
            params.putString("action", action);
            act.getEventEmitter().emit("turbolinksVisit", params);
        } catch (MalformedURLException e) {
            Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
        }
    }

    public void handleVisitCompleted() {
        String javaScript = "document.documentElement.outerHTML";
        final WebView webView = TurbolinksSession.getDefault(act.getApplicationContext()).getWebView();
        webView.evaluateJavascript(javaScript, new ValueCallback<String>() {
            public void onReceiveValue(String source) {
                try {
                    WritableMap params = Arguments.createMap();
                    URL urlLocation = new URL(webView.getUrl());
                    params.putString("url", urlLocation.toString());
                    params.putString("path", urlLocation.getPath());
                    params.putString("source", unescapeJava(source));
                    act.getEventEmitter().emit("turbolinksVisitCompleted", params);
                } catch (MalformedURLException e) {
                    Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
                }
            }
        });
    }

    public void renderTitle() {
        WebView webView = TurbolinksSession.getDefault(act.getApplicationContext()).getWebView();
        String title = act.getRoute().getTitle() != null ? act.getRoute().getTitle() : webView.getTitle();
        act.getSupportActionBar().setTitle(title);
        act.getSupportActionBar().setSubtitle(act.getRoute().getSubtitle());
    }

    public void onBackPressed() {
        if (act.getInitialVisit()) {
            act.moveTaskToBack(true);
        } else {
            act.onSuperBackPressed();
        }
    }

    public void postMessage(String message) {
        act.getEventEmitter().emit("turbolinksMessage", message);
    }

    public void reloadSession() {
        TurbolinksSession.getDefault(act.getApplicationContext()).getWebView().reload();
    }

    public void handleTitlePress(Toolbar toolbar) {
        final WebView webView = TurbolinksSession.getDefault(act.getApplicationContext()).getWebView();
        toolbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    WritableMap params = Arguments.createMap();
                    URL urlLocation = new URL(webView.getUrl());
                    params.putString("component", null);
                    params.putString("url", urlLocation.toString());
                    params.putString("path", urlLocation.getPath());
                    act.getEventEmitter().emit("turbolinksTitlePress", params);
                } catch (MalformedURLException e) {
                    Log.e(ReactConstants.TAG, "Error parsing URL. " + e.toString());
                }
            }
        });
    }

    public void visitTurbolinksView(TurbolinksView turbolinksView, String url) {
        Context context = act.getApplicationContext();
        Activity activity = (Activity) act;

        if (act.getMessageHandler() != null) {
            TurbolinksSession.getDefault(context).addJavascriptInterface(act, act.getMessageHandler());
        }
        if (act.getUserAgent() != null) {
            TurbolinksSession.getDefault(context).getWebView().getSettings().setUserAgentString(act.getUserAgent());
        }

        TurbolinksSession.getDefault(context).activity(activity).adapter(act).view(turbolinksView).visit(url);
    }

    public void setupFileChooser() {
        WebView webView = TurbolinksSession.getDefault(act.getApplicationContext()).getWebView();
        webView.setWebChromeClient(new WebChromeClient() {
            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadingFile = true;
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                act.startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadingFile = true;
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                uploadMessage = filePathCallback;

                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    intent = fileChooserParams.createIntent();
                }
                try {
                    act.startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Log.e(ReactConstants.TAG, "Cannot Open File Chooser.");
                    return false;
                }
                return true;
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null) return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else {
            Log.e(ReactConstants.TAG, "Failed to Upload Image.");
        }
    }

}
