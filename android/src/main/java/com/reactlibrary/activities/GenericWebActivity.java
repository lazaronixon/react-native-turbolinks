package com.reactlibrary.activities;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksView;
import com.reactlibrary.util.TurbolinksRoute;

public interface GenericWebActivity extends GenericActivity, TurbolinksAdapter {

    TurbolinksView getTurbolinksView();

    String getMessageHandler();

    String getUserAgent();

    void renderComponent(TurbolinksRoute route);

    void reload();

}
