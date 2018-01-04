package com.it.onex.vlayouttest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.it.onex.vlayouttest.R;
import com.it.onex.vlayouttest.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.Arrays;

/**
 * Created by Linsa on 2018/1/2:11:21.
 * des: 创建相关LayoutHelper的使用
 */

public class SingleLayoutAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutHelper mHelper;

    public SingleLayoutAdapter(Context context, LayoutHelper helper) {
        this.mContext =context;
        this.mHelper=helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_single_layout, parent, false);
        return new RecyclerViewItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItemHolder recyclerViewHolder = (RecyclerViewItemHolder) holder;
//        ViewGroup.LayoutParams layoutParams =recyclerViewHolder.tv_name.getLayoutParams();
//        layoutParams.height = 260 + position % 7 * 20;
//        recyclerViewHolder.tv_name.setLayoutParams(layoutParams);
//        recyclerViewHolder.iv_icon.setBackgroundResource();
        recyclerViewHolder.banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        Integer[] images={R.mipmap.ic,R.mipmap.icon,R.mipmap.icon_head,R.mipmap.icone};
        recyclerViewHolder.banner.setBannerAnimation(Transformer.ZoomIn);
        recyclerViewHolder.banner.setImages(Arrays.asList(images));
        //banner设置方法全部调用完毕时最后调用
        recyclerViewHolder.banner.start();

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private class RecyclerViewItemHolder extends RecyclerView.ViewHolder {

        public Banner banner;

        public RecyclerViewItemHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
        }
    }
}
