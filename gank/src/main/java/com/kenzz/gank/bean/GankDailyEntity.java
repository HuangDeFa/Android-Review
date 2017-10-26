package com.kenzz.gank.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ken.huang on 10/24/2017.
 */

public class GankDailyEntity {

    /**
     * category : ["RestVideo","FrontEnd","Android","Welfare","Resource"]
     * error : false
     * results : {"Android":[{"_id":"59ee0dd5421aa90fe50c0199","createdAt":"2017-10-23T23:42:13.769Z","desc":"设计模式之 Kotlin 实现","publishedAt":"2017-10-24T11:50:49.1Z","source":"web","type":"Android","url":"https://github.com/volodymyrprokopyuk/kotlin-sdp","used":true,"who":null},{"_id":"59ee9a4e421aa90fe72535bb","createdAt":"2017-10-24T09:41:34.774Z","desc":"如何简单高效的学会Smali语法?","publishedAt":"2017-10-24T11:50:49.1Z","source":"web","type":"Android","url":"https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&mid=2247487635&idx=1&sn=407576fc688f4bef4bb689fd5c025e33","used":true,"who":"陈宇明"},{"_id":"59eeb78f421aa90fe72535bd","createdAt":"2017-10-24T11:46:23.809Z","desc":"Java 实现的自然语言处理 中文分词 词性标注 命名实体识别 依存句法分析 关键词提取 自动摘要 短语提取 拼音 简繁转换。","publishedAt":"2017-10-24T11:50:49.1Z","source":"chrome","type":"Android","url":"https://github.com/hankcs/HanLP","used":true,"who":"代码家"}],"RestVideo":[{"_id":"59eb735f421aa90fef2034b1","createdAt":"2017-10-22T00:18:39.652Z","desc":"造价1万亿元的世界第一高楼，高4千米住100万人，2050年完工","publishedAt":"2017-10-24T11:50:49.1Z","source":"chrome","type":"RestVideo","url":"http://www.bilibili.com/video/av15580072/","used":true,"who":"LHF"}],"FrontEnd":[{"_id":"59edb49b421aa90fe50c0197","createdAt":"2017-10-23T17:21:31.325Z","desc":"前端每周清单第 36 期：深入 Vue 2.5 类型增强、Puppeteer 端到端测试、PayPal 跨域套装","publishedAt":"2017-10-24T11:50:49.1Z","source":"web","type":"FrontEnd","url":"https://zhuanlan.zhihu.com/p/30379534","used":true,"who":"王下邀月熊(Chevalier)"},{"_id":"59edc0ab421aa90fe50c0198","createdAt":"2017-10-23T18:12:59.895Z","desc":"饿了么总结的Node.js进阶知识","publishedAt":"2017-10-24T11:50:49.1Z","source":"web","type":"FrontEnd","url":"https://elemefe.github.io/node-interview/#/sections/zh-cn/","used":true,"who":"鑫花璐放"}],"Resource":[{"_id":"59eeb761421aa90fef2034c4","createdAt":"2017-10-24T11:45:37.558Z","desc":"机器学习入门实战。","publishedAt":"2017-10-24T11:50:49.1Z","source":"chrome","type":"Resource","url":"https://github.com/apachecn/MachineLearning","used":true,"who":"代码家"}],"Welfare":[{"_id":"59ee8adf421aa90fe50c019b","createdAt":"2017-10-24T08:35:43.61Z","desc":"10-24","publishedAt":"2017-10-24T11:50:49.1Z","source":"chrome","type":"Welfare","url":"http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg","used":true,"who":"代码家"}]}
     */

    private boolean error;
    private ResultsBean results;
    private List<String> category;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public static class ResultsBean {
        @SerializedName("Android")
        private List<GankEntity.ResultsBean> Android;
        @SerializedName("休息视频")
        private List<GankEntity.ResultsBean> RestVideo;
        @SerializedName("前端")
        private List<GankEntity.ResultsBean> FrontEnd;
        @SerializedName("拓展资源")
        private List<GankEntity.ResultsBean> Resource;
        @SerializedName("福利")
        private List<GankEntity.ResultsBean> Welfare;
        @SerializedName("iOS")
        private List<GankEntity.ResultsBean> iOS;
        @SerializedName("App")
        private List<GankEntity.ResultsBean> App;
        @SerializedName("瞎推荐")
        private List<GankEntity.ResultsBean> Recommend;

        public List<GankEntity.ResultsBean> getiOS() {
            return iOS;
        }

        public void setiOS(List<GankEntity.ResultsBean> iOS) {
            this.iOS = iOS;
        }

        public List<GankEntity.ResultsBean> getApp() {
            return App;
        }

        public void setApp(List<GankEntity.ResultsBean> app) {
            App = app;
        }

        public List<GankEntity.ResultsBean> getRecommend() {
            return Recommend;
        }

        public void setRecommend(List<GankEntity.ResultsBean> recommend) {
            this.Recommend = recommend;
        }

        public List<GankEntity.ResultsBean> getAndroid() {
            return Android;
        }

        public void setAndroid(List<GankEntity.ResultsBean> Android) {
            this.Android = Android;
        }

        public List<GankEntity.ResultsBean> getRestVideo() {
            return RestVideo;
        }

        public void setRestVideo(List<GankEntity.ResultsBean> restVideo) {
            this.RestVideo = restVideo;
        }

        public List<GankEntity.ResultsBean> getFrontEnd() {
            return FrontEnd;
        }

        public void setFrontEnd(List<GankEntity.ResultsBean> frontEnd) {
            this.FrontEnd = frontEnd;
        }

        public List<GankEntity.ResultsBean> getResource() {
            return Resource;
        }

        public void setResource(List<GankEntity.ResultsBean> resource) {
            this.Resource = resource;
        }

        public List<GankEntity.ResultsBean> getWelfare() {
            return Welfare;
        }

        public void setWelfare(List<GankEntity.ResultsBean> welfare) {
            this.Welfare = welfare;
        }
    }
}
