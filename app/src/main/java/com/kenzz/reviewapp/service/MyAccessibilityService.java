package com.kenzz.reviewapp.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;


/**
 * 辅助服务
 */
public class MyAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info=new AccessibilityServiceInfo();
        //监听所以事件
        info.eventTypes=AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType=AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout=100;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {

    }
}
