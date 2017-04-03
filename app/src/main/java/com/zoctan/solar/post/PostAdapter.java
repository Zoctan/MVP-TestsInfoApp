package com.zoctan.solar.post;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoctan.solar.R;
import com.zoctan.solar.beans.PostBean;
import com.zoctan.solar.utils.LogUtils;

import java.util.List;

/**
 * Post适配器
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    // Post数据集合
    private List<PostBean> mData;
    private boolean mShowFooter = true;
    private Context mContext;
    private String TAG = "PostAdapter";

    // 监听列表点击事件
    private OnItemClickListener mOnItemClickListener;

    public PostAdapter(Context context) {
        this.mContext = context;
    }

    public void setmDate(List<PostBean> data) {
        LogUtils.d(TAG,"mData change.");
        this.mData = data;
        // 处理的数据发生变化, 通知View作出改变
        this.notifyDataSetChanged();
    }

    // 不同位置的数据进行不同的处理
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if(!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    // 负责为Item创建视图
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtils.d(TAG,"为Item创建视图");
        if((viewType == TYPE_ITEM) || true) {
            // 实例化一个item_post布局
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
             return new ItemViewHolder(v);
        } else {
            // 实例化一个footer布局
            @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, null);
            // 第一个参数为宽的设置，第二个参数为高的设置
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    // 负责将数据绑定到Item的视图上
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogUtils.d(TAG,"将数据绑定到Item的视图上");

        LogUtils.d(TAG,String.format("position: %d",position));
        if(holder instanceof ItemViewHolder) {
            if(position>=mData.size()){
                return;
            }
            PostBean post = mData.get(position);
            if(post == null) {
                return;
            }
            LogUtils.d(TAG,String.format("postion: %d" +
                    "mData.size(): %d" +
                    "post.getTitle(): %s",position,mData.size(),post.getTitle()));
            // 为item设置标题
            ((ItemViewHolder) holder).mTitle.setText(post.getTitle());
            // 为item设置time
            ((ItemViewHolder) holder).mTime.setText(post.getDigest());
            // 为item设置user
            ((ItemViewHolder)holder).mUser.setText(post.getUser());
        }
    }

    // 得到item数目
    @Override
    public int getItemCount() {
        /*
        int begin = mShowFooter ? 1 : 0;
        if(mData == null) {
            return begin;
        }
        return mData.size() + begin;
        */
        return mData == null ? 0 : mData.size();
    }

    public PostBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitle;
        TextView mTime;
        TextView mUser;

        ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tvTitle_postItem);
            mTime = (TextView) v.findViewById(R.id.tvTime_postItem);
            mUser = (TextView) v.findViewById(R.id.tvUser_postItem);

            // 监听item
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mOnItemClickListener != null) {
                // 点击某个item时,找到其view和位置
                mOnItemClickListener.onItemClick(view, this.getPosition());
            }
        }
    }
}
