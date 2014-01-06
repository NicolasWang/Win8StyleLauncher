package com.jsmobile.widget;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxin on 11/22/13.
 */
public class ClockWeatherService {
    //singletone
    private static ClockWeatherService mService;
    private ClockWeatherService(){

    }
    public static ClockWeatherService getInstance(){
        if(mService == null){
            mService = new ClockWeatherService();
        }
        return mService;
    }

    public static final class WeatherInfo{
        String city;
        String cityid;
        String tempMax;
        String tempMin;
        String weather;

        @Override
        public String toString() {
            return "WeatherInfo{" +
                    "city='" + city + '\'' +
                    ", cityid='" + cityid + '\'' +
                    ", tempMax='" + tempMax + '\'' +
                    ", tempMin='" + tempMin + '\'' +
                    ", weather='" + weather + '\'' +
                    '}';
        }
    }
    private WeatherInfo mWeatherInfo;

    public static interface ClockWeatherListener{
        void onClockWeatherReady(WeatherInfo weatherInfo);
    }

    List<ClockWeatherListener> mClockWeatherListeners = new ArrayList<ClockWeatherListener>();
    public void registerClockWeatherListener(ClockWeatherListener clockWeatherListener){
        if(mWeatherInfo != null){
            clockWeatherListener.onClockWeatherReady(mWeatherInfo);
            return;
        }
        mClockWeatherListeners.add(clockWeatherListener);
        if(mClockWeatherListeners.size() == 1){
            new PlaceAsyncTask().execute();
        }
    }

    class PlaceAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Log.d("wangx", "PlaceAsyncTask doInBackground");
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL("http://61.4.185.48:81/g/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(60000);
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                sb = new StringBuilder();
                char[] buf = new char[4096];
                while(true){
                    int size = isr.read(buf);
                    if(size < 0)break;
                    sb.append(buf, 0, size);
                }
            } catch (Exception e) {
                Log.d("wangx", "PlaceAsyncTask Exception: " + e.toString());
                e.printStackTrace();
            }
            Log.d("wangx", "city code: " + sb.toString());
            return convert(sb.toString());
        }

        @Override
        protected void onPostExecute(String city) {
            Log.d("wangx", "PlaceAsyncTask onPostExecute " + city);
            if(city == null){
                new PlaceAsyncTask().execute();
                return;
            }
            new WeatherAsyncTask().execute(city);
        }

        private String convert(String str){
            if(str == null || str.isEmpty() || str.length() < 4)return null;
            StringBuilder sb = new StringBuilder(str);
            String result = sb.substring(sb.indexOf("id=") + 3, sb.indexOf(";", sb.indexOf("id=")));

            return result.replace(";", "");
        }
    }

    class WeatherAsyncTask extends AsyncTask<String, Void, WeatherInfo>{

        @Override
        protected WeatherInfo doInBackground(String... citys) {
            Log.d("wangx", "WeatherAsyncTask doInBackground");
            StringBuilder weatherStr = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("http://www.weather.com.cn/data/cityinfo/");
                sb.append(citys[0]);
                sb.append(".html");
                Log.d("wangx", "city info: " + sb.toString());
                URL url = new URL(sb.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                char[] buf = new char[4096];
                while(true){
                    int size = isr.read(buf);
                    if(size < 0)break;
                    weatherStr.append(buf, 0, size);
                }

                Log.d("wangx", "json str: " + weatherStr);
                mWeatherInfo = new WeatherInfo();
                JSONObject jsonObject = new JSONObject(weatherStr.toString());
                JSONObject weather = jsonObject.getJSONObject("weatherinfo");
                mWeatherInfo.city = weather.getString("city");
                mWeatherInfo.cityid = weather.getString("cityid");
                mWeatherInfo.tempMax = weather.getString("temp1");
                mWeatherInfo.tempMin = weather.getString("temp2");
                mWeatherInfo.weather = weather.getString("weather");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mWeatherInfo;
        }

        @Override
        protected void onPostExecute(WeatherInfo weatherInfo) {
            Log.d("wangx", "WeatherAsyncTask onPostExecute");
            Log.d("wangx", weatherInfo.toString());
            for(ClockWeatherListener listener: mClockWeatherListeners){
                listener.onClockWeatherReady(weatherInfo);
            }
        }
    }

}
