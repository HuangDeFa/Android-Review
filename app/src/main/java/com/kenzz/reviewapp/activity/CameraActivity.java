package com.kenzz.reviewapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.SurfaceView;

import com.kenzz.reviewapp.R;

public class CameraActivity extends BaseActivity {

    CameraDevice mCameraDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

    }

    @TargetApi(21)
    private void checkCamera(){
        CameraManager  cameraManager =
                (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            //获取摄像头列表
            String[] cameraIdList = cameraManager.getCameraIdList();
            for (String cameraId : cameraIdList) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
