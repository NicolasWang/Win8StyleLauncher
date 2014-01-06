package com.jsmobile.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsmobile.app.JsMobileApplication;
import com.jsmobile.app.R;

/**
 * Created by wangxin on 11/5/13.
 */
public class Navigator extends RelativeLayout {
    public Navigator(Context context) {
        super(context);
        init();
    }

    private void init(){
        /*
         * 全屏的导航层，里面的scrollview是位于这一层的最下方
         */
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setGravity(Gravity.CENTER_HORIZONTAL);

        mScrollView = new HorizontalScrollView(getContext());
        RelativeLayout.LayoutParams rlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                58);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mScrollView.setLayoutParams(rlp);
        mScrollView.setHorizontalScrollBarEnabled(false);

        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);


        JsMobileApplication app = (JsMobileApplication) getContext().getApplicationContext();
        int pageNum = app.getLauncherLayout().getPageNum();
        for(int i = 0; i < pageNum; i++){
            String id = app.getLauncherLayout().getIdByOrder(i + 1); //TODO
            String str = app.getLauncherLayout().getPageLayoutInfo(id).getPageName();
            TextView tv = newText(str);
            tv.setTag(app.getLauncherLayout().getPageLayoutInfo(id).getPageId());
            mLinearLayout.addView(tv);
        }

        mScrollView.addView(mLinearLayout);

        this.addView(mScrollView);

        Log.d("wangx", "LinearLayout focusable " + mLinearLayout.getDescendantFocusability());
        Log.d("wangx", "scrollview focusable " + mScrollView.getDescendantFocusability());
        Log.d("wangx", "navigator focusable " + this.getDescendantFocusability());
        mLinearLayout.setFocusable(false);
        mScrollView.setFocusable(true);
//        this.setFocusable(fa);
//        mLinearLayout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
//        mScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//        this.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("wangx", "Navigator onFocusChange");
                if(b)mScrollView.requestFocus();
            }
        });
        mScrollView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("wangx", "scrollview onFocusChange " + b);
                if(b)mLinearLayout.requestFocus();
            }
        });
        mLinearLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("wangx", "LinearLayout onFocusChange");
            }
        });
    }

    //向下滑动到
    public void focusDownToChild(String tag){
        mLinearLayout.findViewWithTag(tag).requestFocus();
        mLinearLayout.findViewWithTag(tag).setBackgroundResource(R.drawable.nav_focus);
        int count = mLinearLayout.getChildCount();
        for(int i = 0; i < count; i++){
            View view = mLinearLayout.getChildAt(i);
            String str = (String) view.getTag();
            if(!str.equalsIgnoreCase(tag)){
                view.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    //水平滑动到
    public void focusMoveToChild(String tag){
        Log.d("wangx", "focusMoveToChild " + tag);
        mLinearLayout.findViewWithTag(tag).requestFocus();
        mLinearLayout.findViewWithTag(tag).setBackgroundResource(R.drawable.nav_focus);
        int count = mLinearLayout.getChildCount();
        for(int i = 0; i < count; i++){
            View view = mLinearLayout.getChildAt(i);
            String str = (String) view.getTag();
            if(!str.equalsIgnoreCase(tag)){
                view.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public void selectionMoveToChild(String previous, String now){
        //TODO
        Log.d("wangx", "selectionMoveToChild, previous=" + previous + ",now=" + now);
//        mLinearLayout.findViewWithTag(previous).setSelected(false);
//        mLinearLayout.findViewWithTag(now).setSelected(true);
        mLinearLayout.findViewWithTag(now).setBackgroundResource(R.drawable.nav_focus);

        int count = mLinearLayout.getChildCount();
        for(int i = 0; i < count; i++){
            View view = mLinearLayout.getChildAt(i);
            String tag = (String) view.getTag();
            if(!tag.equalsIgnoreCase(now)){
                view.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private TextView newText(String str){
        TextView tv = new TextView(getContext());
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        tv.setText(str);
        tv.setFocusable(true);
        tv.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("wangx", "textview onfocuschange " + b);
                String tag = (String) Navigator.this.getFocusedChild().getTag();
                Log.d("wangx", "textview onfocuschange tag: " + tag + ",getFocusedChild:" + Navigator.this.getFocusedChild() + ",view:" + view);
                Log.d("wangx", "textview onfocuschange tvtag: " + view.getTag());

            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 40); //TODO

        lp.setMargins(15, 0, 15, 0);//TODO

        tv.setLayoutParams(lp);

        return tv;
    }

    public HorizontalScrollView mScrollView;//TODO
    public LinearLayout mLinearLayout;//TODO

}
