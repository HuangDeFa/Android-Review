package com.kenzz.gank.activity;

import android.os.Bundle;

import com.kenzz.gank.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GankActivity extends BaseActivity {

    private int[] mDate=new int[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank);
        parseDateString();
    }

    private void parseDateString(){
       String date = getIntent().getStringExtra("DATE");
        date = date.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
        try {
            Date d = format.parse(date );
            System.out.print(d);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
           mDate[0] = calendar.get(Calendar.YEAR);
           mDate[1] = calendar.get(Calendar.MONTH)+1;
           mDate[2] = calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadData(){

    }
}
