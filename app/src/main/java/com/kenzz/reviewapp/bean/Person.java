package com.kenzz.reviewapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangdefa on 15/10/2017.
 * Version 1.0
 * 主要用于序列化对象到内存中去
 */

public class Person implements Parcelable {
    private int age;
    private String name;

    protected Person(Parcel in) {
        age = in.readInt();
        name = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(name);
    }
}
