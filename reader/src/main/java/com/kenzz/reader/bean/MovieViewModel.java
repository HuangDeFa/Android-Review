package com.kenzz.reader.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ken.huang on 12/6/2017.
 * MovieViewModel
 */

public class MovieViewModel implements Parcelable {
    public String imageUrl;
    public String title;
    public String directors;
    public String casts;
    public String type;
    public String rate;
    public String year;
    public String rateNum;
    public String id;

    public MovieViewModel(String title,String directors,String casts, String type, String rate) {
        this.title = title;
        this.directors = directors;
        this.casts = casts;
        this.type = type;
        this.rate = rate;
    }

    public MovieViewModel() {
    }

    protected MovieViewModel(Parcel in) {
        imageUrl = in.readString();
        title = in.readString();
        directors = in.readString();
        casts = in.readString();
        type = in.readString();
        rate = in.readString();
        year = in.readString();
        rateNum = in.readString();
        id=in.readString();
    }

    public static final Creator<MovieViewModel> CREATOR = new Creator<MovieViewModel>() {
        @Override
        public MovieViewModel createFromParcel(Parcel in) {
            return new MovieViewModel(in);
        }

        @Override
        public MovieViewModel[] newArray(int size) {
            return new MovieViewModel[size];
        }
    };

    public static MovieViewModel build(MovieEntity.Subject subject){
        MovieViewModel model=new MovieViewModel();
        model.title=subject.title;
        model.imageUrl=subject.images.medium;
        StringBuilder sb=new StringBuilder();
        for (MovieEntity.Casts director : subject.directors) {
            sb.append(director.name);
            sb.append("/");
        }
        model.directors = sb.subSequence(0,sb.length()-1).toString();
        sb=new StringBuilder();
        for (MovieEntity.Casts cast : subject.casts) {
            sb.append(cast.name);
            sb.append("/");
        }
        model.casts = sb.subSequence(0,sb.length()-1).toString();
        sb=new StringBuilder();
        for (String genre : subject.genres) {
            sb.append(genre);
            sb.append("/");
        }
        model.type = sb.subSequence(0,sb.length()-1).toString();
        model.rate=subject.rating.average+"";
        model.rateNum=subject.rating.stars;
        model.year=subject.year;
        model.id=subject.id;
        return model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(directors);
        dest.writeString(casts);
        dest.writeString(type);
        dest.writeString(rate);
        dest.writeString(year);
        dest.writeString(rateNum);
        dest.writeString(id);
    }
}
