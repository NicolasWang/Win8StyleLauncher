package com.jsmobile.data;

/**
 * Created by wangxin on 10/29/13.
 */
public class ShortCut {
    public static final String NAV_TYPE_LOGO = "NAV_LOGO";
    public static final String NAV_TYPE_USER_LOGIN = "NAV_USER_LOGIN";
    public static final String NAV_TYPE_USER_MESSAGE = "NAV_USER_MESSAGE";
    public static final String NAV_TYPE_CLOCK_WEATHER = "NAV_CLOCK_WEATHER";
    public static final String NAV_TYPE_SHOW_WIFI = "NAV_SHOW_WIFI";

    private String mId;
    private String mType;
    private boolean mCanFocus;
    private String mName;
    private ElementAction mElementAction;

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public boolean isCanFocus() {
        return mCanFocus;
    }

    public void setCanFocus(boolean mCanFocus) {
        this.mCanFocus = mCanFocus;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public ElementAction getElementAction() {
        return mElementAction;
    }

    public void setElementAction(ElementAction mElementAction) {
        this.mElementAction = mElementAction;
    }

    @Override
    public String toString() {
        return "ShortCut{" +
                "mId='" + mId + '\'' +
                ", mType='" + mType + '\'' +
                ", mCanFocus=" + mCanFocus +
                ", mName='" + mName + '\'' +
                ", mElementAction=" + mElementAction +
                '}';
    }
}

