package com.jsmobile.data;

import org.xml.sax.Attributes;


/**
 * Created by wangxin on 10/28/13.
 */
public class LauncherXMLParser extends XMLParser {
    private LauncherTemplateFileConfigs mConfigs;

    public LauncherXMLParser(LauncherTemplateFileConfigs config){
        super();
        mConfigs = config;
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
        if(name.equals("Layout")){
            int len = attributes.getLength();
            for(int i = 0; i < len; i++){
                if(attributes.getLocalName(i).equals("version")){
                    mConfigs.setLauncherLayoutVersion(attributes.getValue(i));
                } else if(attributes.getLocalName(i).equals("file")){
                    mConfigs.setLauncherLayoutFile(attributes.getValue(i));
                }
            }
        } else if(name.equals("CommonData")){
            int len = attributes.getLength();
            for(int i = 0; i < len; i++){
                if(attributes.getLocalName(i).equals("version")){
                    mConfigs.setLauncherCommonDataVersion(attributes.getValue(i));
                } else if(attributes.getLocalName(i).equals("file")){
                    mConfigs.setLauncherCommonDataFile(attributes.getValue(i));
                }
            }
        } else if(name.equals("Page")){
            LauncherTemplateFileConfigs.LauncherPageFile pageFile = new LauncherTemplateFileConfigs.LauncherPageFile();
            int len = attributes.getLength();
            for(int i = 0; i < len; i++){
                if(attributes.getLocalName(i).equals("version")){
                    pageFile.version = attributes.getValue(i);
                } else if(attributes.getLocalName(i).equals("file")){
                    pageFile.fileName = attributes.getValue(i);
                } else if(attributes.getLocalName(i).equals("id")){
                    pageFile.id = attributes.getValue(i);
                }
            }

            mConfigs.putLauncherPageFile(pageFile);
        }
    }

    @Override
    protected void onEndElement(String name) {
        super.onEndElement(name);
    }

    @Override
    protected void onText(String text) {
        super.onText(text);
    }
}
