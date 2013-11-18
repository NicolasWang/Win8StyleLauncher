package com.jsmobile.widget;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.jsmobile.data.Element;
import com.jsmobile.data.LauncherLayout;

/**
 * Created by wangxin on 11/13/13.
 */
public class VideoElement extends BaseElement implements MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, SurfaceHolder.Callback, Handler.Callback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;

    private MediaPlayer mMediaPlayer;
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private ListElement mLinkElement;

    private static final int MSG_OPEN = 1;
    private static final int MSG_START = 2;
    private static final int MSG_STOP = 3;
    private static final int MSG_RELEASE = 4;

    public VideoElement(Context context, LauncherLayout.ElementLayoutInfo elementLayoutInfo, Element element) {
        super(context, elementLayoutInfo, element);
    }

    @Override
    protected void initElement() {
        this.setPadding(ElementLayout.DEFAULT_ELEMENT_MARGIN, ElementLayout.DEFAULT_ELEMENT_MARGIN,
                ElementLayout.DEFAULT_ELEMENT_MARGIN, ElementLayout.DEFAULT_ELEMENT_MARGIN);

        mSurfaceView = new SurfaceView(this.getContext());
        mSurfaceView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSurfaceView.getHolder().addCallback(this);

        mSurfaceView.setVisibility(View.GONE);

        this.addView(mSurfaceView);

        mHandlerThread = new HandlerThread("mediaplayer", Thread.MIN_PRIORITY);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), this);

        Log.d("wangx", "ui thread id: " + Thread.currentThread().getId());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        ViewGroup parent = (ViewGroup) this.getParent();
        mLinkElement = (ListElement) parent.findViewWithTag(mElement.getLinkElementId());
    }

    private boolean isAutoPlay(){
        return this.mElement.isAutoPlay();
    }

    private void openPlayer(String uri){
        Message msg = mHandler.obtainMessage(MSG_OPEN);
        msg.obj = uri;
        mHandler.sendMessageDelayed(msg, 1000);
    }

    private void handleOpenPlayer(String uri){
        Log.d("wangx", "handleOpenPlayer " + uri);
        if(uri == null || mHolder == null){
            return;
        }

        handleReleasePlayer();

        try{
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnInfoListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);

            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.setDisplay(mHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (Exception e){
            this.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        }
    }

    private void startPlayer(){
        Message msg = mHandler.obtainMessage(MSG_START);
        mHandler.sendMessage(msg);
    }

    private void handleStartPlayer(){
        Log.d("wangx", "handleStartPlayer");
        if(mMediaPlayer != null){
            mMediaPlayer.start();
        }
    }

    private void stopPlayer(){
        Message msg = mHandler.obtainMessage(MSG_STOP);
        mHandler.removeMessages(MSG_OPEN);
        mHandler.sendMessage(msg);
    }

    private void handleStopPlayer(){
        Log.d("wangx", "handleStopPlayer ");
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void releasePlayer(){
        Message msg = mHandler.obtainMessage(MSG_RELEASE);
        mHandler.removeMessages(MSG_OPEN);
        mHandler.sendMessage(msg);
    }

    private void handleReleasePlayer(){
        Log.d("wangx", "handleReleasePlayer ");
        if(mMediaPlayer != null){
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.d("wangx", "onVisibilityChanged " + visibility + ", thread id:" + Thread.currentThread().getId());
        if(visibility == View.GONE){
            releasePlayer();
        } else if(visibility == View.VISIBLE){
            openPlayer(VideoElement.this.getElementData(0).getContentUrl());
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        Log.d("wangx", String.format("onVideoSizeChanged width=%d, height=%d", mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight()));
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("wangx", "onPrepared thread id: " + Thread.currentThread().getId());
        startPlayer();
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("wangx", "surfaceCreated thread id:" + Thread.currentThread().getId());
        mHolder = surfaceHolder;
        mHolder.setFixedSize(VideoElement.this.getWidth(), VideoElement.this.getHeight());
        mSurfaceView.measure(VideoElement.this.getWidth(), VideoElement.this.getHeight());
        mSurfaceView.layout(0, 0, VideoElement.this.getWidth(), VideoElement.this.getHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.d("wangx", String.format("surfaceChanged width=%d, heihgt=%d", VideoElement.this.getWidth(), VideoElement.this.getHeight()));

//            if(isAutoPlay()){
        openPlayer(VideoElement.this.getElementData(0).getContentUrl());
//            }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d("wangx", "surfaceDestroyed");
        mHolder = null;
        releasePlayer();
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
         case MSG_OPEN:
            handleOpenPlayer((String) message.obj);
            break;
         case MSG_START:
            handleStartPlayer();
            break;
         case MSG_STOP:
            handleStopPlayer();
            break;
         case MSG_RELEASE:
            handleReleasePlayer();
            break;
        }
        return true;
    }
}
