package com.jsmobile.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangxin on 10/29/13.
 */
public class LauncherPageData {
    private String mId;
    private String mVersion;

    Map<String, ShortCut> mShortCuts;
    Map<String, Element> mElements;

    public LauncherPageData() {
        mShortCuts = new HashMap<String, ShortCut>();
        mElements = new HashMap<String, Element>();
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String mVersion) {
        this.mVersion = mVersion;
    }

    public ShortCut getShortCut(String id){
        return mShortCuts.get(id);
    }

    public void addShortCut(ShortCut shortCut){
        mShortCuts.put(shortCut.getId(), shortCut);
    }

    public Element getElement(String id){
        return mElements.get(id);
    }

    public void addElement(Element element){
        mElements.put(element.getId(), element);
    }

    @Override
    public String toString() {
        return "LauncherPageData{" +
                "mId='" + mId + '\'' +
                ", mVersion='" + mVersion + '\'' +
                ", mShortCuts=" + mShortCuts +
                ", mElements=" + mElements +
                '}';
    }
}
