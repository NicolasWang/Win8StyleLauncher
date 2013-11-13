package com.jsmobile.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangxin on 10/29/13.
 */
public class LauncherLayout {
    private String version;
    private CommonPageLayoutInfo mCommonLayoutInfo;

    private int mMaxPageNum;
    private Map<String, PageLayoutInfo> mAllPagesLayoutInfo; //id--[layout data]
    private List<String> mAllPagesId;//order as index, order--id

    public LauncherLayout() {
        mAllPagesLayoutInfo = new HashMap<String, PageLayoutInfo>();
        mAllPagesId = new ArrayList<String>();
    }

    public CommonPageLayoutInfo getCommonLayoutInfo() {
        return mCommonLayoutInfo;
    }

    public void setCommonLayoutInfo(CommonPageLayoutInfo mCommonLayoutInfo) {
        this.mCommonLayoutInfo = mCommonLayoutInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getMaxPageNum() {
        return mMaxPageNum;
    }

    public void setMaxPageNum(int mMaxPageNum) {
        this.mMaxPageNum = mMaxPageNum;
    }

    public PageLayoutInfo getPageLayoutInfo(String id){
        return mAllPagesLayoutInfo.get(id);
    }

    public void addPageLayoutInfo(PageLayoutInfo pageLayoutInfo){
        mAllPagesLayoutInfo.put(pageLayoutInfo.id, pageLayoutInfo);
        mAllPagesId.add(pageLayoutInfo.order, pageLayoutInfo.id);
    }

    public String getIdByOrder(int order){
        return mAllPagesId.get(order);
    }

    public static class CommonPageLayoutInfo{
        String defaultBg;
        String focusNavBg;

        @Override
        public String toString() {
            return "CommonPageLayoutInfo{" +
                    "defaultBg='" + defaultBg + '\'' +
                    ", focusNavBg='" + focusNavBg + '\'' +
                    '}';
        }
    }

    public static class PageLayoutInfo{
        String id;
        int order;
        int width;
        int height;

        String pageName;
        String pageBg;

        int elementNum;
        int shortcutNum;

        private Map<String, ShortCutRefInfo> mShortCuts;
        private Map<String, ElementLayoutInfo> mElements;

        public PageLayoutInfo() {
            mShortCuts = new HashMap<String, ShortCutRefInfo>();
            mElements = new HashMap<String, ElementLayoutInfo>();
        }

        public void addShortcutRefInfo(ShortCutRefInfo shortCutRefInfo){
            mShortCuts.put(shortCutRefInfo.id, shortCutRefInfo);
            shortcutNum = mShortCuts.size();
        }

        public ShortCutRefInfo getShortcutRefInfo(String id){
            return mShortCuts.get(id);
        }

        public void addElementLayoutInfo(ElementLayoutInfo elementLayoutInfo){
            mElements.put(elementLayoutInfo.id, elementLayoutInfo);
            elementNum = mElements.size();
        }

        public ElementLayoutInfo getElementLayoutInfo(String id){
            return mElements.get(id);
        }

        public String getPageName(){
            return pageName;
        }

        public String getPageBg(){
            return pageBg;
        }

        public int getPageOrder(){
            return order;
        }

        public String getPageId(){
            return id;
        }

        public int getElementNum(){
            return elementNum;
        }

        public int getShortcutNum(){
            return shortcutNum;
        }

        public Map<String, ElementLayoutInfo> getAllElements(){
            return mElements;
        }

        public Map<String, ShortCutRefInfo> getAllShortcuts(){
            return mShortCuts;
        }

        @Override
        public String toString() {
            return "PageLayoutInfo{" +
                    "id='" + id + '\'' +
                    ", order=" + order +
                    ", width=" + width +
                    ", height=" + height +
                    ", pageName='" + pageName + '\'' +
                    ", pageBg='" + pageBg + '\'' +
                    ", mShortCuts=" + mShortCuts +
                    ", mElements=" + mElements +
                    '}';
        }
    }

    public static class ShortCutRefInfo{
        String id;
        String type;

        @Override
        public String toString() {
            return "ShortCutRefInfo{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    public static class ElementLayoutInfo{
        public String id;
        public String type;
        public int left;
        public int top;
        public int width;
        public int height;

        @Override
        public String toString() {
            return "ElementLayoutInfo{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", left=" + left +
                    ", top=" + top +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LauncherLayout{" +
                "version='" + version + '\'' +
                ", mCommonLayoutInfo=" + mCommonLayoutInfo +
                ", mMaxPageNum=" + mMaxPageNum +
                ", mAllPagesLayoutInfo=" + mAllPagesLayoutInfo +
                '}';
    }
}
