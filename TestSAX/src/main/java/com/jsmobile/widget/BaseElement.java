package com.jsmobile.widget;

import android.content.Context;
import android.widget.ViewAnimator;

import com.jsmobile.data.Element;
import com.jsmobile.data.ElementData;
import com.jsmobile.data.LauncherLayout;

/**
 * Created by wangxin on 10/31/13.
 */
public abstract class BaseElement extends ViewAnimator{
    protected LauncherLayout.ElementLayoutInfo mElementLayoutInfo;
    protected Element mElement;

    public BaseElement(Context context, LauncherLayout.ElementLayoutInfo elementLayoutInfo, Element element) {
        super(context);
        mElementLayoutInfo = elementLayoutInfo;
        mElement = element;
        init();
    }

    private void init(){
        if(mElement.isCanFocus()){
            this.setFocusable(true);
            this.setClickable(true);
//            this.setBackgroundResource(R.drawable.imagefocus);
        }
        ElementLayout.LayoutParams elp = new ElementLayout.LayoutParams(mElementLayoutInfo.width, mElementLayoutInfo.height);
        elp.elementleft = mElementLayoutInfo.left;
        elp.elementtop = mElementLayoutInfo.top;
        this.setLayoutParams(elp);
        initElement();
    }

    protected abstract void initElement();

    public int getElementWidth() {
        return mElementLayoutInfo.width;
    }

    public void setElementWidth(int mWidht) {
        this.mElementLayoutInfo.width = mWidht;
    }

    public int getElementHeight() {
        return mElementLayoutInfo.height;
    }

    public void setElementHeight(int mHeight) {
        this.mElementLayoutInfo.height = mHeight;
    }

    public int getElementLeft() {
        return mElementLayoutInfo.left;
    }

    public void setElementLeft(int mLeft) {
        this.mElementLayoutInfo.left = mLeft;
    }

    public int getElementTop() {
        return mElementLayoutInfo.top;
    }

    public void setElementTop(int top) {
        this.mElementLayoutInfo.top = top;
    }

    public String getElementId() {
        return mElement.getId();
    }

    public boolean isCanFocus() {
        return mElement.isCanFocus();
    }

    public void setCanFocus(boolean mCanFocus) {
        this.mElement.setCanFocus(mCanFocus);
    }

    public String getType() {
        return mElementLayoutInfo.type;
    }

    public ElementData getElementData(int order){
        return mElement.getElementData(order);
    }

    public void setElementData(ElementData elementData){
        mElement.addElementData(elementData);
    }
}
