package com.jsmobile.app;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.jsmobile.data.LauncherTemplateFileConfigs;
import com.jsmobile.data.LauncherXMLParser;
import com.jsmobile.data.XMLParser;

import com.jsmobile.data.LauncherTemplateFileConfigs.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wangxin on 1/2/14.
 */
public class ConfigUpdateManager implements Handler.Callback {
    static final String TAG = "ConfigUpdate";

    private Context mContext;
    private HandlerThread mThread;
    private Handler mHandler;

    private static final int CHECK_UPDATE_PERIOD = 60 * 60 *1000;
    public static final String LOGIN_AUTH_URL = "http://112.4.17.13:8101/AuthenticationURL.html";
    private static final int MSG_GET_PATH = 1;
    private static final int MSG_CHECK_UPDATE = 2;

    private String mConfigPath = null;

    //singleton
    private static ConfigUpdateManager mUpdateManager;
    private ConfigUpdateManager(Context context) {
        mContext = context;
        mThread = new HandlerThread("CheckConfigUpdate");
        mThread.start();
        mHandler = new Handler(mThread.getLooper(), this);
    }
    public static ConfigUpdateManager getInstance(Context context){
        synchronized (ConfigUpdateManager.class){
            if(mUpdateManager == null){
                mUpdateManager = new ConfigUpdateManager(context);
            }
        }
        return mUpdateManager;
    }

    private Callback mCallback;
    public void startCheckPeriodic(Callback callback){
        mCallback = callback;
        if(isUserTokenAvailable()){
            Message msg = null;
            if(mConfigPath != null && !(mConfigPath.isEmpty())){
                msg = mHandler.obtainMessage(MSG_CHECK_UPDATE);
            } else {
                msg = mHandler.obtainMessage(MSG_GET_PATH);
            }
            mHandler.sendMessage(msg);
        } else {

        }


    }

    public void stopCheck(){

    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case MSG_GET_PATH:
                getConfigFilePath();
                break;
            case MSG_CHECK_UPDATE:
                checkUpdate();
                break;
        }
        return true;
    }

    private void getConfigFilePath(){
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            URL url = new URL(LOGIN_AUTH_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            is = urlConnection.getInputStream();
            isr = new InputStreamReader(is);

            char[] buf = new char[4096];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while((len = isr.read(buf, 0, 4096)) >= 0){
                sb.append(buf, 0, len);
            }

            String content = sb.toString();

            JSONObject jsonObject = new JSONObject(content);
            mConfigPath = jsonObject.getString("LauncherXMLLocaltion");

            JsMobileApplication app = (JsMobileApplication) mContext.getApplicationContext();
            app.setConfigPath(mConfigPath);
            Log.d("wangxin", "setConfigPath:" + mConfigPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                isr.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Message msg = mHandler.obtainMessage(MSG_CHECK_UPDATE);
            mHandler.sendMessage(msg);
        }
    }

    private int getResolutionHeight(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    private void checkUpdate(){
        StringBuilder sb = new StringBuilder();
        sb.append(mConfigPath);
        sb.append("launcher_");
        sb.append(getResolutionHeight());
        sb.append("p.xml");

        InputStream is = null;
        InputStreamReader isr = null;
        try {
            URL url = new URL(sb.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            is = urlConnection.getInputStream();
            isr = new InputStreamReader(is);

            char[] buf = new char[4096];
            int len = 0;
            StringBuilder content = new StringBuilder();
            while((len = isr.read(buf, 0, 4096)) >= 0){
                content.append(buf, 0, len);
            }
            Log.d(TAG, "launcher_720p content: " + content.toString());

            LauncherTemplateFileConfigs launcherConfig = new LauncherTemplateFileConfigs();
            XMLParser parser = new LauncherXMLParser(launcherConfig);
            parser.parse(new InputSource(new StringReader(content.toString())));

            JsMobileApplication app = (JsMobileApplication) mContext.getApplicationContext();
            LauncherTemplateFileConfigs currentConfig = app.getConfig();

            if(isConfigFileUpdated(launcherConfig)){
                downloadNewConfigFiles(launcherConfig);
                if(mCallback != null){
                    mCallback.notifyUpdateEnd();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Message msg = mHandler.obtainMessage(MSG_CHECK_UPDATE);
            mHandler.sendMessageDelayed(msg, CHECK_UPDATE_PERIOD);
        }
    }

    private boolean isUserTokenAvailable(){
        return true;
    }

    private boolean isConfigFileUpdated(LauncherTemplateFileConfigs launcherConfig){
        JsMobileApplication app = (JsMobileApplication) mContext.getApplicationContext();
        LauncherTemplateFileConfigs currentConfig = app.getConfig();

        if(currentConfig == null)return true;

        if(Long.valueOf(launcherConfig.getLayoutFile().version) > Long.valueOf(currentConfig.getLayoutFile().version)){
            return true;
        }
        if(Long.valueOf(launcherConfig.getCommonDataFile().version) > Long.valueOf(currentConfig.getCommonDataFile().version)){
            return true;
        }
        Map<String, LauncherPageFile> allPagesMap = launcherConfig.getAllLauncherPages();
        Map<String, LauncherPageFile> currentPagesMap = currentConfig.getAllLauncherPages();
        Iterator<Map.Entry<String, LauncherPageFile>> iterator = allPagesMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, LauncherPageFile> entry = iterator.next();
            if(currentPagesMap.containsKey(entry.getKey())){
                if(Long.valueOf(entry.getValue().version) > Long.valueOf(currentPagesMap.get(entry.getKey()).version)){
                    return true;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    private void downloadNewConfigFiles(LauncherTemplateFileConfigs launcherConfig){
        File dir = new File(mContext.getFilesDir().getAbsolutePath() + "/new");
        deleteFile(dir);

        dir.mkdir();

        File desDir = new File(dir.getAbsolutePath() + "/" + getResolutionHeight() + "p");
        desDir.mkdir();

        downloadFile(new File(dir.getAbsolutePath() + "/launcher_" + getResolutionHeight() + "p.xml"), mConfigPath + "/launcher_" + getResolutionHeight() + "p.xml");
        downloadFile(new File(dir.getAbsolutePath() + "/" + launcherConfig.getLayoutFile().fileName),
                mConfigPath + "/" + launcherConfig.getLayoutFile().fileName);
        downloadFile(new File(dir.getAbsolutePath() + "/" + launcherConfig.getCommonDataFile().fileName),
                mConfigPath + "/" + launcherConfig.getCommonDataFile().fileName);

        Iterator<Map.Entry<String,LauncherPageFile>> iterator = launcherConfig.getAllLauncherPages().entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, LauncherPageFile> entry = iterator.next();
            downloadFile(new File(dir.getAbsolutePath() + "/" + entry.getValue().fileName),
                    mConfigPath + "/" + entry.getValue().fileName);
        }
    }

    private void downloadFile(File destination, String url){
        InputStream is = null;

        try {
            FileOutputStream fos = new FileOutputStream(destination);

            URL httpUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) httpUrl.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            is = urlConnection.getInputStream();

            byte[] buf = new byte[4096];
            int len = -1;
            while((len = is.read(buf, 0, 4096)) >= 0){
                fos.write(buf, 0, len);
            }

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(File file){
        if(file.exists()){
            if(file.isDirectory()){
                File[] listFiles = file.listFiles();
                for(File listFile: listFiles){
                    if(listFile.isDirectory())
                        deleteFile(listFile);
                    else
                        listFile.delete();
                }
            } else {
                file.delete();
            }
        }
    }

    public static interface Callback{
        void notifyUpdateEnd();
    }

}
