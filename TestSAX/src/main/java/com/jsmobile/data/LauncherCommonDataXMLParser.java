package com.jsmobile.data;

import org.xml.sax.Attributes;

/**
 * Created by wangxin on 10/29/13.
 */
public class LauncherCommonDataXMLParser extends XMLParser {
    private LauncherCommonData mLauncherCommonData;

    public LauncherCommonDataXMLParser() {
        super();
        mLauncherCommonData = new LauncherCommonData();
    }

    public LauncherCommonDataXMLParser(LauncherCommonData launcherCommonData){
        super();
        mLauncherCommonData = launcherCommonData;
    }

    public LauncherCommonData getResult(){
        return mLauncherCommonData;
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
        if(name.equalsIgnoreCase("LauncherData")){
            mLauncherCommonData.setVersion(attributes.getValue("version"));
        } else if(name.equalsIgnoreCase("Navs")){
            mLauncherCommonData.setNavType(attributes.getValue("type"));
        } else if(name.equalsIgnoreCase("Nav")){
            inTagNav = true;
            if(mLauncherCommonData.getNavType().equals(LauncherCommonData.NAVS_TYPE_TEXT)){
                mTextNavInfo = new LauncherCommonData.TextNavInfo();
                mTextNavInfo.id = attributes.getValue("id");
            } else if(mLauncherCommonData.getNavType().equals(LauncherCommonData.NAVS_TYPE_IMAGE)){
                mImageNavInfo = new LauncherCommonData.ImageNavInfo();
                mImageNavInfo.id = attributes.getValue("id");
            }
        } else if(name.equalsIgnoreCase("Name")){
            inTagName = true;
        } else if(name.equalsIgnoreCase("Shortcuts")){

        } else if(name.equalsIgnoreCase("Shortcut")){
            inTagShortcut = true;
            mShortCut = new ShortCut();
            mShortCut.setId(attributes.getValue("id"));
            mShortCut.setType(attributes.getValue("type"));
            mShortCut.setCanFocus(Boolean.valueOf(attributes.getValue("canfocus")));
        } else if(name.equalsIgnoreCase("ElementAction")){
            mElementAction = new ElementAction();
        } else if(name.equalsIgnoreCase("ActionType")){
            inTagActionType = true;
        } else if(name.equalsIgnoreCase("ActionURL")){
            inTagActionURL = true;
        } else if(name.equalsIgnoreCase("Params")){

        } else if(name.equalsIgnoreCase("Param")){
            inTagParam = true;
            mParamName = attributes.getValue("name");
        } else if(name.equalsIgnoreCase("Img")){
            inTagImg = true;
        } else if(name.equalsIgnoreCase("FocusImg")){
            inTagFocusImg = true;
        }
    }

    @Override
    protected void onEndElement(String name) {
        super.onEndElement(name);
        if(name.equalsIgnoreCase("LauncherData")){

        } else if(name.equalsIgnoreCase("Navs")){

        } else if(name.equalsIgnoreCase("Nav")){
            inTagNav = false;
            if(mLauncherCommonData.getNavType().equals(LauncherCommonData.NAVS_TYPE_IMAGE)){
                mLauncherCommonData.addImageNavInfo(mImageNavInfo);
                mImageNavInfo = null;
            } else if(mLauncherCommonData.getNavType().equals(LauncherCommonData.NAVS_TYPE_TEXT)){
                mLauncherCommonData.addTextNavInfo(mTextNavInfo);
                mTextNavInfo = null;
            }
        } else if(name.equalsIgnoreCase("Name")){
            inTagName = false;
        } else if(name.equalsIgnoreCase("Shortcuts")){

        } else if(name.equalsIgnoreCase("Shortcut")){
            inTagShortcut = false;
            mLauncherCommonData.addShortCut(mShortCut);
            mShortCut = null;
        } else if(name.equalsIgnoreCase("ElementAction")){
            mShortCut.setElementAction(mElementAction);
            mElementAction = null;
        } else if(name.equalsIgnoreCase("ActionType")){
            inTagActionType = false;
        } else if(name.equalsIgnoreCase("ActionURL")){
            inTagActionURL = false;
        } else if(name.equalsIgnoreCase("Params")){

        } else if(name.equalsIgnoreCase("Param")){
            inTagParam = false;
            mParamName = null;
        } else if(name.equalsIgnoreCase("Img")){
            inTagImg = false;
        } else if(name.equalsIgnoreCase("FocusImg")){
            inTagFocusImg = false;
        }
    }

    @Override
    protected void onText(String text) {
        super.onText(text);
        if(inTagNav && inTagName){
            if(mLauncherCommonData.getNavType().equals(LauncherCommonData.NAVS_TYPE_TEXT)){
              mTextNavInfo.name = text;
            }
        } else if(inTagNav && inTagImg){
            if(mLauncherCommonData.getNavType().equals(LauncherCommonData.NAVS_TYPE_IMAGE)){
                mImageNavInfo.img = text;
            }
        } else if(inTagNav && inTagFocusImg){
            if(mLauncherCommonData.getNavType().equals(LauncherCommonData.NAVS_TYPE_IMAGE)){
                mImageNavInfo.focusImg = text;
            }
        } else if(inTagShortcut && inTagName){
            mShortCut.setName(text);
        } else if(inTagParam){
            mElementAction.addParam(mParamName, text);
            mParamName = null;
        } else if(inTagActionType){
            mElementAction.setActionType(Integer.valueOf(text));
        } else if(inTagActionURL){
            mElementAction.setActionUrl(text);
        }
    }

    private boolean inTagNav;
    private boolean inTagName;
    private boolean inTagImg;
    private boolean inTagFocusImg;
    private LauncherCommonData.ImageNavInfo mImageNavInfo;
    private LauncherCommonData.TextNavInfo mTextNavInfo;

    private boolean inTagShortcut;
    private ShortCut mShortCut;

    private boolean inTagActionType;
    private boolean inTagParam;
    private boolean inTagActionURL;
    private ElementAction mElementAction;
    private String mParamName;
}
