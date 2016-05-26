package com.github.rain.rest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jianyu.L (<a href="mailto:lijianyu2012@gmail.com">lijianyu2012@gmail.com</a>)
 * @since 2016-05-26 13:36
 */
public class UserAdapter extends BaseAdapter {
    List<GitUser> mUserList;

    Context mContext;
    LayoutInflater mLayoutInflater;

    public UserAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mUserList = new ArrayList<>();
    }

    public void setUserList(List<GitUser> userList) {
        mUserList.clear();
        mUserList.addAll(userList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public GitUser getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_user_list, null);
            viewHolder.headView = (ImageView) convertView.findViewById(R.id.head);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.name);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        GitUser gitUser = getItem(position);

        Glide.with(mContext).load(gitUser.getAvatar_url()).into(viewHolder.headView);
        viewHolder.nameView.setText(gitUser.getLogin());
        viewHolder.contentView.setText("score:" + gitUser.getScore());

        return convertView;
    }

    static class ViewHolder {
        ImageView headView;
        TextView nameView, contentView;
    }
}
