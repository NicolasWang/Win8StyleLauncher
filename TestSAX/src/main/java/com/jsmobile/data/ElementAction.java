package com.jsmobile.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangxin on 10/25/13.
 */
public class ElementAction {

    public static final int ACTION_INVALID = 0;
    public static final int ACTION_PLAY_VIDEO = 1;
    public static final int ACTION_SLIDE_PAGE = 2;
    public static final int ACTION_OPEN_WEBPAGE = 3;
    public static final int ACTION_START_APP = 4;
    public static final int ACTION_START_WIDGET = 5;

    private int mActionType;
    private String mActionUrl;
    private Map<String, String> mParams;

    public ElementAction() {
        mActionType = ACTION_INVALID;
        mParams = new HashMap<String, String>();
    }

    public boolean isValid(int type){
        if(type > ACTION_START_WIDGET || type < ACTION_PLAY_VIDEO)
            return false;
        else
            return true;
    }

    public int getActionType(){
        return mActionType;
    }

    public void setActionType(int type){
        if(isValid(type)){
            mActionType = type;
        } else {
            mActionType = ACTION_INVALID;
        }
    }

    public String getmActionUrl() {
        return mActionUrl;
    }

    public void setActionUrl(String url){
        mActionUrl = url;
    }

    public String getParam(String name){
        if(mParams.containsKey(name)){
            return mParams.get(name);
        } else
            return null;
    }

    public void addParam(String name, String value){
        mParams.put(name, value);
    }

    @Override
    public String toString() {
        return "ElementAction{" +
                "mActionType=" + mActionType +
                ", mActionUrl='" + mActionUrl + '\'' +
                ", mParams=" + mParams +
                '}';
    }
}
