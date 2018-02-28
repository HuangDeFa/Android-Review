package com.kenzz.reviewapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CameraActivity extends BaseActivity {

    final static String TAG=CameraActivity.class.getSimpleName();

    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;

    private Handler maniHandler;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;//Camera state: Waiting for the focus to be locked.
    private static final int STATE_WAITING_PRECAPTURE = 2;//Camera state: Waiting for the exposure to be pre capture state.
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;//Camera state: Waiting for the exposure state to be something other than precapture.
    private static final int STATE_PICTURE_TAKEN = 4;//Camera state: Picture was taken.
    private static final int MAX_PREVIEW_WIDTH = 1920;//Max preview width that is guaranteed by Camera2 API
    private static final int MAX_PREVIEW_HEIGHT = 1080;//Max preview height that is guaranteed by Camera2 API
    private int mState = STATE_PREVIEW;//{#see mCaptureCallback}The current state of camera state for taking pictures.

    private Size mPreviewSize;
    //从屏幕旋转转换为JPEG方向
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置Activity的window为安全窗口 无法截屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_camera);
        mSurfaceView = findViewById(R.id.surface_view);
        mSurfaceHolder=mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }else {
                        checkCamera();
                    }
                }else {
                    checkCamera();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                 if(mCameraDevice!=null) mCameraDevice.close();
            }
        });
        findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockFocus();
            }
        });
    }

    /**
     * 开启后台线程，因为cameraSession的打开是个耗时的操作
     */
    private void startBackgroundThread(){
        mBackgroundThread=new HandlerThread("Camera backgroundThread");
        mBackgroundThread.start();
        mBackgroundHandler=new Handler(mBackgroundThread.getLooper());
        maniHandler=new Handler(Looper.getMainLooper());
    }

    private void stopBackgroundThread(){
        if(mBackgroundThread==null)return;
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread=null;
            mBackgroundHandler=null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //监听Camera的开启状态
    private CameraDevice.StateCallback mStateCallback=new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
                mCameraDevice=camera;
                Log.d(TAG,"Camera opened!");
                openCameraPreview();
        }


        @SuppressLint("NewApi")
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
                camera.close();
                mCameraDevice=null;
                Log.d(TAG,"Camera disconnected!");
        }

        @SuppressLint("NewApi")
        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
               camera.close();
               mCameraDevice=null;
               Log.d(TAG,"Camera error!");
        }
    };

    /**
     * {@link CameraCaptureSession.CaptureCallback } 相机捕获的结果回调
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback=new CameraCaptureSession.CaptureCallback() {

        @SuppressLint("NewApi")
        private void process(CaptureResult result){
            switch (mState){
                case STATE_PREVIEW:
                    //do nothing
                    break;
                case STATE_WAITING_LOCK:{
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if(afState==null){
                     captureStillPicture();
                    }else if(CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED==afState||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED==afState
                            ){
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if(aeState==null || aeState==CaptureResult.CONTROL_AE_STATE_CONVERGED){
                            mState=STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        }else {
                            runPrecaptureSequence();
                        }
                    }
                }
                break;
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                }
                    break;
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
           process(partialResult);
        }

        @SuppressLint("NewApi")
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            process(result);
        }
    };


    @SuppressLint("NewApi")
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCameraCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 捕获静态图片
     */
    @SuppressLint("NewApi")
    private void captureStillPicture() {
           try{
               if(mCameraDevice==null)return;
               CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
               captureBuilder.addTarget(mImageReader.getSurface());
               captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
               //设置旋转角度
               // int rotation = getWindowManager().getDefaultDisplay().getRotation();
               //captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,getOrientation(rotation));

               CameraCaptureSession.CaptureCallback captureCallback=new CameraCaptureSession.CaptureCallback() {
                   @Override
                   public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                      unlockFocus();
                   }
               };
               mCameraCaptureSession.stopRepeating();
               mCameraCaptureSession.capture(captureBuilder.build(),captureCallback,null);
           } catch (CameraAccessException e) {
               e.printStackTrace();
           }
    }

    @SuppressLint("NewApi")
    private void lockFocus() {

        try {
            if(mCameraCaptureSession==null)return;
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            mState = STATE_WAITING_LOCK;
            mCameraCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    @SuppressLint("NewApi")
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            if (mCameraCaptureSession == null) {
                return;
            }
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            mCameraCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview，重新预览
            mState = STATE_PREVIEW;
            mCameraCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
        } catch ( CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Integer getOrientation(int rotation) {
        return null;
    }

    //开启相机预览
    @SuppressLint("NewApi")
    private void openCameraPreview() {
        if (mSurfaceHolder == null) throw new AssertionError();
        try {
            mPreviewRequestBuilder=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if(mCameraDevice==null)return;
                    mCameraCaptureSession=session;
                    try {
                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        mPreviewRequest = mPreviewRequestBuilder.build();
                        mCameraCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void checkCamera() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        startBackgroundThread();
        try {
            String[] cameraIdList = mCameraManager.getCameraIdList();
            for (String cameraId : cameraIdList) {
                CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;
                Size[] preSize = map.getOutputSizes(SurfaceHolder.class);//预览尺寸
                Size[] picSize = map.getOutputSizes(ImageFormat.JPEG); //成像尺寸

                Integer integer = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (integer!=null && integer == CameraCharacteristics.LENS_FACING_FRONT)
                    continue;
                    mCameraManager.openCamera(cameraId,mStateCallback, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            checkCamera();
        }
    }


    private ImageReader mImageReader =ImageReader.newInstance(1000,500, ImageFormat.JPEG,2);
    {
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                Image.Plane plane = image.getPlanes()[0];
                ByteBuffer buffer = plane.getBuffer();
                byte[] bytes =new byte[buffer.remaining()];
                buffer.get(bytes);
                try {
                    FileOutputStream fos = new FileOutputStream(new File(getExternalCacheDir(),"take_pic.jpg"));
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },mBackgroundHandler);
    }

    /**
     * 将图片文件添加到图库
     * @param file
     */
    private void addPicToGallery(File file){
        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }


    @SuppressLint("NewApi")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCameraDevice!=null){
            mCameraDevice.close();
        }
        stopBackgroundThread();
    }
}
