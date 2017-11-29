package com.kenzz.reader.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ken.huang on 11/29/2017.
 *
 */

public class OneBookEntity {

    @SerializedName("count")
    public int count;
    @SerializedName("start")
    public int start;
    @SerializedName("total")
    public int total;
    @SerializedName("books")
    public List<Books> books;

    public static class Rating {
        @SerializedName("max")
        public int max;
        @SerializedName("numRaters")
        public int numRaters;
        @SerializedName("average")
        public String average;
        @SerializedName("min")
        public int min;
    }

    public static class Tags {
        @SerializedName("count")
        public int count;
        @SerializedName("name")
        public String name;
        @SerializedName("title")
        public String title;
    }

    public static class Images {
        @SerializedName("small")
        public String small;
        @SerializedName("large")
        public String large;
        @SerializedName("medium")
        public String medium;
    }

    public static class Series {
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
    }

    public static class Books {
        @SerializedName("rating")
        public Rating rating;
        @SerializedName("subtitle")
        public String subtitle;
        @SerializedName("author")
        public List<String> author;
        @SerializedName("pubdate")
        public String pubdate;
        @SerializedName("tags")
        public List<Tags> tags;
        @SerializedName("origin_title")
        public String origin_title;
        @SerializedName("image")
        public String image;
        @SerializedName("binding")
        public String binding;
        @SerializedName("translator")
        public List<String> translator;
        @SerializedName("catalog")
        public String catalog;
        @SerializedName("ebook_url")
        public String ebook_url;
        @SerializedName("pages")
        public String pages;
        @SerializedName("images")
        public Images images;
        @SerializedName("alt")
        public String alt;
        @SerializedName("id")
        public String id;
        @SerializedName("publisher")
        public String publisher;
        @SerializedName("isbn10")
        public String isbn10;
        @SerializedName("isbn13")
        public String isbn13;
        @SerializedName("title")
        public String title;
        @SerializedName("url")
        public String url;
        @SerializedName("alt_title")
        public String alt_title;
        @SerializedName("author_intro")
        public String author_intro;
        @SerializedName("summary")
        public String summary;
        @SerializedName("ebook_price")
        public String ebook_price;
        @SerializedName("series")
        public Series series;
        @SerializedName("price")
        public String price;
    }
}
