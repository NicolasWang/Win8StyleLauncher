package com.jsmobile.app;

import android.app.Application;
import android.util.Log;

import com.jsmobile.data.LauncherData;
import com.jsmobile.data.LauncherLayout;
import com.jsmobile.data.LauncherTemplateFileConfigs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by wangxin on 11/5/13.
 */
public class JsMobileApplication extends Application {
    private LauncherTemplateFileConfigs mConfig;
    private LauncherLayout mLauncherLayout;
    private LauncherData mLauncherData;

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
}
