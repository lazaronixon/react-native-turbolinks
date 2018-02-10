package com.reactlibrary.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.basecamp.turbolinks.TurbolinksView;
import com.facebook.react.ReactRootView;

import java.util.ArrayList;

public class TurbolinksPagerAdapter extends PagerAdapter {

    private ArrayList<View> viewList = new ArrayList<>();

    public TurbolinksPagerAdapter(Context context, ArrayList<Bundle> routes) {
        for(Bundle bundle: routes) {
            TurbolinksRoute route = new TurbolinksRoute(bundle);
            TurbolinksView webView = new TurbolinksView(context);
            ReactRootView nativeView = new ReactRootView(context);
            View view =  route.getUrl() != null ? webView : nativeView;
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            viewList.add(view);
        }
    }

    public View getItem(int position) {
        return viewList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
