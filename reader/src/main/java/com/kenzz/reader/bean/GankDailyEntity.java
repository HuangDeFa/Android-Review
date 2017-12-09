package com.kenzz.reader.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ken.huang on 11/24/2017.
 *
 */

public class GankDailyEntity {
    public boolean error;
    public ResultsBean results;
    public List<String> category;
    public static class ResultsBean implements Serializable{
        @SerializedName("Android")
        public List<GankEntity.ResultsBean> Android;
        @SerializedName("休息视频")
        public List<GankEntity.ResultsBean> RestVideo;
        @SerializedName("前端")
        public List<GankEntity.ResultsBean> FrontEnd;
        @SerializedName("拓展资源")
        public List<GankEntity.ResultsBean> Resource;
        @SerializedName("福利")
        public List<GankEntity.ResultsBean> Welfare;
        @SerializedName("iOS")
        public List<GankEntity.ResultsBean> iOS;
        @SerializedName("App")
        public List<GankEntity.ResultsBean> App;
        @SerializedName("瞎推荐")
        public List<GankEntity.ResultsBean> Recommend;
    }
}
