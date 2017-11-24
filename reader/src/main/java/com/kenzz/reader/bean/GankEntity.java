package com.kenzz.reader.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ken.huang on 11/24/2017.
 * 
 */

public class GankEntity {
    public boolean error;
    public List<ResultsBean> results;

    public static class ResultsBean {
        @SerializedName("_id")
        public String _id;
        @SerializedName("createdAt")
        public String createdAt;
        @SerializedName("desc")
        public String desc;
        @SerializedName("publishedAt")
        public String publishedAt;
        @SerializedName("source")
        public String source;
        @SerializedName("type")
        public String type;
        @SerializedName("url")
        public String url;
        @SerializedName("used")
        public boolean used;
        @SerializedName("who")
        public String who;
        @SerializedName("images")
        public List<String> images;
    }
}
