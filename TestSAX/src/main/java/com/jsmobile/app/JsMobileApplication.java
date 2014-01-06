package com.jsmobile.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.jsmobile.data.LauncherData;
import com.jsmobile.data.LauncherLayout;
import com.jsmobile.data.LauncherTemplateFileConfigs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wangxin on 11/5/13.
 */
public class JsMobileApplication extends Application {
    private LauncherTemplateFileConfigs mConfig;
    private LauncherLayout mLauncherLayout;
    private LauncherData mLauncherData;

    private static final String DEFAULT_CONFIG_PATH = "http://112.4.17.14:8101/PortalManager/LauncherXML/";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Jsmobile", "fileDir: " + getFilesDir());
        Log.d("Jsmobile", "obbDir: " + getObbDir());
        Log.d("Jsmobile", "cacheDir: " + getCacheDir());
        Log.d("Jsmobile", "externalcacheDir: " + getExternalCacheDir());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3)
                .defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(config);
    }

    public LauncherTemplateFileConfigs getConfig() {
        return mConfig;
    }

    public void setConfig(LauncherTemplateFileConfigs mConfig) {
        this.mConfig = mConfig;
    }

    public LauncherData getLauncherData() {
        return mLauncherData;
    }

    public void setLauncherData(LauncherData mLauncherData) {
        this.mLauncherData = mLauncherData;
    }

    public LauncherLayout getLauncherLayout() {
        return mLauncherLayout;
    }

    public void setLauncherLayout(LauncherLayout mLauncherLayout) {
        this.mLauncherLayout = mLauncherLayout;
    }

    public String getConfigPath(){
        String ret = null;
        SharedPreferences pref = this.getSharedPreferences("config", 0);
        ret = pref.getString("config_path", DEFAULT_CONFIG_PATH);
        return ret;
    }

    public void setConfigPath(String str){
        SharedPreferences pref = this.getSharedPreferences("config", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("config_path", str);
        editor.commit();
    }
}
