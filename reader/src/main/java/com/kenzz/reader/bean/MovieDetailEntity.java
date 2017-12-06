package com.kenzz.reader.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ken.huang on 12/6/2017.
 * MovieDetailEntity
 */

public class MovieDetailEntity {

    @SerializedName("rating")
    public Rating rating;
    @SerializedName("reviews_count")
    public int reviews_count;
    @SerializedName("wish_count")
    public int wish_count;
    @SerializedName("douban_site")
    public String douban_site;
    @SerializedName("year")
    public String year;
    @SerializedName("images")
    public Images images;
    @SerializedName("alt")
    public String alt;
    @SerializedName("id")
    public String id;
    @SerializedName("mobile_url")
    public String mobile_url;
    @SerializedName("title")
    public String title;
    @SerializedName("do_count")
    public String do_count;
    @SerializedName("share_url")
    public String share_url;
    @SerializedName("seasons_count")
    public String seasons_count;
    @SerializedName("schedule_url")
    public String schedule_url;
    @SerializedName("episodes_count")
    public String episodes_count;
    @SerializedName("countries")
    public List<String> countries;
    @SerializedName("genres")
    public List<String> genres;
    @SerializedName("collect_count")
    public int collect_count;
    @SerializedName("casts")
    public List<Casts> casts;
    @SerializedName("current_season")
    public String current_season;
    @SerializedName("original_title")
    public String original_title;
    @SerializedName("summary")
    public String summary;
    @SerializedName("subtype")
    public String subtype;
    @SerializedName("directors")
    public List<Casts> directors;
    @SerializedName("comments_count")
    public int comments_count;
    @SerializedName("ratings_count")
    public int ratings_count;
    @SerializedName("aka")
    public List<String> aka;

    public static class Rating {
        @SerializedName("max")
        public int max;
        @SerializedName("average")
        public double average;
        @SerializedName("stars")
        public String stars;
        @SerializedName("min")
        public int min;
    }

    public static class Images {
        @SerializedName("small")
        public String small;
        @SerializedName("large")
        public String large;
        @SerializedName("medium")
        public String medium;
    }

    public static class Avatars {
        @SerializedName("small")
        public String small;
        @SerializedName("large")
        public String large;
        @SerializedName("medium")
        public String medium;
    }

    public static class Casts {
        @SerializedName("alt")
        public String alt;
        @SerializedName("avatars")
        public Avatars avatars;
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;
    }
}
