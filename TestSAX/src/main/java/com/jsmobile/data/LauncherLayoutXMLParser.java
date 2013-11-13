package com.jsmobile.data;

import org.xml.sax.Attributes;

/**
 * Created by wangxin on 10/29/13.
 */
public class LauncherLayoutXMLParser extends XMLParser {
    private LauncherLayout mLauncherLayout;

    public LauncherLayoutXMLParser() {
        super();
        mLauncherLayout = new LauncherLayout();
    }

    public LauncherLayoutXMLParser(LauncherLayout launcherLayout){
        super();
        mLauncherLayout = launcherLayout;
    }

    public LauncherLayout getResult(){
        return mLauncherLayout;
    }

    @Override
    protected void onStartDocument() {
        super.onStartDocument();
    }

    @Override
    protected void onEndDocument() {
        super.onEndDocument();
    }

    @Override
    protected void onStartElement(String name, Attributes attributes) {
        super.onStartElement(name, attributes);
        if(name.equalsIgnoreCase("LauncherLayout")){
            mLauncherLayout.setVersion(attributes.getValue("version"));
        } else if(name.equalsIgnoreCase("PageCommon")){
            mCommonPageLayoutInfo = new LauncherLayout.CommonPageLayoutInfo();
        } else if(name.equalsIgnoreCase("DefaultBackground")){
            mInTagDefaultBackground = true;
        } else if(name.equalsIgnoreCase("FocusNavBgImg")){
            mInTagFocusNavBgImg = true;
        } else if(name.equalsIgnoreCase("PageList")){
            mLauncherLayout.setMaxPageNum(Integer.valueOf(attributes.getValue("MaxPageNumber")));
        } else if(name.equalsIgnoreCase("Page")){
            mPageLayoutInfo = new LauncherLayout.PageLayoutInfo();
            mPageLayoutInfo.id = attributes.getValue("id");
            mCurrentPageId = mPageLayoutInfo.id;
            mPageLayoutInfo.order = Integer.valueOf(attributes.getValue("order"));
            mPageLayoutInfo.width = Integer.valueOf(attributes.getValue("width"));
            mPageLayoutInfo.height = Integer.valueOf(attributes.getValue("height"));
        } else if(name.equalsIgnoreCase("PageName")){
            mInTagPageName = true;
        } else if(name.equalsIgnoreCase("PageBackground")){
            mInTagPageBackground = true;
        } else if(name.equalsIgnoreCase("Shortcuts")){

        } else if(name.equalsIgnoreCase("Shortcut")){
            LauncherLayout.ShortCutRefInfo shortCutRefInfo = new LauncherLayout.ShortCutRefInfo();
            shortCutRefInfo.id = attributes.getValue("id");
            shortCutRefInfo.type = attributes.getValue("type");
            mPageLayoutInfo.addShortcutRefInfo(shortCutRefInfo);
        } else if(name.equalsIgnoreCase("Elements")){

        } else if(name.equalsIgnoreCase("Element")){
            LauncherLayout.ElementLayoutInfo elementLayoutInfo = new LauncherLayout.ElementLayoutInfo();
            elementLayoutInfo.id = attributes.getValue("id");
            elementLayoutInfo.type = attributes.getValue("type");
            elementLayoutInfo.left = Integer.valueOf(attributes.getValue("left"));
            elementLayoutInfo.top = Integer.valueOf(attributes.getValue("top"));
            elementLayoutInfo.width = Integer.valueOf(attributes.getValue("width"));
            elementLayoutInfo.height = Integer.valueOf(attributes.getValue("height"));
            mPageLayoutInfo.addElementLayoutInfo(elementLayoutInfo);
        }
    }

    @Override
    protected void onEndElement(String name) {
        super.onEndElement(name);
        if(name.equalsIgnoreCase("LauncherLayout")){

        } else if(name.equalsIgnoreCase("PageCommon")){
            mLauncherLayout.setCommonLayoutInfo(mCommonPageLayoutInfo);
            mCommonPageLayoutInfo = null;
        } else if(name.equalsIgnoreCase("DefaultBackground")){
            mInTagDefaultBackground = false;
        } else if(name.equalsIgnoreCase("FocusNavBgImg")){
            mInTagFocusNavBgImg = false;
        } else if(name.equalsIgnoreCase("PageList")){

        } else if(name.equalsIgnoreCase("Page")){
            mLauncherLayout.addPageLayoutInfo(mPageLayoutInfo);
            mPageLayoutInfo = null;
        } else if(name.equalsIgnoreCase("PageName")){
            mInTagPageName = false;
        } else if(name.equalsIgnoreCase("PageBackground")){
            mInTagPageBackground = false;
        } else if(name.equalsIgnoreCase("Shortcuts")){

        } else if(name.equalsIgnoreCase("Shortcut")){

        } else if(name.equalsIgnoreCase("Elements")){

        } else if(name.equalsIgnoreCase("Element")){

        }
    }

    @Override
    protected void onText(String text) {
        super.onText(text);
        if(mInTagDefaultBackground){
            mCommonPageLayoutInfo.defaultBg = text;
        } else if(mInTagFocusNavBgImg){
            mCommonPageLayoutInfo.focusNavBg = text;
        } else if(mInTagPageName){
            mPageLayoutInfo.pageName = text;
        } else if(mInTagPageBackground){
            mPageLayoutInfo.pageBg = text;
        }
    }

    private boolean mInTagDefaultBackground;
    private boolean mInTagFocusNavBgImg;
    private boolean mInTagPageName;
    private boolean mInTagPageBackground;
    //当前正在解析的page页面
    private String mCurrentPageId;

    private LauncherLayout.CommonPageLayoutInfo mCommonPageLayoutInfo;
    private LauncherLayout.PageLayoutInfo mPageLayoutInfo;
}
