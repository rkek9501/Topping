package com.example.topping.topping.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topping.topping.R;
import com.example.topping.topping.SearchListViewItem;

import java.util.ArrayList;

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
//        ImageView userImg = (ImageView)convertView.findViewById(R.id.userImg);
        TextView userName = (TextView)convertView.findViewById(R.id.searchUserName);
        TextView userHobby = (TextView)convertView.findViewById(R.id.searchUserHobby);
        TextView toDate = (TextView)convertView.findViewById(R.id.searchToDate);
        TextView place = (TextView)convertView.findViewById(R.id.searchPlace);

        SearchListViewItem item = (SearchListViewItem) getItem(position);

//        userImg.setImageDrawable(item.getUserImg());
        userName.setText("주최자 : "+item.getUserName());
        userHobby.setText("취 미 : "+item.getHobby());
        toDate.setText("시작일 : "+(CharSequence) item.getDate());
        place.setText("장 소 : "+item.getPlace());
        return convertView;
    }

    public void addItem(Drawable userImg, String userName, String userHobby, String toDate, String place){
        SearchListViewItem item = new SearchListViewItem();

        item.setUserImg(userImg);
        item.setUserName(userName);
        item.setHobby(userHobby);
        item.setDate(toDate);
        item.setPlace(place);

        listItem.add(item);
    }
}
