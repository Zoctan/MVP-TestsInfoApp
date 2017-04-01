package com.zoctan.solar.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoctan.solar.R;
import com.zoctan.solar.beans.PostCommentBean;
import com.zoctan.solar.utils.ImageLoaderUtils;
import com.zoctan.solar.utils.LogUtils;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 3/15/17.
 */

public class PostCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<PostCommentBean> mData;
    private String TAG = "PostCommentAdapter";
    private Context mContext;

    public PostCommentAdapter(Context context){
        this.mContext=context;
    }
    public void setmData(List<PostCommentBean> data){
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LogUtils.d(TAG,"为Item创建视图");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        LogUtils.d(TAG,"将数据绑定到Item的视图");
        if(holder instanceof ItemViewHolder){
            PostCommentBean comment = mData.get(position);
            if(comment == null){
                return;
            }else{
                ((ItemViewHolder)holder).mUser.setText(comment.getComment_user());
                ImageLoaderUtils.displayUserImg(
                        mContext,
                        ((ItemViewHolder)holder).mUserImg,
                        comment.getComment_user_img()
                );
                ((ItemViewHolder)holder).mTime.setText(comment.getComment_time());
                ((ItemViewHolder)holder).mComment.setHtml(comment.getComment_content(),new HtmlHttpImageGetter(((ItemViewHolder)holder).mComment));
            }
        }
    }
    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        } else {
            return mData.size();
        }
    }
    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mUser;
        CircleImageView mUserImg;
        TextView mTime;
        HtmlTextView mComment;

        ItemViewHolder(View view){
            super(view);
            mUserImg = (CircleImageView) view.findViewById(R.id.user_image);
            mUser = (TextView) view.findViewById(R.id.tvUser);
            mTime = (TextView) view.findViewById(R.id.tvTime);
            mComment = (HtmlTextView) view.findViewById(R.id.htCommentContent);
        }
    }
}
