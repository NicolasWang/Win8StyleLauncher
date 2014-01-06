package com.jsmobile.data;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxin on 10/29/13.
 */
public class Element {
    public static final String ELEMENT_TYPE_IMAGE = "image";
    public static final String ELEMENT_TYPE_VIDEO = "video";
    public static final String ELEMENT_TYPE_LIST = "list";
    public static final String ELEMENT_TYPE_WIDGET = "widget";
    public static final String ELEMENT_TYPE_CUSTOM = "custom";

    private String mId;
    private String mType;
    private boolean mCanFocus;
    private boolean mDefaultFocus;
    private boolean mAutoPlay;//only valid to video element
    private String mLink;//valid to video,list and  image element
    private SparseArray<ElementData> mElementDatas; //order as index

    public Element() {
        mElementDatas = new SparseArray<ElementData>();
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public boolean isCanFocus() {
        return mCanFocus;
    }

    public void setCanFocus(boolean mCanFocus) {
        this.mCanFocus = mCanFocus;
    }

    public boolean isDefaultFocus(){
        return mDefaultFocus;
    }

    public void setDefaultFocus(boolean defaultFocus){
        this.mDefaultFocus = defaultFocus;
    }

    public boolean isAutoPlay(){
        return mAutoPlay;
    }

    public void setAutoPlay(boolean autoPlay){
        this.mAutoPlay = autoPlay;
    }

    public String getLinkElementId(){
        return mLink;
    }

    public void setLinkElementId(String id){
        this.mLink = id;
    }

    public void addElementData(ElementData elementData){
        mElementDatas.append(elementData.getOrder(), elementData);
    }

    public ElementData getElementData(int order){
        return mElementDatas.get(order);
    }

    public SparseArray<ElementData> getAllElementDatas(){
        return mElementDatas;
    }

    @Override
    public String toString() {
        return "Element{" +
                "mId='" + mId + '\'' +
                ", mType='" + mType + '\'' +
                ", mCanFocus=" + mCanFocus +
                ", mElementDatas=" + mElementDatas +
                '}';
    }
}
