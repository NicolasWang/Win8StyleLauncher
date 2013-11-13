package com.jsmobile.widget;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ViewAnimator;

import com.jsmobile.app.R;

/**
 * Created by wangxin on 11/11/13.
 */
public class PageView extends FrameLayout {
    private ElementLayout mElementLayout;

    public PageView(Context context) {
        super(context);
    }

    public void addElementLayout(ElementLayout elementLayout){
        this.addView(elementLayout);
        mElementLayout = elementLayout;
    }

    public ElementLayout getElementLayout(){
        return mElementLayout;
    }

}
