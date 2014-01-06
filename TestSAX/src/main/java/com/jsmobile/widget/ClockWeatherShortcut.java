package com.jsmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsmobile.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by wangxin on 11/20/13.
 */
public class ClockWeatherShortcut extends LinearLayout implements ClockWeatherService.ClockWeatherListener {
    private boolean mAttached = false;

    private Calendar mCalendar;
    private ClockWeatherService.WeatherInfo mWeatherInfo;

    private ImageView mWeatherIcon;
    private TextView mTemp;
    private TextView mClock;
    private TextView mCityWeather;

    public ClockWeatherShortcut(Context context) {
        super(context);
        this.setOrientation(LinearLayout.HORIZONTAL);

        mWeatherIcon = new ImageView(context);
        LinearLayout.LayoutParams lpweather = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lpweather.gravity = Gravity.BOTTOM;
        mWeatherIcon.setLayoutParams(lpweather);
        mWeatherIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        mWeatherIcon.setImageResource(R.drawable.weather_ico);


        mTemp = new TextView(context);
        LinearLayout.LayoutParams lpTemp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lpTemp.gravity = Gravity.BOTTOM;
        mTemp.setLayoutParams(lpTemp);
        mTemp.setTextSize(14);
        mTemp.setText("null");
        mTemp.setVisibility(View.INVISIBLE);

        LinearLayout ll = new LinearLayout(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.VERTICAL);

        mClock = new TextView(context);
        LayoutParams lpClock = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpClock.gravity = Gravity.RIGHT;
        mClock.setLayoutParams(lpClock);
        mClock.setTextSize(13);

        mCityWeather = new TextView(context);
        LayoutParams lpCity = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpCity.gravity = Gravity.RIGHT;
        mCityWeather.setLayoutParams(lpCity);
        mCityWeather.setTextSize(14);
        mCityWeather.setText("null");
        mCityWeather.setVisibility(View.INVISIBLE);

        ll.addView(mClock);
        ll.addView(mCityWeather);


        this.addView(mWeatherIcon);
        this.addView(mTemp);
        this.addView(ll);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        this.setLayoutParams(param);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d("wangx", "$%^&*()_)^%#$%^&*()_)(*&^%$#$%^&*() onAttachedToWindow(^&%^&()&^%$%^&*(");
        super.onAttachedToWindow();
        if(!mAttached){
            mAttached = true;
            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());

            ClockWeatherService.getInstance().registerClockWeatherListener(this);
        }

        mCalendar = Calendar.getInstance(TimeZone.getDefault());
        updateClock();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d("wangx", "$%^&*()_)^%#$%^&*()_)(*&^%$#$%^&*() onDetachedFromWindow(^&%^&()&^%$%^&*(");
        super.onDetachedFromWindow();
        if(mAttached){
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateClock();
        }
    };

    private void updateClock(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        String result = sdf.format(mCalendar.getTime());
        Log.d("wangx", "updateClock, result: " + result);
        Log.d("wangx", String.format("left is %d, width is %d", this.getLeft(), this.getWidth()));
        Log.d("wangx", String.format("container,width is %d", ((View)this.getParent()).getWidth()));
        mClock.setText(result);
    }

    @Override
    public void onClockWeatherReady(ClockWeatherService.WeatherInfo weatherInfo) {
        mTemp.setText(weatherInfo.tempMax);
        mTemp.setVisibility(View.VISIBLE);
        mCityWeather.setText(weatherInfo.city + " " + weatherInfo.weather);
        mCityWeather.setVisibility(View.VISIBLE);
    }
}
