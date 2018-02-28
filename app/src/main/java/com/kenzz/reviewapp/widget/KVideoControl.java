package com.kenzz.reviewapp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by ken.huang on 2/27/2018.
 * 播放器控制界面
 */

public class KVideoControl extends RelativeLayout implements KVideoView.IKVideoControl {

    private String title;
    private Bitmap mBackgroundImage;

    public static final int STATE_IDL=0;
    public static final int STATE_PREPARED=1;
    public static final int STATE_STARTED=2;
    public static final int STATE_PAUSED=3;
    public static final int STATE_STOPED=4;
    public static final int STATE_ERROR=5;
    public static final int STATE_END=6;


    private int mCurrentState=STATE_IDL;
    private KVideoView mVideoView;

    public KVideoControl(Context context) {
        super(context);
    }

    public KVideoControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KVideoControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start() {
         mVideoView.start();
         mCurrentState=STATE_STARTED;
    }

    @Override
    public void pause() {
        if(mCurrentState==STATE_STARTED){
            mVideoView.pause();
            mCurrentState=STATE_PAUSED;
        }
    }

    @Override
    public void resume() {
      if(mCurrentState==STATE_PAUSED){
          mVideoView.resume();
          mCurrentState=STATE_STARTED;
      }
    }

    @Override
    public void reset() {

    }

    @Override
    public void restart() {
         if(mCurrentState==STATE_END){
             mVideoView.restart();
             mCurrentState=STATE_STARTED;
         }
    }

    @Override
    public boolean setVolume(float volume) {
        if(mCurrentState!=STATE_IDL && mCurrentState!=STATE_ERROR){
            return mVideoView.setVolume(volume);
        }
        return false;
    }

    @Override
    public void seekTo(int position) {
       if(mCurrentState!=STATE_IDL && mCurrentState!=STATE_ERROR && mCurrentState!=STATE_STOPED){
           mVideoView.seekTo(position);
       }
    }

    @Override
    public int getDuration() {
        return mVideoView.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    @Override
    public void onError(Exception e) {
        mCurrentState=STATE_ERROR;
        mVideoView.reset();
        mVideoView.start();
    }

    @Override
    public void onComplete() {
       mCurrentState=STATE_END;
    }

    @Override
    public void onLoadVideo(float percent) {

    }

    public void setVideoView(KVideoView videoView) {
        mVideoView = videoView;
    }
}
