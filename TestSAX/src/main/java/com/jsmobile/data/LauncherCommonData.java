package com.jsmobile.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangxin on 10/29/13.
 */
public class LauncherCommonData {
    private String version;

    public static final String NAVS_TYPE_TEXT = "text";
    public static final String NAVS_TYPE_IMAGE = "image";
    private String mNavType;
    private Map<String,TextNavInfo> mTextNavsInfo;
    private Map<String,ImageNavInfo> mImageNavsInfo;

    public static class TextNavInfo{
        String id;
        String name;

        @Override
        public String toString() {
            return "TextNavInfo{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class ImageNavInfo{
        String id;
        String img;
        String focusImg;

        @Override
        public String toString() {
            return "ImageNavInfo{" +
                    "id='" + id + '\'' +
                    ", img='" + img + '\'' +
                    ", focusImg='" + focusImg + '\'' +
                    '}';
        }
    }

    private Map<String, ShortCut> mShortCutsInfo;

    public LauncherCommonData() {
        mTextNavsInfo = new HashMap<String, TextNavInfo>();
        mImageNavsInfo = new HashMap<String, ImageNavInfo>();
        mShortCutsInfo = new HashMap<String, ShortCut>();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNavType() {
        return mNavType;
    }

    public void setNavType(String mNavType) {
        this.mNavType = mNavType;
    }

    public TextNavInfo getTextNavInfo(String id) {
        return mTextNavsInfo.get(id);
    }

    public void addTextNavInfo(TextNavInfo mTextNavInfo) {
        this.mTextNavsInfo.put(mTextNavInfo.id, mTextNavInfo);
    }

    public ImageNavInfo getImageNavInfo(String id) {
        return mImageNavsInfo.get(id);
    }

    public void addImageNavInfo(ImageNavInfo mImageNavInfo) {
        this.mImageNavsInfo.put(mImageNavInfo.id, mImageNavInfo);
    }

    public ShortCut getShortCut(String id){
        return mShortCutsInfo.get(id);
    }

    public void addShortCut(ShortCut shortCut){
        mShortCutsInfo.put(shortCut.getId(), shortCut);
    }

    @Override
    public String toString() {
        return "LauncherCommonData{" +
                "version='" + version + '\'' +
                ", mNavType='" + mNavType + '\'' +
                ", mTextNavsInfo=" + mTextNavsInfo +
                ", mImageNavsInfo=" + mImageNavsInfo +
                ", mShortCutsInfo=" + mShortCutsInfo +
                '}';
    }
}
