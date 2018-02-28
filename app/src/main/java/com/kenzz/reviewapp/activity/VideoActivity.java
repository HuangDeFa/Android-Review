package com.kenzz.reviewapp.activity;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.SurfaceView;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.widget.KVideoControl;
import com.kenzz.reviewapp.widget.KVideoView;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 音视频学习
 */
public class VideoActivity extends BaseActivity {

    private SurfaceView mSurfaceView;
    private KVideoView mKVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mSurfaceView=findViewById(R.id.video);
        mKVideoView=findViewById(R.id.k_video_view);
        mKVideoView.setControl(new KVideoControl(this));
        mKVideoView.setResourceUrl("https://us.sinaimg.cn/003jdkYLjx07cBHyYtuw010f0100r7va0k01.mp4");
        mKVideoView.start();
    }

    /**
     * 视频播放的两个重要类:{@link MediaExtractor 读取; MediaCodec 编解码}
     * 这属于硬解码技术，兼容性较差
     * @param url 视频文件地址
     */
    private void loadVideo(String url) throws IOException {
        MediaExtractor extractor=null;
        MediaCodec mediaCodec=null;


        extractor=new MediaExtractor();
        extractor.setDataSource(url);
        int videoTrack = getVideoTrack(extractor);
        if(videoTrack<0){
          throw new IOException("can not find video in the path->"+url);
        }
        try {
            extractor.selectTrack(videoTrack);
            MediaFormat mediaFormat = extractor.getTrackFormat(videoTrack);
            String decodeType = mediaFormat.getString(MediaFormat.KEY_MIME);

            mediaCodec = MediaCodec.createDecoderByType(decodeType);
            mediaCodec.configure(mediaFormat, mSurfaceView.getHolder().getSurface(), null, 0);
            mediaCodec.start();
            doExtractor(extractor,mediaCodec,videoTrack);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(mediaCodec!=null){
                mediaCodec.stop();
                mediaCodec.release();
            }

            if(extractor!=null){
                extractor.release();
            }
        }

    }

    /**
     * TODO: 循环读取文件输入到编解码器。并输出到surfaceView
     * @param extractor
     * @param decoder
     * @param trackIndex
     */
    private void doExtractor(MediaExtractor extractor, MediaCodec decoder, int trackIndex) {

        boolean isInput=false,isOutPut=false;
        while (true) {
            if (!isInput) {
                int i = decoder.dequeueInputBuffer(1000);
                ByteBuffer byteBuffer=null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    byteBuffer=decoder.getInputBuffer(i);
                }else {
                    byteBuffer = decoder.getInputBuffers()[i];
                    byteBuffer.clear();
                }

                int size = extractor.readSampleData(byteBuffer, 0);
                if (size <= 0) {
                    isInput = true;
                }
                decoder.queueInputBuffer(i, 0, size, extractor.getSampleTime(), 0);
                extractor.advance();
            }

            if(!isOutPut){
                MediaCodec.BufferInfo info=new MediaCodec.BufferInfo();

                int index = decoder.dequeueOutputBuffer(info, 1000);
            }

        }


    }

    /**
     * 一般来说 音视频文件最少有音轨和视频轨道。假如有多种配音则音轨增加
     * @param extractor
     * @return
     */
    private int getVideoTrack(MediaExtractor extractor) {
        //获取所有轨道
        int trackCount = extractor.getTrackCount();
        for(int index=0;index<trackCount;index++){
            //获取轨道的Format
            MediaFormat trackFormat = extractor.getTrackFormat(index);
            //读取对应轨道的Mime值
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if(mime.startsWith("video/")){
                return index;
            }
        }

        return -1;
    }

    /**
     * 音视频合成 {@link MediaMuxer} 最多只能写入一个视频轨和一个音频轨
     * @param videoSource 视频路径
     * @param audioSource 音频路径
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void MixVideoAndAudio(String videoSource, String audioSource){
        //获取文件合成后写入的路径
        String outPutPath=getExternalCacheDir().getAbsolutePath()+"/videoSample.mp4";
        try {
            MediaMuxer mediaMuxer=new MediaMuxer(outPutPath,MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            MediaExtractor videoExtractor=new MediaExtractor();
            videoExtractor.setDataSource(videoSource);
            int trackIndex=-1;
            for(int i=0;i<videoExtractor.getTrackCount();i++){
                MediaFormat trackFormat = videoExtractor.getTrackFormat(i);
                if(trackFormat.getString(MediaFormat.KEY_MIME).startsWith("video/")){
                    trackIndex=i;
                }
            }
            if(trackIndex!=-1) {
                videoExtractor.selectTrack(trackIndex);
                mediaMuxer.addTrack(videoExtractor.getTrackFormat(trackIndex));
            }

            MediaExtractor audioExtractor=new MediaExtractor();
            audioExtractor.setDataSource(audioSource);
            int  audioTrackIndex=-1;
            for(int i=0;i<audioExtractor.getTrackCount();i++){
                MediaFormat trackFormat = audioExtractor.getTrackFormat(i);
                if(trackFormat.getString(MediaFormat.KEY_MIME).startsWith("audio/")){
                    audioTrackIndex=i;
                }
            }
            if(audioTrackIndex!=-1) {
                audioExtractor.selectTrack(audioTrackIndex);
                mediaMuxer.addTrack(audioExtractor.getTrackFormat(audioTrackIndex));
            }
            //开始合成
            mediaMuxer.start();
            //视频写入
            if(trackIndex!=-1){
                MediaCodec.BufferInfo info=new MediaCodec.BufferInfo();
                ByteBuffer buffer=ByteBuffer.allocate(100*1024);
                while (true){
                int i = videoExtractor.readSampleData(buffer, 0);
                if(i<0){
                    break;
                 }
                 info.offset=0;
                 info.size=i;
                 info.flags=MediaExtractor.SAMPLE_FLAG_SYNC;
                 info.presentationTimeUs=videoExtractor.getSampleTime();
                 mediaMuxer.writeSampleData(trackIndex,buffer,info);
                 //移动到下一帧
                 videoExtractor.advance();
                }
            }
            //音频写入
            if(audioTrackIndex!=-1){
                MediaCodec.BufferInfo info=new MediaCodec.BufferInfo();
                ByteBuffer buffer=ByteBuffer.allocate(100*1024);
                while (true){
                    int i = audioExtractor.readSampleData(buffer, 0);
                    if(i<0){
                        break;
                    }
                    info.offset=0;
                    info.size=i;
                    info.flags=MediaExtractor.SAMPLE_FLAG_SYNC;
                    info.presentationTimeUs=audioExtractor.getSampleTime();
                    mediaMuxer.writeSampleData(audioTrackIndex,buffer,info);
                    //移动到下一帧
                    audioExtractor.advance();
                }
            }

            //释放Extractor
            videoExtractor.release();
            audioExtractor.release();

            //释放MediaMuxer
            mediaMuxer.stop();
            mediaMuxer.release();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
