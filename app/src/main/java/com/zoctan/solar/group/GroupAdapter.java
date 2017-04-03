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

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GroupBean> mData;
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
            ((ItemViewHolder)holder).mBrief.setText((groupBean.getBrief()));
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
        TextView mBrief;
        ImageView mGroupImg;

        ItemViewHolder(View v){
            super(v);
            mTitle=(TextView)v.findViewById(R.id.tvName);
            mBrief=(TextView)v.findViewById(R.id.tvBrief);
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
        if(mData == null){
            return 0;
        }
        return mData.size();
    }

    public GroupBean getItem(int position){
        return mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
