package com.kenzz.reviewapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangdefa on 07/11/2017.
 * Version 1.0
 * 采用socket 通信服务端
 */

public class TcpService extends Service {


    private ServerSocket mServerSocket;
    private ExecutorService mExecutor;
    private static final String TAG=TcpService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutor = Executors.newFixedThreadPool(4);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mServerSocket==null){
            new Thread(runSocketTask).start();
        }
        return START_STICKY;
    }

    boolean isDestroy=false;
    @Override
    public void onDestroy() {
        isDestroy = true;
        super.onDestroy();
    }

    private Runnable runSocketTask=new Runnable() {
        @Override
        public void run() {
            try {
                mServerSocket = new ServerSocket(60000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!isDestroy){
                try {
                    Socket newSocket = mServerSocket.accept();
                    handleSocket(newSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void handleSocket(final Socket newSocket) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                   BufferedReader reader =new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(newSocket.getOutputStream()),true);
                    while (true) {
                        String msg = reader.readLine();
                        Log.d(TAG,"receive: client-> "+msg);
                        if(TextUtils.isEmpty(msg)){
                            break;
                        }
                        printWriter.println("Server->我收到: "+msg);
                    }

                    reader.close();
                    printWriter.close();
                    newSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
