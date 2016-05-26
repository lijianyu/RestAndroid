package com.github.rain.rest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class UserDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        WebView webUser = (WebView) findViewById(R.id.webUser);

        webUser.loadUrl(getIntent().getStringExtra("user_url"));
    }
}
