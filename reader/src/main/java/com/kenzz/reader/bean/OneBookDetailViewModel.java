package com.kenzz.reader.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken.huang on 11/29/2017.
 * OneBookDetailViewModel
 */

public class OneBookDetailViewModel implements Parcelable {

    public String url;
    public String imageUrl;
    public String title;
    public String subTitle;
    public List<String> authors=new ArrayList<>();
    public String publishTime;
    public String publisher;
    // public List<String> translators;
    //评分
    public float averageRate;
    //评分人数
    public int raters;

    public String summary;
    public String authorInfo;
    public String catalog;

    public OneBookDetailViewModel(){}

    protected OneBookDetailViewModel(Parcel in) {
        url = in.readString();
        imageUrl = in.readString();
        title = in.readString();
        subTitle = in.readString();
        in.readStringList(authors);
        publishTime = in.readString();
        publisher = in.readString();
        averageRate = in.readFloat();
        raters = in.readInt();
        summary = in.readString();
        authorInfo = in.readString();
        catalog = in.readString();

    }

    public static final Creator<OneBookDetailViewModel> CREATOR = new Creator<OneBookDetailViewModel>() {
        @Override
        public OneBookDetailViewModel createFromParcel(Parcel in) {
            return new OneBookDetailViewModel(in);
        }

        @Override
        public OneBookDetailViewModel[] newArray(int size) {
            return new OneBookDetailViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeStringList(authors);
        dest.writeString(publishTime);
        dest.writeString(publisher);
        // dest.writeStringList(translators);
        dest.writeFloat(averageRate);
        dest.writeInt(raters);
        dest.writeString(summary);
        dest.writeString(authorInfo);
        dest.writeString(catalog);
    }

    public static OneBookDetailViewModel build(OneBookEntity.Books books){
        OneBookDetailViewModel model=new OneBookDetailViewModel();
        model.url=books.alt;
        model.imageUrl=books.image;
        model.title=books.title;
        model.summary=books.summary;
        model.subTitle=books.subtitle;
        model.title=books.title;
        model.catalog=books.catalog;
        model.authorInfo=books.author_intro;
        model.publisher=books.publisher;
        model.publishTime=books.pubdate;
        model.authors=books.author;
        model.averageRate=Float.valueOf(books.rating.average);
        model.raters=books.rating.numRaters;
        return model;
    }
}
