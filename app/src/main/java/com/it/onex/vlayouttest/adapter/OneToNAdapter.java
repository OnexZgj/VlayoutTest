package com.it.onex.vlayouttest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.it.onex.vlayouttest.R;

import java.util.List;

/**
 * Created by Linsa on 2018/1/2:11:21.
 * des: 创建相关LayoutHelper的使用
 */

public class OneToNAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

    private LayoutHelper mHelper;
    private List<Integer> mData;

    public OneToNAdapter(List<Integer> mData, LayoutHelper helper) {
        this.mData = mData;
        this.mHelper=helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_one_to_n_layout, parent, false);
        return new RecyclerViewItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItemHolder recyclerViewHolder = (RecyclerViewItemHolder) holder;
//        ViewGroup.LayoutParams layoutParams =recyclerViewHolder.tv_name.getLayoutParams();
//        layoutParams.height = 260 + position % 7 * 20;
//        recyclerViewHolder.tv_name.setLayoutParams(layoutParams);
        recyclerViewHolder.iv_image.setBackgroundResource(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private class RecyclerViewItemHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image;

        public RecyclerViewItemHolder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
        }
    }
}
