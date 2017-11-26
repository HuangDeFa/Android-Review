package com.kenzz.reader.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangdefa on 26/11/2017.
 * Version 1.0
 */

public class SPUtil {
    private final static String CONFIG_NAME="config";

    public static void putString(Context context,String value,String key){
        putString(context,CONFIG_NAME,value,key);
    }

    public static void putString(Context context,String fileName,String value,String key){
        context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
                .edit()
                .putString(key,value)
                .commit();
    }
    public static String getString(Context context,String fileName,String key){
        return context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
                .getString(key,null);
    }
    public static String getString(Context context,String key){
        return getString(context,CONFIG_NAME,key);
    }

    public static boolean getBoolean(Context context,String fileName,String key){
       return context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
                .getBoolean(key,false);
    }
    public static boolean getBoolean(Context context,String key){
        return getBoolean(context,CONFIG_NAME,key);
    }

    public static void putBoolean(Context context,boolean value,String key){
        putBoolean(context,CONFIG_NAME,value,key);
    }

    public static void putBoolean(Context context,String fileName,boolean value,String key){
        context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key,value)
                .commit();
    }

    public static void putStrings(Context context, String fileName, String key, List<String> strings){
        context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
                .edit()
                .putStringSet(key,new HashSet<>(strings))
                .commit();
    }
    public static void putStrings(Context context,String key,List<String> strings){
        putStrings(context,CONFIG_NAME,key,strings);
    }
    public static List<String> getStrings(Context context,String fileName,String key){
        List<String> result=null;
        Set<String> stringSet = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                .getStringSet(key, null);
        if(stringSet!=null){
            result=new ArrayList<>(stringSet);
        }
        return result;
    }
    public static List<String> getStrings(Context context,String key){
        return getStrings(context,CONFIG_NAME,key);
    }
}
