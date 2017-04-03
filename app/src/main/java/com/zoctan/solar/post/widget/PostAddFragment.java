package com.zoctan.solar.post.widget;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zoctan.solar.R;
import com.zoctan.solar.post.presenter.PostPresenter;
import com.zoctan.solar.utils.SPUtils;

public class PostAddFragment extends Fragment implements View.OnClickListener {
    private static final String toastText_titleNull = "Title can't be null";
    private static final String toastText_contentNull = "Content can't be null";
    private static final String toastText_sendingOK = "sending completed";
    private static final String toastText_sendingFail ="network is bad";
    
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
            Toast.makeText(getContext(),toastText_titleNull,Toast.LENGTH_SHORT).show();
            return;
        }
        if(content.equals("")){
            Toast.makeText(getContext(),toastText_contentNull,Toast.LENGTH_SHORT).show();
            return;
        }
        String user_id = mSPUtils.getString("userID");
        mPostPresenter.sendPost(title,content,user_id);
    }
    public void showProcessBar(){

    }
    public void hideProcessBar(){

    }
    public void queryAction(){
        Toast.makeText(getContext(),toastText_sendingOK,Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }
    public void showFailedMessage(){
        Toast.makeText(getContext(),toastText_sendingFail,Toast.LENGTH_SHORT).show();
    }
}