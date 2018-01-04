package com.it.onex.vlayouttest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.it.onex.vlayouttest.R;

/**
 * Created by Linsa on 2018/1/2:11:21.
 * des: 创建相关LayoutHelper的使用
 */

public class FloatLayoutAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutHelper mHelper;

    public FloatLayoutAdapter(Context context,LayoutHelper helper) {
        this.mContext=context;
        this.mHelper=helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_float_layout, parent, false);
        return new RecyclerViewItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItemHolder recyclerViewHolder = (RecyclerViewItemHolder) holder;
//        ViewGroup.LayoutParams layoutParams =recyclerViewHolder.tv_name.getLayoutParams();
//        layoutParams.height = 260 + position % 7 * 20;
//        recyclerViewHolder.tv_name.setLayoutParams(layoutParams);
//        recyclerViewHolder.iv_icon.setBackgroundResource();
//        recyclerViewHolder.iv_float.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "Float Helper!", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private class RecyclerViewItemHolder extends RecyclerView.ViewHolder {

        public ImageView iv_float;

        public RecyclerViewItemHolder(View itemView) {
            super(itemView);
            iv_float = itemView.findViewById(R.id.iv_float);
        }
    }
}
