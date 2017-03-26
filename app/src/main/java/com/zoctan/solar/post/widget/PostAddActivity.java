package com.zoctan.solar.post.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zoctan.solar.R;

public class PostAddActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private EditText mTitle;
    private EditText mContent;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_box);


        initView();
    }

    private void initView(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mToolbar.setTitle("Fire a post");

        mTitle = (EditText)findViewById(R.id.add_post_title);
        mContent = (EditText)findViewById(R.id.add_post_content);
        mButton = (Button) findViewById(R.id.add_post_submit);

        mButton.setOnClickListener(this);
    }

    public void onClick(View view){
        // do something.
    }
}
