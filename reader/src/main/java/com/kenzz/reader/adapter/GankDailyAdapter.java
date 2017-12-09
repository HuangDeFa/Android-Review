package com.kenzz.reader.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzz.reader.Constant;
import com.kenzz.reader.R;
import com.kenzz.reader.bean.GankDailyModel;
import com.kenzz.reader.bean.GankEntity;
import com.kenzz.reader.utils.ImageLoader;
import com.kenzz.reader.widget.Banner;
import com.kenzz.reader.widget.DefaultBannerLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ken.huang on 11/24/2017.
 * GankDailyAdapter
 */

public class GankDailyAdapter extends BaseRecyclerViewAdapter<GankDailyModel, GankDailyAdapter.GankDailyVH> {

    private List<Object> bannerDatas;
    private final List<Object> defaultBannerDatas = new ArrayList<Object>() {
        {
            add("http://7xi8d6.com1.z0.glb.clouddn.com/20171109095254_dOw5qh_bluenamchu_9_11_2017_9_52_47_256.jpeg");
            add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg");
            add("http://ww2.sinaimg.cn/large/7a8aed7bjw1ewym3nctp0j20i60qon23.jpg");
            add("http://ww4.sinaimg.cn/large/7a8aed7bgw1eujhfwyj27j20qo0hs0vy.jpg");
            add("http://ww3.sinaimg.cn/large/7a8aed7bgw1ewy3cst6rzj20lx0v4wj5.jpg");
        }
    };
    private Random mRandom =new Random();
    public GankDailyAdapter(List<GankDailyModel> modelList) {
        this.dataList = modelList;
        this.bannerDatas = defaultBannerDatas;
    }

    @UiThread
    public void updateDataList(List<GankDailyModel> modelList, List<Object> bannerDatas) {
        this.dataList = modelList;
        if (bannerDatas != null && bannerDatas.size() > 0) {
            this.bannerDatas = bannerDatas;
        }
        notifyDataSetChanged();
    }

    public void updateBannerData(List<Object> bannerDatas) {
        if (bannerDatas != null && bannerDatas.size() > 0) {
            this.bannerDatas = bannerDatas;
            notifyItemChanged(0);
        }
    }

    @Override
    public GankDailyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        if (viewType == -1) {
            layoutId = R.layout.item_gank_daily_head;
        } else if (viewType == 0) {
            layoutId = R.layout.item_gank_daily_title;
        } else if (viewType == 1) {
            layoutId = R.layout.item_gank_daily_content_one;
        } else if (viewType == 2) layoutId = R.layout.item_gank_daily_content_two;
        else if (viewType == 3) layoutId = R.layout.item_gank_daily_content_three;
        else layoutId = R.layout.item_gank_daily_content_six;
        return new GankDailyVH(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(GankDailyVH holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == -1) {
            Banner banner = holder.getView(R.id.banner_header);
            banner.setDataAndLoader(bannerDatas, new DefaultBannerLoader(banner.getContext()));
        }
        if (viewType == 0) {
            bindTitleView(holder, position);
        } else if (viewType == 1) {
            bindOneContentView(holder, position);
        } else if (viewType == 2) {
            bindTwoContentView(holder, position);
        } else if (viewType == 3) {
            bindThreeContentView(holder, position);
        } else if (viewType == 6) {
            bindSixContentView(holder, position);
        }
    }

    private void bindTitleView(GankDailyVH holder, int position) {
        TextView textView = holder.getView(R.id.tv_item_gank_daily_title);
        ImageView imageView = holder.getView(R.id.iv_item_gank_daily_title);
        String title = dataList.get(position).Title;
        textView.setText(title);
        if (title.equals("Android")) {
            imageView.setImageResource(R.mipmap.home_title_android);
        } else if (title.equals("IOS")) {
            imageView.setImageResource(R.mipmap.home_title_ios);
        } else if (title.equals("应用")) {
            imageView.setImageResource(R.mipmap.home_title_app);
        } else if (title.equals("福利")) {
            imageView.setImageResource(R.mipmap.home_title_meizi);
        } else if (title.equals("小视频")) {
            imageView.setImageResource(R.mipmap.home_title_movie);
        } else if (title.equals("前端")) {
            imageView.setImageResource(R.mipmap.home_title_qian);
        } else if (title.equals("拓展资源")) {
            imageView.setImageResource(R.mipmap.home_title_source);
        } else if (title.equals("瞎推荐")) {
            imageView.setImageResource(R.mipmap.home_title_xia);
        }
        holder.getView(R.id.tv_item_gank_daily_title_more)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTouchListener!=null){
                    mTouchListener.onMoreClick(title);
                }
            }
        });
    }

    private void bindOneContentView(GankDailyVH holder, int position) {
        GankDailyModel model = dataList.get(position);
        TextView textView = holder.getView(R.id.tv_item_gank_daily_content_one);
        ImageView imageView = holder.getView(R.id.iv_item_gank_daily_content_one);
        textView.setText(model.DataList.get(0).desc);
        if (model.DataList.get(0).type.equals("福利")) {
            ImageLoader.LoadImage(imageView, model.DataList.get(0).url, R.mipmap.img_one_bi_one);
        }else {
             int index =mRandom.nextInt(6);
            ImageLoader.LoadImage(imageView,generateImageUrl(holder.itemView,Constant.DAILY_SIX_URLS[index]), R.mipmap.img_one_bi_one);
        }
        imageView.setOnClickListener(v -> {
            if(mTouchListener!=null){
                mTouchListener.onClick(model.DataList.get(0),position);
            }
        });
    }

    private void bindTwoContentView(GankDailyVH holder, int position) {
        GankDailyModel model = dataList.get(position);
        TextView textView = holder.getView(R.id.tv_item_gank_daily_content_two);
        ImageView imageView = holder.getView(R.id.iv1_item_gank_daily_content_two);
        ImageView imageView1 = holder.getView(R.id.iv2_item_gank_daily_content_two);
        textView.setText(model.DataList.get(0).desc);
        if(model.DataList.get(0).type.equals("福利")){
            ImageLoader.LoadImage(imageView, model.DataList.get(0).url, R.mipmap.img_one_bi_one);
            ImageLoader.LoadImage(imageView1, model.DataList.get(1).url, R.mipmap.img_one_bi_one);
        }else {
            int index =mRandom.nextInt(3);
            ImageLoader.LoadImage(imageView,generateImageUrl(textView,Constant.DAILY_THREE_URLS[index]), R.mipmap.img_one_bi_one);
            index = mRandom.nextInt(3);
            ImageLoader.LoadImage(imageView1,generateImageUrl(holder.itemView,Constant.DAILY_THREE_URLS[index]), R.mipmap.img_one_bi_one);
        }
        imageView.setOnClickListener(v -> {
            if(mTouchListener!=null){
                mTouchListener.onClick(model.DataList.get(0),position);
            }
        });
        imageView1.setOnClickListener(v -> {
            if(mTouchListener!=null){
                mTouchListener.onClick(model.DataList.get(1),position);
            }
        });
    }

    private void bindThreeContentView(GankDailyVH holder, int position) {
        GankDailyModel model = dataList.get(position);
        ViewGroup content = holder.getView(R.id.ly1_item_gank_daily_content_three);
        boolean isMeizi=model.DataList.get(0).type.equals("福利");
        setContentView(content,0,model,isMeizi?null:Constant.DAILY_THREE_URLS[mRandom.nextInt(3)]);

        content = holder.getView(R.id.ly2_item_gank_daily_content_three);
        setContentView(content,1,model,isMeizi?null:Constant.DAILY_THREE_URLS[mRandom.nextInt(3)]);

        content = holder.getView(R.id.ly3_item_gank_daily_content_three);
        setContentView(content,2,model,isMeizi?null:Constant.DAILY_THREE_URLS[mRandom.nextInt(3)]);
    }

    private void setContentView(ViewGroup content, int index, GankDailyModel model, String defaultImageUrl){
        ((TextView) content.getChildAt(1)).setText(model.DataList.get(index).desc);
        ImageView imageView= (ImageView) content.getChildAt(0);
        ImageLoader.LoadImage(imageView,generateImageUrl(content,defaultImageUrl)==null?model.DataList.get(index).url:
                defaultImageUrl,R.mipmap.img_four_bi_three);
        content.setOnClickListener((View v) -> {
            if(mTouchListener!=null){
                mTouchListener.onClick(model.DataList.get(index),0);
            }
        });
    }

    private void bindSixContentView(GankDailyVH holder, int position) {
        GankDailyModel model = dataList.get(position);
        ViewGroup content = holder.getView(R.id.ly1_item_gank_daily_content_six);
        boolean isMeizi=model.DataList.get(0).type.equals("福利");
        setContentView(content,0,model,isMeizi?null:Constant.DAILY_SIX_URLS[mRandom.nextInt(6)]);

        content = holder.getView(R.id.ly2_item_gank_daily_content_six);
        setContentView(content,1,model,isMeizi?null:Constant.DAILY_SIX_URLS[mRandom.nextInt(6)]);

        content = holder.getView(R.id.ly3_item_gank_daily_content_six);
        ((TextView) content.getChildAt(1)).setText(model.DataList.get(2).desc);
        setContentView(content,2,model,isMeizi?null:Constant.DAILY_SIX_URLS[mRandom.nextInt(6)]);

        content = holder.getView(R.id.ly4_item_gank_daily_content_six);
        ((TextView) content.getChildAt(1)).setText(model.DataList.get(3).desc);
        setContentView(content,3,model,isMeizi?null:Constant.DAILY_SIX_URLS[mRandom.nextInt(6)]);

        content = holder.getView(R.id.ly5_item_gank_daily_content_six);
        setContentView(content,4,model,isMeizi?null:Constant.DAILY_SIX_URLS[mRandom.nextInt(6)]);

        content = holder.getView(R.id.ly6_item_gank_daily_content_six);
        setContentView(content,5,model,isMeizi?null:Constant.DAILY_SIX_URLS[mRandom.nextInt(6)]);
    }

    private String generateImageUrl(View imageView,String defaultUrl){
        if(TextUtils.isEmpty(defaultUrl)) return null;
        String url;
        if(imageView.getTag()==null){
            imageView.setTag(defaultUrl);
            url =defaultUrl;
        }else {
            url = (String) imageView.getTag();
        }
        return url;
    }


    @Override
    public int getItemViewType(int position) {
        GankDailyModel model = dataList.get(position);
        if (!TextUtils.isEmpty(model.Title)) {
            if (model.Title.equals("head")) return -1;
            return 0;
        } else {
            int size = model.DataList.size();
            if (size == 1) return 1;
            if (size < 3) return 2;
            if (size >= 6) return 6;
            return 3;
        }
    }

   public static class GankDailyVH extends RecyclerView.ViewHolder {
        SparseArray<View> mViewCaches;

        public GankDailyVH(View itemView) {
            super(itemView);
            mViewCaches = new SparseArray<>();
        }

        public <T extends View> T getView(@IdRes int resId) {
            View view = mViewCaches.get(resId);
            if (view == null) {
                view = itemView.findViewById(resId);
                mViewCaches.put(resId, view);
            }
            return (T) view;
        }
    }

   public interface ItemTouchListener{
        void onClick(GankEntity.ResultsBean data,int position);
        void onMoreClick(String type);
   }

    public void setTouchListener(ItemTouchListener touchListener) {
        mTouchListener = touchListener;
    }

    private ItemTouchListener mTouchListener;

}
