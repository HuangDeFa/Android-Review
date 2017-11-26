package com.kenzz.reader.bean;

import java.util.List;

/**
 * Created by ken.huang on 11/24/2017.
 *
 */

public class GankDailyModel {
   public String Title;
   public List<GankEntity.ResultsBean> DataList;

    public GankDailyModel() {
    }

    public GankDailyModel(String title, List<GankEntity.ResultsBean> dataList) {
        Title = title;
        DataList = dataList;
    }
}
