package com.kenzz.reviewapp.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangdefa on 15/10/2017.
 * Version 1.0
 */

public class Book implements Parcelable {
    private int bookId;
    private String name;
    private float price;
    protected Book(Parcel in) {
       bookId = in.readInt();
       name = in.readString();
       price = in.readFloat();
    }

    public Book(int bookId, String name, float price) {
        this.bookId = bookId;
        this.name = name;
        this.price = price;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(name);
        dest.writeFloat(price);
    }

    @Override
    public String toString() {
        return "[Book id:"+bookId+" name:"+name+" price:"+price+"]";
    }
}
