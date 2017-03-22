package com.zoctan.solar.group;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.zoctan.solar.R;
import com.zoctan.solar.beans.GroupBean;
import com.zoctan.solar.utils.ImageLoaderUtils;
import com.zoctan.solar.utils.LogUtils;

import java.util.List;


/**
 * Created by root on 3/6/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER =1;
    // Group data set
    private String TAG = "GroupAdapter";
    private List<GroupBean> mData;
   private boolean mShowFooter = true;
    private Context mContext;

    // Listen on group item click event
    private GroupAdapter.OnItemClickListener mOnItemClickListener;

    public GroupAdapter(Context context){
        this.mContext=context;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position){
        if(holder instanceof ItemViewHolder){
            GroupBean groupBean = mData.get(position);
            if(groupBean==null){
                return;
            }
            ((ItemViewHolder)holder).mTitle.setText((groupBean.getTitle()));
        ((ItemViewHolder)holder).mDigest.setText((groupBean.getBrief()));
        ImageLoaderUtils.display(mContext,((ItemViewHolder)holder).mGroupImg,groupBean.getImgsrc());
        }
    }

    public void setmData(List<GroupBean> data){
        this.mData=data;
        // 处理的数据发生变化, 通知View作出改变
        this.notifyDataSetChanged();
    }

    // response for item creating viewer
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group,parent,false);
        return  new ItemViewHolder(v);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitle;
        TextView mDigest;
        ImageView mGroupImg;

        ItemViewHolder(View v){
            super(v);
            mTitle=(TextView)v.findViewById(R.id.tvName);
            mDigest=(TextView)v.findViewById(R.id.tvJoin);
            mGroupImg=(ImageView)v.findViewById(R.id.ivGroup);
            // Listen on item
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(v,this.getPosition());
            }
        }
    }
    public int getItemCount(){
        if(mData==null){
            return 0;
        }
        LogUtils.d(TAG,""+mData.size());
        return mData.size();
    }
    public GroupBean getItem(int position){
        return mData.get(position);
    }
    public boolean isShowFooter(){
        return mShowFooter;
    }
    public void isShowFooter(boolean mShowFooter){
        this.mShowFooter=mShowFooter;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
