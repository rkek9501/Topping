package com.example.topping.topping.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topping.topping.R;
import com.example.topping.topping.SearchListViewItem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Arktic on 2018-04-18.
 */

public class SearchListViewAdapter extends BaseAdapter {

    public ArrayList<SearchListViewItem> listItem = new ArrayList<>();

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
        }
        ImageView userImg = (ImageView)convertView.findViewById(R.id.userImg);
        TextView userName = (TextView)convertView.findViewById(R.id.userName);
        TextView userHobby = (TextView)convertView.findViewById(R.id.userHobby);
        TextView toDate = (TextView)convertView.findViewById(R.id.toDate);

        SearchListViewItem item = (SearchListViewItem) getItem(position);

        userImg.setImageDrawable(item.getUserImg());
        userName.setText(item.getUserName());
        userHobby.setText(item.getHobby());
        toDate.setText((CharSequence) item.getDate());
        return convertView;
    }

    public void addItem(Drawable userImg, String userName, String userHobby, String toDate){
        SearchListViewItem item = new SearchListViewItem();

        item.setUserImg(userImg);
        item.setUserName(userName);
        item.setHobby(userHobby);
        item.setDate(toDate);

        listItem.add(item);
    }
}
