package com.kenzz.reader.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ken.huang on 12/5/2017.
 * MovieEntity
 */

public class MovieEntity {

    @SerializedName("count")
    public int count;
    @SerializedName("start")
    public int start;
    @SerializedName("total")
    public int total;
    @SerializedName("subjects")
    public List<Subject> subjects;
    @SerializedName("title")
    public String title;

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

    public static class Images {
        @SerializedName("small")
        public String small;
        @SerializedName("large")
        public String large;
        @SerializedName("medium")
        public String medium;
    }

    public static class Subject
    {
        @SerializedName("rating")
        public Rating rating;
        @SerializedName("genres")
        public List<String> genres;
        @SerializedName("title")
        public String title;
        @SerializedName("casts")
        public List<Casts> casts;
        @SerializedName("collect_count")
        public int collect_count;
        @SerializedName("original_title")
        public String original_title;
        @SerializedName("subtype")
        public String subtype;
        @SerializedName("directors")
        public List<Casts> directors;
        @SerializedName("year")
        public String year;
        @SerializedName("images")
        public Images images;
        @SerializedName("alt")
        public String alt;
        @SerializedName("id")
        public String id;
    }
}
