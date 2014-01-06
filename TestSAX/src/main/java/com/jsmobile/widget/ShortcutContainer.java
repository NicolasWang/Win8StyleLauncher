package com.jsmobile.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by wangxin on 11/21/13.
 */
public class ShortcutContainer extends RelativeLayout {
    public ShortcutContainer(Context context) {
        super(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(1280,
                111);
        this.setLayoutParams(lp);
        this.setVerticalGravity(Gravity.CENTER);
    }
}
