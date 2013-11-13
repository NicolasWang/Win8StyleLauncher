package com.jsmobile.app;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
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
import com.jsmobile.widget.ElementLayout;
import com.jsmobile.widget.ImageElement;
import com.jsmobile.widget.Navigator;
import com.jsmobile.widget.PageContainer;
import com.jsmobile.widget.PageView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private SAXParser mParser;
    private XMLReader mReader;

    private TextView mTextView1;
    private TextView mTextView2;

    private ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        JsMobileApplication app = (JsMobileApplication) getApplication();
//        app.setConfig(LauncherTemplateFileConfigs.getInstance());
//        XMLParser xmlParser = new LauncherXMLParser(app.getConfig());
//        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher)));
//
//        app.setLauncherLayout(new LauncherLayout());
//        xmlParser = new LauncherLayoutXMLParser(app.getLauncherLayout());
//        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher_layout)));
//
//        LauncherData launcherData = new LauncherData();
//        LauncherCommonData commonData = new LauncherCommonData();
//        LauncherPageData pageData = new LauncherPageData();
//        xmlParser = new LauncherCommonDataXMLParser(commonData);
//        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher_data_common)));
//        xmlParser = new LauncherPageDataXMLParser(pageData);
//        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher_data_page_myhome)));
//
//        launcherData.addPageData(pageData.getId(), pageData);
//        launcherData.setCommonData(commonData);
//        app.setLauncherData(launcherData);




//        ElementLayout page1 = new ElementLayout(this);
//        page1.addView(newImageElement(1,1,1,1));
//        page1.addView(newImageElement(1,2,1,1));
//        page1.addView(newImageElement(1,3,1,1));
//        page1.addView(newImageElement(2,1,3,1));
//        page1.addView(newImageElement(5,1,1,1));
//
//        ElementLayout page2 = new ElementLayout(this);
//        page2.addView(newImageElement(1,3,1,1));
//        page2.addView(newImageElement(2,3,3,1));
//        page2.addView(newImageElement(5,3,1,1));
//        page2.addView(newImageElement(5,2,1,1));
//        page2.addView(newImageElement(5,1,1,1));
//
//        mViewAnimator = new ViewAnimator(this);
//        mViewAnimator.addView(page1);
//        mViewAnimator.addView(page2);
//
//        mViewAnimator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        mViewAnimator.setBackgroundResource(R.drawable.bg_img);

//        setContentView(mViewAnimator);

        parseRes();
        inflateFromConfig();
        mNavigator = new Navigator(this);

        mView = new FrameLayout(this);
        mView.setBackgroundResource(R.drawable.bg_img);

        mView.addView(mViewAnimator);
        mView.addView(mNavigator);

        setContentView(mView);

        FocusController.getInstance().setTargetViews(mView, mViewAnimator, mNavigator);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mNavigator.requestFocus();
//            }
//        }, 10);
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        mNavigator.requestFocus();
//    }

    private Handler mHandler = new Handler();

    private FrameLayout mView;
    private Navigator mNavigator;
    private ViewAnimator mViewAnimator;

    public ImageElement newImageElement(int left, int top, int width, int height){
        Element element = new Element();
        element.setCanFocus(true);

        LauncherLayout.ElementLayoutInfo elementLayoutInfo = new LauncherLayout.ElementLayoutInfo();
        elementLayoutInfo.left = left;
        elementLayoutInfo.top = top;
        elementLayoutInfo.width = width;
        elementLayoutInfo.height = height;

        ImageElement imageElement = new ImageElement(this, elementLayoutInfo, element);

        ElementLayout.LayoutParams elp = new ElementLayout.LayoutParams(width, height);
        elp.elementleft = left;
        elp.elementtop = top;

        imageElement.setLayoutParams(elp);

        return imageElement;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d("wangx", "textview onfocuschange FrameLayout getFocusedChild: " + mView.getFocusedChild());
//        Log.d("wangx", "textview onfocuschange viewanimator getFocusedChild: " + mViewAnimator.getFocusedChild());
//        Log.d("wangx", "textview onfocuschange navigator getFocusedChild: " + mNavigator.getFocusedChild());
//        Log.d("wangx", "textview onfocuschange root getFocusedChild: " + mNavigator.getRootView());
//        Log.d("wangx", "ScrollView: " + mNavigator.mScrollView.getWidth() + "," + mNavigator.mScrollView.getHeight() +
//        ", LinearLayout: " + mNavigator.mLinearLayout.getWidth() + "," + mNavigator.mLinearLayout.getHeight());

        if(FocusController.getInstance().onKeyEvent(keyCode, event))return true;

        if(keyCode == KeyEvent.KEYCODE_1){
            mViewAnimator.setInAnimation(this, R.anim.rightinanimation);
            mViewAnimator.setOutAnimation(this, R.anim.leftoutanimation);
            mViewAnimator.showPrevious();
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_2){
            mViewAnimator.setInAnimation(this, R.anim.leftinanimation);
            mViewAnimator.setOutAnimation(this, R.anim.rightoutanimation);
            mViewAnimator.showNext();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void parseRes(){
        JsMobileApplication app = (JsMobileApplication) getApplication();
        app.setConfig(LauncherTemplateFileConfigs.getInstance());
        XMLParser xmlParser = new LauncherXMLParser(app.getConfig());
        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher)));

        app.setLauncherLayout(new LauncherLayout());
        xmlParser = new LauncherLayoutXMLParser(app.getLauncherLayout());
        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher_layout)));

        LauncherData launcherData = new LauncherData();

        LauncherCommonData commonData = new LauncherCommonData();
        xmlParser = new LauncherCommonDataXMLParser(commonData);
        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher_data_common)));

        LauncherPageData pageData1 = new LauncherPageData();
        xmlParser = new LauncherPageDataXMLParser(pageData1);
        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher_data_page_myhome)));

        LauncherPageData pageData2 = new LauncherPageData();
        xmlParser = new LauncherPageDataXMLParser(pageData2);
        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher_data_page_movie)));

        LauncherPageData pageData3 = new LauncherPageData();
        xmlParser = new LauncherPageDataXMLParser(pageData3);
        xmlParser.parse(new InputSource(getResources().openRawResource(R.raw.launcher_data_page_app)));

        launcherData.addPageData(pageData1.getId(), pageData1);
        launcherData.addPageData(pageData2.getId(), pageData2);
        launcherData.addPageData(pageData3.getId(), pageData3);
        launcherData.setCommonData(commonData);

        app.setLauncherData(launcherData);

        Log.d("wangx", "launcherPageData1:  " + pageData1);
//        Log.d("wangx", "launcherPageData2:  " + pageData2);
//        Log.d("wangx", "launcherPageData3:  " + pageData3);
    }

    private void inflateFromConfig(){
        JsMobileApplication app = (JsMobileApplication) getApplication();
        LauncherLayout launcherLayout = app.getLauncherLayout();
        LauncherData launcherData = app.getLauncherData();

        mViewAnimator = new PageContainer(getApplicationContext());

        int pageNum = launcherLayout.getMaxPageNum();
        for(int i = 0; i< pageNum; i++){
            ElementLayout page = new ElementLayout(getApplicationContext());
            String id = launcherLayout.getIdByOrder(i);
            Log.d("wangx", "page id:" + id);
            LauncherLayout.PageLayoutInfo pageLayoutInfo = launcherLayout.getPageLayoutInfo(id);
            Set<Map.Entry<String, LauncherLayout.ElementLayoutInfo>> elements = pageLayoutInfo.getAllElements().entrySet();
            Iterator<Map.Entry<String, LauncherLayout.ElementLayoutInfo>> iterator = elements.iterator();
            while(iterator.hasNext()){
                Map.Entry<String, LauncherLayout.ElementLayoutInfo> elementLayoutInfo = iterator.next();
                if(elementLayoutInfo.getValue().type.equalsIgnoreCase("image")){
                    Log.d("wangx", "elementLayoutInfo.getKey=" + elementLayoutInfo.getKey());
                    if(launcherData.getPageData(id) == null)Log.d("wangx", "launcherData.getPageData is null");
                    if(launcherData.getPageData(id).getElement(elementLayoutInfo.getKey()) == null)
                        Log.d("wangx", "launcherData.getPageData.getElement is null");
                    if(launcherData.getPageData(id).getElement(elementLayoutInfo.getKey()) == null)Log.d("wangx", "xxx is null");

                    ImageElement imageElement = new ImageElement(getApplicationContext(), elementLayoutInfo.getValue(),
                            launcherData.getPageData(id).getElement(elementLayoutInfo.getKey()));
                    page.addView(imageElement);
                }
            }

            page.setTag(pageLayoutInfo.getPageId());

            PageView pageView = new PageView(this);
            pageView.addElementLayout(page);
            pageView.setTag(pageLayoutInfo.getPageId());

            mViewAnimator.addView(pageView, pageLayoutInfo.getPageOrder());
        }
    }
    
}
