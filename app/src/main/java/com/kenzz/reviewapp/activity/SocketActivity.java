package com.kenzz.reviewapp.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.service.TcpService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SocketActivity extends AppCompatActivity {

    final static String TAG=SocketActivity.class.getSimpleName();
    Unbinder butterKnifebind;
    @BindView(R.id.socket_tv_msg)
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        butterKnifebind = ButterKnife.bind(this);
        //连接service
        startService(new Intent(this, TcpService.class));
        bindSocket();
    }

    private PrintWriter mPrintWriter;
    private void bindSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("localhost",60000);
                    if(socket.isConnected()){
                        mPrintWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
                        Log.d(TAG,"socket is connected");
                        while (true){
                            InputStream inputStream = socket.getInputStream();
                            if(inputStream==null || inputStream.available()==0){
                                SystemClock.sleep(500);
                            }
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            String msg =  reader.readLine();
                            while (!TextUtils.isEmpty(msg)) {
                                Log.d(TAG, "From Server-> " +msg);
                                reader.readLine();
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int times;
    @OnClick({R.id.socket_btn_send})
    public void onClick(){
        if(mPrintWriter!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mPrintWriter.println("hello server "+times++);
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        butterKnifebind.unbind();
    }
}
