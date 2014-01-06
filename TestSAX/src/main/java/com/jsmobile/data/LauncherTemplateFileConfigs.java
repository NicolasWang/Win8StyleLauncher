package com.jsmobile.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wangxin on 10/28/13.
 */
public class LauncherTemplateFileConfigs {
    public static class LauncherLayoutFile{
       public String version;
       public String fileName;
    }

    public static class LauncherCommonDataFile{
        public String version;
        public String fileName;
    }

    public static class LauncherPageFile{
        public String id;
        public String version;
        public String fileName;
    }

//    private static LauncherTemplateFileConfigs mConfigs;
    public LauncherTemplateFileConfigs(){
        mLayoutFile = new LauncherLayoutFile();
        mCommonDataFile = new LauncherCommonDataFile();
        mPagesFile = new HashMap<String, LauncherPageFile>();
    }
//    public static LauncherTemplateFileConfigs getInstance(){
//        if(mConfigs == null){
//            mConfigs = new LauncherTemplateFileConfigs();
//        }
//        return mConfigs;
//    }

    //deprecated?
    public LauncherLayoutFile getLayoutFile(){
        return mLayoutFile;
    }

    //deprecated?
    public LauncherCommonDataFile getCommonDataFile(){
        return mCommonDataFile;
    }

    public void setLauncherLayoutVersion(String version){
        mLayoutFile.version = version;
    }

    public void setLauncherLayoutFile(String file){
        mLayoutFile.fileName = file;
    }

    public String getLauncherLayoutVersion(){
        return mLayoutFile.version;
    }

    public String getLauncherLayoutFile(){
        return mLayoutFile.fileName;
    }

    public void setLauncherCommonDataVersion(String version){
        mCommonDataFile.version = version;
    }

    public void setLauncherCommonDataFile(String file){
        mCommonDataFile.fileName = file;
    }

    public String getLauncherCommonDataVersion(){
        return mCommonDataFile.version;
    }

    public String getLauncherCommonDataFile(){
        return mCommonDataFile.fileName;
    }

    public void putLauncherPageFile(LauncherPageFile file){
        mPagesFile.put(file.id, file);
    }

    public LauncherPageFile getLauncherPageFile(String id){
        return mPagesFile.get(id);
    }

    public Map<String, LauncherPageFile> getAllLauncherPages(){
        return mPagesFile;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LauncherTemplateFileConfigs{" +
                "mLayoutFile=" + "[version: " + mLayoutFile.version + ",file:" + mLayoutFile.fileName + "]"
                + ", mCommonDataFile=" + "[version:" + mCommonDataFile.version + ",file:" + mCommonDataFile.fileName + "]");

        sb.append(", mPagesFile=[");
        Iterator<LauncherPageFile> iterator = mPagesFile.values().iterator();
        while(iterator.hasNext()){
            LauncherPageFile file = iterator.next();
            sb.append("(id:" + file.id + ",version:" + file.version + ",file:" + file.fileName + ")");
        }
        sb.append("]}");
        return sb.toString();
    }

    private LauncherLayoutFile mLayoutFile;
    private LauncherCommonDataFile mCommonDataFile;
    //key is id
    private Map<String, LauncherPageFile> mPagesFile;
}
