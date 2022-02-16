package com.fz.badgeview;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;

public interface IBadgeView {
    int getWidth();

    int getHeight();

    Context getContext();

    View getRootView();

    ViewParent getParent();
    boolean getGlobalVisibleRect(Rect r);
    void postInvalidate();
    int getId();
}
