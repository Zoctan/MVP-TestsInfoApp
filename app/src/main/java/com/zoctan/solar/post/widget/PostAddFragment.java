package com.zoctan.solar.post.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zoctan.solar.R;
import com.zoctan.solar.post.presenter.PostPresenter;
import com.zoctan.solar.utils.SPUtils;
import com.zoctan.solar.utils.ToastUtils;

public class PostAddFragment extends Fragment implements View.OnClickListener {
    
    private EditText mTitle;
    private EditText mContent;
    private SPUtils mSPUtils;
    private PostPresenter mPostPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_add_post, null);
        initView(view);
        return view;
    }

    public void initView(View view) {
        mTitle = (EditText) view.findViewById(R.id.post_title);
        mContent = (EditText) view.findViewById(R.id.post_content);
        Button mButton = (Button) view.findViewById(R.id.btn_add_post);
        mSPUtils = new SPUtils(view.getContext());
        mPostPresenter = new PostPresenter(this);
        mButton.setOnClickListener(this);
    }

    public void onClick(View view){
        String title=mTitle.getText().toString();
        String content = mContent.getText().toString();
        if(title.equals("")){
            ToastUtils.showShort(getContext(), "标题不能为空");
            return;
        }
        if(content.equals("")){
            ToastUtils.showShort(getContext(), "内容不能为空");
            return;
        }
        String user_id = mSPUtils.getString("userID");
        mPostPresenter.sendPost(mSPUtils.getString("group_id"), title,content,user_id);
    }

    public void queryAction(){
        ToastUtils.showShort(getContext(), "成功发帖");
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void showFailedMessage(){
        ToastUtils.showShort(getContext(), "发帖失败");
    }
}