package com.github.rain.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.rain.net.RestRequest;
import com.github.rain.net.callback.DataCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    ProgressBar mProgressBar;
    UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mListView = (ListView) findViewById(android.R.id.list);
        mUserAdapter = new UserAdapter(this);
        mListView.setAdapter(mUserAdapter);

        new RestRequest.Builder()
                .urlPath("search/users?q=followers:>10000")
                .build()
                .enqueue(new DataCallback<Result<List<GitUser>>>() {
                    @Override
                    public void onResponse(Result<List<GitUser>> response) {
                        mProgressBar.setVisibility(View.GONE);
                        mUserAdapter.setUserList(response.getItems());
                    }
                });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                intent.putExtra("user_url", mUserAdapter.getItem(position).getHtml_url());

                startActivity(intent);
            }
        });
    }


}
