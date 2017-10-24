package com.kenzz.gank.bean;

import java.util.List;

/**
 * Created by ken.huang on 10/24/2017.
 *
 */

public class GankEntity {

    /**
     * error : false
     * results : [{"_id":"593dfaeb421aa92c7be61bf5","createdAt":"2017-06-12T10:22:35.413Z","desc":"Android 权限管理。","images":["http://img.gank.io/0b60d44e-6d53-4a1a-a64c-a5436ac21185"],"publishedAt":"2017-06-12T11:11:11.25Z","source":"chrome","type":"Android","url":"https://github.com/Arjun-sna/android-permission-checker-app","used":true,"who":"代码家"},{"_id":"593dfb03421aa92c794633ad","createdAt":"2017-06-12T10:22:59.890Z","desc":"一行代码即可监听 App 中所有网络链接的上传以及下载进度,包括 Glide 的图片加载进度","images":["http://img.gank.io/4b4a7068-e151-4d74-a782-e86e49ba904b"],"publishedAt":"2017-06-12T11:11:11.25Z","source":"web","type":"Android","url":"https://github.com/JessYanCoding/ProgressManager/blob/master/README-zh.md","used":true,"who":"jess"},{"_id":"59393f36421aa92c79463386","createdAt":"2017-06-08T20:12:38.175Z","desc":"面向Java开发者的C++11学习指南","publishedAt":"2017-06-09T12:50:03.131Z","source":"web","type":"Android","url":"https://pqpo.me/2017/06/08/c-11-learning-notes/","used":true,"who":"Linmin Qiu"},{"_id":"5939484f421aa92c769a8c39","createdAt":"2017-06-08T20:51:27.119Z","desc":"Java并发编程：volatile关键字解析（非常棒的文章）","publishedAt":"2017-06-09T12:50:03.131Z","source":"chrome","type":"Android","url":"http://www.importnew.com/18126.html","used":true,"who":"咕咚"},{"_id":"59394867421aa92c79463387","createdAt":"2017-06-08T20:51:51.475Z","desc":"简单高效的实现Android App全局字体替换","images":["http://img.gank.io/116bb496-79cb-4e31-8823-4389bfa6b629"],"publishedAt":"2017-06-09T12:50:03.131Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/4e1e96fe6d26","used":true,"who":"黎赵太郎"},{"_id":"59396544421aa92c7be61bd2","createdAt":"2017-06-08T22:55:00.289Z","desc":"使用Kotlin开发的Dribbble客户端","images":["http://img.gank.io/325f5635-47e4-4262-8499-d2b4b63cfc69"],"publishedAt":"2017-06-09T12:50:03.131Z","source":"chrome","type":"Android","url":"https://github.com/550609334/Twobbble","used":true,"who":"Jason"},{"_id":"5939dce5421aa92c7946338a","createdAt":"2017-06-09T07:25:25.149Z","desc":"Android之自定义View的死亡三部曲之Layout","publishedAt":"2017-06-09T12:50:03.131Z","source":"web","type":"Android","url":"http://url.cn/4ACGlEk","used":true,"who":"陈宇明"},{"_id":"5939f950421aa92c7946338b","createdAt":"2017-06-09T09:26:40.602Z","desc":"Tumblr 出品：解耦 RecyclerView，提高滑动性能。","publishedAt":"2017-06-09T12:50:03.131Z","source":"chrome","type":"Android","url":"https://github.com/tumblr/Graywater","used":true,"who":"代码家"},{"_id":"5939faa9421aa92c73b647e1","createdAt":"2017-06-09T09:32:25.936Z","desc":"支持不同形态的 Span Grid Layout。","images":["http://img.gank.io/425b030c-e730-4d70-8be0-361cdeae4b68"],"publishedAt":"2017-06-09T12:50:03.131Z","source":"chrome","type":"Android","url":"https://github.com/Arasthel/SpannedGridLayoutManager","used":true,"who":"代码家"},{"_id":"592b8466421aa92c7be61b6b","createdAt":"2017-05-29T10:16:06.620Z","desc":"给初学者的RxJava2.0教程(六)：治理Backpressure","publishedAt":"2017-06-08T11:27:47.21Z","source":"web","type":"Android","url":"https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&mid=2247484689&idx=1&sn=1f54c343c6a5d73c63b4cdb555bdf254&chksm=96cda45ca1ba2d4af7e0e51454283f86d145a19b613a1fc149e4d46d22e277c150d2910e95b6#rd","used":true,"who":"陈宇明"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 593dfaeb421aa92c7be61bf5
         * createdAt : 2017-06-12T10:22:35.413Z
         * desc : Android 权限管理。
         * images : ["http://img.gank.io/0b60d44e-6d53-4a1a-a64c-a5436ac21185"]
         * publishedAt : 2017-06-12T11:11:11.25Z
         * source : chrome
         * type : Android
         * url : https://github.com/Arjun-sna/android-permission-checker-app
         * used : true
         * who : 代码家
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
