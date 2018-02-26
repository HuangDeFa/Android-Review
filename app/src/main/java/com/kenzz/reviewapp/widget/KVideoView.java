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
import android.view.Surface;
import android.view.TextureView;
import android.widget.FrameLayout;

import java.io.IOException;

/**
 * Created by ken.huang on 2/26/2018.
 * 视频播放器控件
 */

public class KVideoView extends FrameLayout implements TextureView.SurfaceTextureListener {

    //播放器
    private MediaPlayer mPlayer;
    private FrameLayout mContainer;
    private TextureView mTextureView;
    private Context mContext;
    //视频URL
    private String mUrl;

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

        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    };
    private MediaPlayer.OnErrorListener mOnErrorListener=new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
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
        mPlayer.prepareAsync();
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
        initPlayer();
        initTextureView();
        addTextureView();
    }
}
