package com.kenzz.reviewapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.util.ToastUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CameraActivity extends BaseActivity {

    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private SurfaceView mSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置Activity的window为安全窗口 无法截屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_camera);
        mSurfaceView = findViewById(R.id.surface_view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void checkCamera() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = mCameraManager.getCameraIdList();
            for (String cameraId : cameraIdList) {
                CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
                Integer integer = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (integer == CameraCharacteristics.LENS_FACING_FRONT)
                    continue;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(@NonNull CameraDevice camera) {
                        mCameraDevice=camera;
                        takePreview();
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice camera) {
                     camera.close();
                    }

                    @Override
                    public void onError(@NonNull CameraDevice camera, int error) {
                        ToastUtil.showShortToast(CameraActivity.this,"open camera error!");
                    }
                }, new Handler() {

                });
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void takePreview() {
        try {
            final CaptureRequest.Builder captureRequest = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequest.addTarget(mSurfaceView.getHolder().getSurface());
            captureRequest.addTarget(previewReader.getSurface());
            mCameraDevice.createCaptureSession(Arrays.asList(previewReader.getSurface(), mSurfaceView.getHolder().getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if(mCameraDevice==null) return;
                    mCameraCaptureSession=session;
                    try {
                        captureRequest.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        mCameraCaptureSession.capture(captureRequest.build(),null,null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            },new Handler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private ImageReader previewReader=ImageReader.newInstance(1000,500, ImageFormat.JPEG,2);
    {
        previewReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireLatestImage();
                Image.Plane[] planes = image.getPlanes();
                List<Byte>  byteList=new ArrayList<>();
                for (Image.Plane plane : planes) {
                     ByteBuffer buffer = plane.getBuffer();
                     if(buffer.hasArray()){
                         for (byte b : buffer.array()) {
                             byteList.add(b);
                         }
                     }
                }
            }
        },null);
    }

}
