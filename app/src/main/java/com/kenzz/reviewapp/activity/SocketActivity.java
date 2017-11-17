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

/**
 * 利用Socket进行IPC即跨进程通讯。Socket是传输层的编程实现可以指定TCP/UDP通信协议，这是个全双工的通信。
 * 即系客户端接受客户端信息同时也能发送信息给服务端 反过来服务端也一样。
 * 首先 服务端创建ServerSocket 监听指定的端口。等待客户端的连接(因为这会是个阻塞的操作需要放到子线程去实现)，
 * 当客户端有连接时返回一个新的socket以供和客户端进行通信。这里当一个新的socket到来时接收客户端的信息或者发送
 * 信息给客户端也是个阻塞过程因此也要在子线程中去实现。
 *
 * 作为客户端首先要创建一个连接到服务端的socket。当socket连接上之后创建输入输出流以用来读取信息和发送信息。当然这些操作
 * 也是阻塞的同样需要在子线程操作。
 */
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
                            //等待接受服务端信息
                            InputStream inputStream = socket.getInputStream();
                            if(inputStream==null || inputStream.available()==0){
                                //没有信息则休眠
                                SystemClock.sleep(500);
                                continue;
                            }
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            String msg =  reader.readLine();
                            while (!TextUtils.isEmpty(msg)) {
                                Log.d(TAG, "From Server-> " +msg);
                               msg = reader.readLine();
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
                    //发送信息给服务端
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
