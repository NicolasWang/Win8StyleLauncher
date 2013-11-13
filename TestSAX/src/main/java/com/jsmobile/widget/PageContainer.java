package com.jsmobile.widget;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;

import com.jsmobile.app.R;

/**
 * Created by wangxin on 11/11/13.
 */
public class PageContainer extends ViewAnimator {
    public PageContainer(Context context) {
        super(context);
    }

    public void showNextPage(){
        this.setInAnimation(this.getContext(), R.anim.rightinanimation);
        this.setOutAnimation(this.getContext(), R.anim.leftoutanimation);
        this.showNext();
    }

    public void showPreviousPage(){
        this.setInAnimation(this.getContext(), R.anim.leftinanimation);
        this.setOutAnimation(this.getContext(), R.anim.rightoutanimation);
        this.showPrevious();
    }

    public void showPage(String id){

    }
}
