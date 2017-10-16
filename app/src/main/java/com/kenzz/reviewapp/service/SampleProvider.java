package com.kenzz.reviewapp.service;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by huangdefa on 16/10/2017.
 * Version 1.0
 * 一个简单的ContentProvider
 * ContentProvider底层是Binder.所以根据Binder 的原理query,insert,delete,update 均是
 * 运行在Provider所在进程的Binder线程池中，所以这里需要考虑多线程访问的情况。
 */

public class SampleProvider extends ContentProvider {

    private static final  String AUTHORITY="com.kenzz.reviewapp.service.SampleProvider";

    private static final int PERSON_CODE=0;
    private static final int USER_CODE=1;
    private static UriMatcher sMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sMatcher.addURI(AUTHORITY,"person",PERSON_CODE);
        sMatcher.addURI(AUTHORITY,"user",USER_CODE);
    }

    private DbHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        //运行在主线程
      mDbHelper = new  DbHelper(getContext());
      mDatabase = mDbHelper.getWritableDatabase();
     // mDatabase.execSQL("insert into table "+DbHelper.PERSONTABLENAME+"values()");
        return false;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        getContext().getContentResolver().notifyChange(uri,null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        getContext().getContentResolver().notifyChange(uri,null);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        getContext().getContentResolver().notifyChange(uri,null);
        return 0;
    }

    private String getTableName(Uri uri){
        String table=null;
        int code = sMatcher.match(uri);
        switch (code){
            case PERSON_CODE:
                table = DbHelper.PERSONTABLENAME;
                break;
            case USER_CODE:
                table = DbHelper.USERTABLENAME;
                break;
            default:
                break;
        }
        return table;
    }

    static class DbHelper extends SQLiteOpenHelper{
        static String dbName="sampleProvider.db";
        final static  String USERTABLENAME="tbl_user";
        final static String PERSONTABLENAME="tbl_person";
        private String createPersonTable ="create table if not exist "+PERSONTABLENAME+" (_id integer primary key,age int,name text)";
        public DbHelper(Context context) {
            super(context, dbName, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
           db.execSQL(createPersonTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
