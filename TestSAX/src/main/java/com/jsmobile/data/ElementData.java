package com.jsmobile.data;

/**
 * Created by wangxin on 10/29/13.
 */
public class ElementData {
    private int mOrder;
    private String mElementName;
    private String mElementDesc;
    private String mContentUrl;
    private ElementAction mElementAction;

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int mOrder) {
        this.mOrder = mOrder;
    }

    public String getElementName() {
        return mElementName;
    }

    public void setElementName(String mElementName) {
        this.mElementName = mElementName;
    }

    public String getElementDesc() {
        return mElementDesc;
    }

    public void setElementDesc(String mElementDesc) {
        this.mElementDesc = mElementDesc;
    }

    public String getContentUrl() {
        return mContentUrl;
    }

    public void setContentUrl(String mContentUrl) {
        this.mContentUrl = mContentUrl;
    }

    public ElementAction getElementAction() {
        return mElementAction;
    }

    public void setElementAction(ElementAction mElementAction) {
        this.mElementAction = mElementAction;
    }

    @Override
    public String toString() {
        return "ElementData{" +
                "mOrder=" + mOrder +
                ", mElementName='" + mElementName + '\'' +
                ", mElementDesc='" + mElementDesc + '\'' +
                ", mContentUrl='" + mContentUrl + '\'' +
                ", mElementAction=" + mElementAction +
                '}';
    }
}
