package com.jsmobile.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jsmobile.app.R;
import com.jsmobile.data.Element;
import com.jsmobile.data.ElementData;
import com.jsmobile.data.LauncherLayout;

import java.util.List;
import java.util.Map;

/**
 * Created by wangxin on 11/12/13.
 */
public class ListElement extends BaseElement {
    private BaseElement mLinkElement;

    //假设link的元素只能在list元素的左边或右边
    static final int LINK_AT_LEFT = 1;
    static final int LINK_AT_RIGHT = 2;
    private int linkPos;

    public ListElement(Context context, LauncherLayout.ElementLayoutInfo elementLayoutInfo, Element element) {
        super(context, elementLayoutInfo, element);
    }

    @Override
    protected void initElement() {
        this.setFocusable(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup parent = (ViewGroup) this.getParent();
        Log.d("wangx", "parent tag: " + parent.getTag() + ", linkElementId: " + getLinkElementId());
        mLinkElement = (BaseElement) parent.findViewWithTag(getLinkElementId());
        if(mLinkElement.getElementLeft() > this.getElementLeft()){
            linkPos = LINK_AT_RIGHT;
            this.setPadding(ElementLayout.DEFAULT_ELEMENT_MARGIN, ElementLayout.DEFAULT_ELEMENT_MARGIN, 0, ElementLayout.DEFAULT_ELEMENT_MARGIN);
        } else if(mLinkElement.getElementLeft() < this.getElementLeft()){
            Log.d("wangx", "link at left");
            linkPos = LINK_AT_LEFT;
            this.setPadding(0, ElementLayout.DEFAULT_ELEMENT_MARGIN, ElementLayout.DEFAULT_ELEMENT_MARGIN, ElementLayout.DEFAULT_ELEMENT_MARGIN);
        }

        LinearLayout container = new LinearLayout(this.getContext());
        container.setOrientation(container.VERTICAL);
        LinearLayout.LayoutParams cparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(cparams);

        int count = this.getAllElementDatas().size();
        for(int i = 0; i < count; i++){
            LinearLayout ll = new LinearLayout(this.getContext());
            ll.setOrientation(ll.HORIZONTAL);
            ll.setGravity(Gravity.CENTER);
            ll.setPadding(0, 6, 0, 6);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            ll.setLayoutParams(params);

            ImageView imageView = new ImageView(this.getContext());
            imageView.setVisibility(INVISIBLE);
            imageView.setImageDrawable(new ColorDrawable(Color.RED));
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ElementLayout.DEFAULT_ELEMENT_MARGIN, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setPadding(0, 25, 0, 25);

            TextView textView = new TextView(this.getContext());
            textView.setText(getAllElementDatas().get(i).getElementDesc());
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            if(linkPos == LINK_AT_LEFT){
                ll.addView(imageView);
                ll.addView(textView);
            } else if(linkPos == LINK_AT_RIGHT){
                ll.addView(textView);
                ll.addView(imageView);
            }

            ll.setTag(getAllElementDatas().get(i).getContentUrl());

            container.addView(ll);

            textView.setTag(imageView);
            textView.setFocusable(true);
            textView.setClickable(true);
            textView.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    TextView tv = (TextView) view;
                    ImageView iv = (ImageView) view.getTag();

                    if(b){
                        iv.setVisibility(View.VISIBLE);
                        LinearLayout parent = (LinearLayout) iv.getParent();
                        notifyFocusChange((String) parent.getTag());
                    } else {
                        iv.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        this.addView(container);

        notifyFocusChange(getAllElementDatas().get(0).getContentUrl());
    }





    public String getLinkElementId(){
        return mElement.getLinkElementId();
    }

    public BaseElement getLinkElement(){
        return mLinkElement;
    }

    private void notifyFocusChange(String url){
        BaseElement baseElement = getLinkElement();
        if(baseElement instanceof ImageElement){
            ((ImageElement)baseElement).notifyFocusChange(url);
        } else if(baseElement instanceof VideoElement){

        }
    }
}
