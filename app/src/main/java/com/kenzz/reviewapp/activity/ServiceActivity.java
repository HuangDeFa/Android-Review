package com.kenzz.reviewapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kenzz.reviewapp.R;
import com.kenzz.reviewapp.aidl.Book;
import com.kenzz.reviewapp.aidl.IBookManager;
import com.kenzz.reviewapp.aidl.IUpdateBookListener;
import com.kenzz.reviewapp.service.BookService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ServiceActivity extends BaseActivity {

    private static final String TAG = ServiceActivity.class.getSimpleName();

    @InjectView(R.id.service_btn_add)
    Button mButton;
    ExecutorService mExecutorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.inject(this);
        Intent intent = new Intent(this,BookService.class);
        startService(intent);
        bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);
       mExecutorService =  Executors.newFixedThreadPool(5);
    }

    @OnClick({R.id.service_btn_add})
    public void  onClick(View view){
        if(mBookManager!=null){
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int size = mBookManager.getBooks().size();
                        mBookManager.addBook(new Book(size+1,"Android lession "+(size+1),100));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private IBookManager mBookManager;

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBookManager =  IBookManager.Stub.asInterface(service);
            try {
                mBookManager.registerUpdateBookListener(mUpdateBookListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //调用服务，当前线程会被挂起直到服务返回结果。因此最好放在子线程操纵
            Log.d(TAG,"onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          //重新绑定
            Intent intent=new Intent(ServiceActivity.this, BookService.class);
            startService(intent);
            bindService(intent,mServiceConnection,
                    Context.BIND_AUTO_CREATE);
        }
    };

    @Override
    protected void onDestroy() {
        if(mBookManager!=null){
            try {
                mBookManager.unregisterUpdateBookListener(mUpdateBookListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private IUpdateBookListener.Stub mUpdateBookListener=new IUpdateBookListener.Stub() {
        @Override
        public void onUpdateBook(Book newBook) throws RemoteException {
           //运行在Binder线程池，不能直接访问UI,需要Handler进行线程的切换
            Log.d(TAG,"onUpdateBook");
            List<Book> books = mBookManager.getBooks();
            for (Book book : books) {
                Log.d(TAG,book.toString());
            }
        }
    };
}
