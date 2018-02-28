package com.kenzz.reviewapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.IOException;


/**
 * Created by ken.huang on 2/26/2018.
 * 视频播放器控件
 */

public class KVideoView extends FrameLayout implements TextureView.SurfaceTextureListener {

    final static String TAG=KVideoView.class.getSimpleName();
    //播放器
    private MediaPlayer mPlayer;
    private FrameLayout mContainer;
    private TextureView mTextureView;
    private Context mContext;
    //视频URL
    private String mUrl;
    private IKVideoControl mVideoControl;

    public KVideoView(@NonNull Context context) {
        this(context,null);
    }

    public KVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public KVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        init();
    }

    private void init() {
        mContainer=new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        this.addView(mContainer,lp);
    }

    public void setResourceUrl(String url){
        this.mUrl=url;
    }

    private void initPlayer() {
        if(mPlayer==null) {
            mPlayer = new MediaPlayer();
            mPlayer.setScreenOnWhilePlaying(true);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mPlayer.setOnBufferingUpdateListener(mUpdateListener);
            mPlayer.setOnCompletionListener(mCompletionListener);
            mPlayer.setOnErrorListener(mOnErrorListener);
            mPlayer.setOnPreparedListener(mPreparedListener);
            mPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        }

    }
    private MediaPlayer.OnBufferingUpdateListener mUpdateListener=new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.d(TAG,"缓冲-> "+percent);
            if(mVideoControl!=null){
                mVideoControl.onLoadVideo(percent);
            }
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
           if(mVideoControl!=null){
               mVideoControl.onComplete();
           }
        }
    };
    private MediaPlayer.OnErrorListener mOnErrorListener=new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if(mVideoControl!=null){
                mVideoControl.onError(new Exception("Player error: "+what));
            }
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener mPreparedListener=new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
                 mp.start();
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener=new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        }
    };

    private void initTextureView(){
        if(mTextureView==null){
        mTextureView=new TextureView(mContext);
        mTextureView.setSurfaceTextureListener(this);
        }

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openMediaPlayer(surface);
    }

    private void openMediaPlayer(SurfaceTexture surface) {
        try {
            mPlayer.setDataSource(mContext, Uri.parse(mUrl),null);
            mPlayer.setSurface(new Surface(surface));
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private void addTextureView(){
        mContainer.removeView(mTextureView);
        LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mContainer.addView(mTextureView,0,lp);
    }

    public void start(){
        if(mVideoControl==null)
            throw new RuntimeException("before start the videoControl must be set!");
        initPlayer();
        initTextureView();
        addTextureView();
    }

    public void pause(){
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.pause();
        }
    }

    public void resume(){
        if(mPlayer!=null && !mPlayer.isPlaying()){
            mPlayer.start();
        }
    }

    public int getDuration(){
        if(mPlayer!=null){
            try{
            return mPlayer.getDuration();
            }catch (Exception e){
                throw e;
            }
        }
        return 0;
    }

    public int getCurrentPosition(){
        if(mPlayer!=null){
            try{
                return mPlayer.getCurrentPosition();
            }catch (Exception e){
                throw e;
            }
        }
        return 0;
    }

    public boolean setVolume(float volume){
        try {
        if(mPlayer!=null){
            mPlayer.setVolume(volume,volume);
            return true;
         }
        }catch (Exception e){
          e.printStackTrace();
        }
        return false;
    }

    public void seekTo(int position){
        if(mPlayer!=null){
            mPlayer.seekTo(position);
        }
    }

    public void reset(){
        if(mPlayer!=null){
            mPlayer.reset();
        }
    }

    /**
     * 当播放器处于 completed状态调用start则可以重新播放
     */
    public void restart(){
        if(mPlayer!=null){
            mPlayer.start();
        }
    }

    public void setControl(IKVideoControl control){
        this.mVideoControl=control;
        LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        this.mContainer.addView((View) mVideoControl,lp);
        this.mVideoControl.setVideoView(this);
    }

    public interface IKVideoControl{
        void start();
        void pause();
        void resume();
        void reset();
        void restart();

        boolean setVolume(float volume);
        void seekTo(int position);
        int getDuration();
        int getCurrentPosition();

        /**
         * error 状态下必须reset后才能重用，并且还要重新初始化(prepare)才可以播放使用
         * @param e
         */
        void onError(Exception e);
        void onComplete();
        void onLoadVideo(float percent);
        void setVideoView(KVideoView videoView);
    }
}
