package com.jsmobile.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangxin on 10/29/13.
 */
public class LauncherData {
    private LauncherCommonData mCommonData;
    private Map<String, LauncherPageData> mAllPagesData;

    public LauncherData() {
        mCommonData = new LauncherCommonData();
        mAllPagesData = new HashMap<String, LauncherPageData>();
    }

    public LauncherCommonData getCommonData(){
        return mCommonData;
    }

    public void setCommonData(LauncherCommonData commonData){
        mCommonData = commonData;
    }

    public LauncherPageData getPageData(String id){
        return mAllPagesData.get(id);
    }

    public void addPageData(String id, LauncherPageData pageData){
        mAllPagesData.put(pageData.getId(), pageData);
    }

    @Override
    public String toString() {
        return "LauncherData{" +
                "mCommonData=" + mCommonData +
                ", mAllPagesData=" + mAllPagesData +
                '}';
    }
}
