package com.jsmobile.app;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import com.jsmobile.data.Element;
import com.jsmobile.data.LauncherCommonData;
import com.jsmobile.data.LauncherCommonDataXMLParser;
import com.jsmobile.data.LauncherData;
import com.jsmobile.data.LauncherLayout;
import com.jsmobile.data.LauncherLayoutXMLParser;
import com.jsmobile.data.LauncherPageData;
import com.jsmobile.data.LauncherPageDataXMLParser;
import com.jsmobile.data.LauncherTemplateFileConfigs;
import com.jsmobile.data.LauncherXMLParser;
import com.jsmobile.data.XMLParser;
import com.jsmobile.widget.ClockWeatherShortcut;
import com.jsmobile.widget.CustomElement;
import com.jsmobile.widget.ElementLayout;
import com.jsmobile.widget.ImageElement;
import com.jsmobile.widget.ListElement;
import com.jsmobile.widget.Navigator;
import com.jsmobile.widget.PageContainer;
import com.jsmobile.widget.PageView;
import com.jsmobile.widget.ShortcutContainer;
import com.jsmobile.widget.VideoElement;
import com.jsmobile.widget.WidgetElement;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;

public class MainActivity extends Activity implements Handler.Callback{
    private static final String TAG = "MainActivity";

    private HandlerThread mParseThread;
    private Handler mParseHandler;

    private Handler mHandler;

    private static final int MSG_PARSE = 1;
    private static final int MSG_INFLATE_RENDER = 2;
    private static final int MSG_START_CHECKUPDATE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init(){
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.loading);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(iv);

        mParseThread = new HandlerThread("Parse");
        mParseThread.start();
        mParseHandler = new Handler(mParseThread.getLooper(), new ParseInflateCallback());

        mHandler = new Handler(this);

        Message msg = mParseHandler.obtainMessage(MSG_PARSE);
        mParseHandler.sendMessage(msg);
    }

    private FrameLayout mView;
    private Navigator mNavigator;
    private ViewAnimator mViewAnimator;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(FocusController.getInstance().onKeyEvent(keyCode, event))return true;
        return super.onKeyDown(keyCode, event);
    }

    private void parse() throws FileNotFoundException {
        //parse
        JsMobileApplication app = (JsMobileApplication) getApplication();
        app.setConfig(new LauncherTemplateFileConfigs());
        XMLParser xmlParser = new LauncherXMLParser(app.getConfig());
        xmlParser.parse(new InputSource(new FileInputStream(getLauncherConfigFilePath())));

        app.setLauncherLayout(new LauncherLayout());
        xmlParser = new LauncherLayoutXMLParser(app.getLauncherLayout());
        xmlParser.parse(new InputSource(new FileInputStream(getFilesDir().getAbsolutePath() + "/current/" + app.getConfig().getLayoutFile().fileName)));

        LauncherData launcherData = new LauncherData();

        LauncherCommonData commonData = new LauncherCommonData();
        xmlParser = new LauncherCommonDataXMLParser(commonData);
        xmlParser.parse(new InputSource(new FileInputStream(getFilesDir().getAbsolutePath() + "/current/" + app.getConfig().getCommonDataFile().fileName)));
        launcherData.setCommonData(commonData);

        Iterator<Map.Entry<String, LauncherTemplateFileConfigs.LauncherPageFile>> iterator = app.getConfig().getAllLauncherPages().entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, LauncherTemplateFileConfigs.LauncherPageFile> entry = iterator.next();
            LauncherPageData pageData = new LauncherPageData();
            xmlParser = new LauncherPageDataXMLParser(pageData);
            xmlParser.parse(new InputSource(new FileInputStream(getFilesDir().getAbsolutePath() + "/current/" + entry.getValue().fileName)));
            launcherData.addPageData(pageData.getId(), pageData);
        }

        app.setLauncherData(launcherData);
    }

    private String getLauncherConfigFilePath(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getFilesDir().getAbsolutePath());
        sb.append("/current/launcher_");

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        sb.append(metrics.heightPixels);
        sb.append("p.xml");
        return sb.toString();
    }

    private boolean isLauncherConfigExist(){
        File file = new File(this.getFilesDir().getAbsolutePath() + "/current/launcher_720p.xml");
        if(file.exists())
            return true;
        return false;
    }

    private boolean configUpdateStarted = false;
    private void handleParse(){
        if(isLauncherConfigExist()){
            try {
                parse();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Message msg = mHandler.obtainMessage(MSG_INFLATE_RENDER);
            mHandler.sendMessage(msg);

            if(!configUpdateStarted){
                Message msg2 = mHandler.obtainMessage(MSG_START_CHECKUPDATE);
                mHandler.sendMessage(msg2);
                configUpdateStarted = true;
            }
        } else {
            ConfigUpdateManager.getInstance(this).startCheckPeriodic(new ConfigUpdateCallback());
            configUpdateStarted = true;
        }
    }

    private void handleInflateAndRender(){
        //inflate
        JsMobileApplication app = (JsMobileApplication) this.getApplication();
        LauncherLayout launcherLayout = app.getLauncherLayout();
        LauncherData launcherData = app.getLauncherData();

        mViewAnimator = new PageContainer(getApplicationContext());

        int pageNum = launcherLayout.getPageNum();
        for(int i = 0; i< pageNum; i++){
            ElementLayout page = new ElementLayout(getApplicationContext());
            String id = launcherLayout.getIdByOrder(i + 1);//TODO
            Log.d("wangx", "page id:" + id);
            LauncherLayout.PageLayoutInfo pageLayoutInfo = launcherLayout.getPageLayoutInfo(id);
            Set<Map.Entry<String, LauncherLayout.ElementLayoutInfo>> elements = pageLayoutInfo.getAllElements().entrySet();
            Iterator<Map.Entry<String, LauncherLayout.ElementLayoutInfo>> iterator2 = elements.iterator();
            while(iterator2.hasNext()){
                Map.Entry<String, LauncherLayout.ElementLayoutInfo> elementLayoutInfo = iterator2.next();
                if(elementLayoutInfo.getValue().type.equalsIgnoreCase("image")){
                    ImageElement imageElement = new ImageElement(getApplicationContext(), elementLayoutInfo.getValue(),
                            launcherData.getPageData(id).getElement(elementLayoutInfo.getKey()));
                    imageElement.setTag(elementLayoutInfo.getKey());
                    page.addView(imageElement);
                } else if(elementLayoutInfo.getValue().type.equalsIgnoreCase("list")){
                    ListElement listElement = new ListElement(getApplicationContext(), elementLayoutInfo.getValue(),
                            launcherData.getPageData(id).getElement(elementLayoutInfo.getKey()));
                    listElement.setTag(elementLayoutInfo.getKey());
                    page.addView(listElement);
                } else if(elementLayoutInfo.getValue().type.equalsIgnoreCase("video")){
                    VideoElement videoElement = new VideoElement(getApplicationContext(), elementLayoutInfo.getValue(),
                            launcherData.getPageData(id).getElement(elementLayoutInfo.getKey()));
                    videoElement.setTag(elementLayoutInfo.getKey());
                    page.addView(videoElement);
                } else if(elementLayoutInfo.getValue().type.equalsIgnoreCase("widget")){
                    WidgetElement widgetElement = new WidgetElement(getApplicationContext(), elementLayoutInfo.getValue(),
                            launcherData.getPageData(id).getElement(elementLayoutInfo.getKey()));
                    widgetElement.setTag(elementLayoutInfo.getKey());
                    page.addView(widgetElement);
                } else if(elementLayoutInfo.getValue().type.equalsIgnoreCase("custom")){
                    CustomElement customElement = new CustomElement(getApplicationContext(), elementLayoutInfo.getValue(),
                            launcherData.getPageData(id).getElement(elementLayoutInfo.getKey()));
                    customElement.setTag(elementLayoutInfo.getKey());
                    page.addView(customElement);
                }
            }

            page.setTag(pageLayoutInfo.getPageId());

            PageView pageView = new PageView(this);
            pageView.addElementLayout(page);
            pageView.setTag(pageLayoutInfo.getPageId());

            ShortcutContainer container = new ShortcutContainer(this);
            ClockWeatherShortcut clockweather = new ClockWeatherShortcut(this);
            container.addView(clockweather);

            pageView.addShortcutContainer(container);

            mViewAnimator.addView(pageView, pageLayoutInfo.getPageOrder() - 1);
        }

        mNavigator = new Navigator(this);

        mView = new FrameLayout(this);
        mView.setBackgroundResource(R.drawable.bg_img);

        mView.addView(mViewAnimator);
        mView.addView(mNavigator);

        setContentView(mView);
        FocusController.getInstance().setTargetViews(mView, mViewAnimator, mNavigator);
    }

    class ConfigUpdateCallback implements ConfigUpdateManager.Callback{
        @Override
        public void notifyUpdateEnd() {
            Log.d(TAG, "notifyUpdateEnd");
            File currentDir = new File(MainActivity.this.getFilesDir().getAbsolutePath() + "/current");
            ConfigUpdateManager.deleteFile(currentDir);

            File newDir = new File(MainActivity.this.getFilesDir().getAbsolutePath() + "/new");
            newDir.renameTo(new File(MainActivity.this.getFilesDir().getAbsolutePath() + "/current"));

            Message msg = mParseHandler.obtainMessage(MSG_PARSE);
            mParseHandler.sendMessage(msg);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case MSG_INFLATE_RENDER:
                handleInflateAndRender();
                return true;
            case MSG_START_CHECKUPDATE:
                ConfigUpdateManager.getInstance(this).startCheckPeriodic(new ConfigUpdateCallback());
                return true;
        }
        return false;
    }

    class ParseInflateCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case MSG_PARSE:
                    handleParse();
                    return true;
            }
            return false;
        }
    }
}
