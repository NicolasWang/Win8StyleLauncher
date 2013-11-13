package com.jsmobile.data;

import org.xml.sax.Attributes;

/**
 * Created by wangxin on 10/29/13.
 */
public class LauncherPageDataXMLParser extends XMLParser {
    private LauncherPageData mLauncherPageData;

    public LauncherPageDataXMLParser() {
        super();
        mLauncherPageData = new LauncherPageData();
    }

    public LauncherPageDataXMLParser(LauncherPageData launcherPageData){
        super();
        mLauncherPageData = launcherPageData;
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
            mLauncherPageData.setId(attributes.getValue("id"));
            mLauncherPageData.setVersion(attributes.getValue("version"));
        } else if(name.equalsIgnoreCase("Shortcuts")){

        } else if(name.equalsIgnoreCase("Shortcut")){
            inTagShortcut = true;
            mShortcut = new ShortCut();
            mShortcut.setId(attributes.getValue("id"));
            mShortcut.setType(attributes.getValue("type"));
            mShortcut.setCanFocus(Boolean.valueOf(attributes.getValue("canfocus")));
        } else if(name.equalsIgnoreCase("Name")){
            inTagName = true;
        } else if(name.equalsIgnoreCase("Elements")){

        } else if(name.equalsIgnoreCase("Element")){
            mElement = new Element();
            mElement.setId(attributes.getValue("id"));
            mElement.setType(attributes.getValue("type"));
            mElement.setCanFocus(Boolean.valueOf(attributes.getValue("canfocus")));
        } else if(name.equalsIgnoreCase("ElementData")){
            inTagElementData = true;
            mElementData = new ElementData();
            mElementData.setOrder(Integer.valueOf(attributes.getValue("order")));
        } else if(name.equalsIgnoreCase("ElementName")){
            inTagElementName = true;
        } else if(name.equalsIgnoreCase("ElementDesc")){
            inTagElementDesc = true;
        } else if(name.equalsIgnoreCase("ContentURL")){
            inTagContentURL = true;
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
        }
    }

    @Override
    protected void onEndElement(String name) {
        super.onEndElement(name);
        if(name.equalsIgnoreCase("LauncherData")){

        } else if(name.equalsIgnoreCase("Shortcuts")){

        } else if(name.equalsIgnoreCase("Shortcut")){
            inTagShortcut = false;
            mLauncherPageData.addShortCut(mShortcut);
            mShortcut = null;
        } else if(name.equalsIgnoreCase("Name")){
            inTagName = false;
        } else if(name.equalsIgnoreCase("Elements")){

        } else if(name.equalsIgnoreCase("Element")){
            mLauncherPageData.addElement(mElement);
            mElement = null;
        } else if(name.equalsIgnoreCase("ElementData")){
            inTagElementData = false;
            mElement.addElementData(mElementData);
            mElementData = null;
        } else if(name.equalsIgnoreCase("ElementName")){
            inTagElementName = false;
        } else if(name.equalsIgnoreCase("ElementDesc")){
            inTagElementDesc = false;
        } else if(name.equalsIgnoreCase("ContentURL")){
            inTagContentURL = false;
        } else if(name.equalsIgnoreCase("ElementAction")){
            if(inTagShortcut){
                mShortcut.setElementAction(mElementAction);
                mElementAction = null;
            } else if(inTagElementData){
                mElementData.setElementAction(mElementAction);
                mElementAction = null;
            }
        } else if(name.equalsIgnoreCase("ActionType")){
            inTagActionType = false;
        } else if(name.equalsIgnoreCase("ActionURL")){
            inTagActionURL = false;
        } else if(name.equalsIgnoreCase("Params")){

        } else if(name.equalsIgnoreCase("Param")){
            inTagParam = false;
            mParamName = null;
        }
    }

    @Override
    protected void onText(String text) {
        super.onText(text);
        if(inTagShortcut && inTagName){
            mShortcut.setName(text);
        } else if(inTagActionType){
            mElementAction.setActionType(Integer.valueOf(text));
        } else if(inTagActionURL){
            mElementAction.setActionUrl(text);
        } else if(inTagParam){
            mElementAction.addParam(mParamName, text);
        } else if(inTagElementName){
            mElementData.setElementName(text);
        } else if(inTagElementDesc){
            mElementData.setElementDesc(text);
        } else if(inTagContentURL){
            mElementData.setContentUrl(text);
        }
    }

    private boolean inTagShortcut;
    private boolean inTagElementName;
    private boolean inTagElementDesc;
    private boolean inTagContentURL;
    private boolean inTagActionType;
    private boolean inTagActionURL;
    private boolean inTagName;
    private boolean inTagParam;
    private boolean inTagElementData;

    private ShortCut mShortcut;
    private ElementAction mElementAction;
    private String mParamName;
    private Element mElement;
    private ElementData mElementData;
}
