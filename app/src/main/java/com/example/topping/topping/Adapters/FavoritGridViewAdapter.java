package com.example.topping.topping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topping.topping.DownloadImageTask;
import com.example.topping.topping.FavoritGridViewItem;
import com.example.topping.topping.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritGridViewAdapter extends BaseAdapter {
    private Context mContext;
    public ArrayList<FavoritGridViewItem> listItem = new ArrayList<>();

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.favorit_item, null);
        }
        TextView otherName = (TextView)convertView.findViewById(R.id.otherUserName);
        TextView otherHobby = (TextView)convertView.findViewById(R.id.otherUserHobby);
        CircleImageView otherImg = (CircleImageView) convertView.findViewById(R.id.otherUserImg);

        FavoritGridViewItem item = (FavoritGridViewItem)getItem(position);

        otherName.setText(item.getOtherUserName());
        otherHobby.setText(item.getOtherUserHobby());
        new DownloadImageTask(otherImg).execute(item.getOtherUserImg());

        return convertView;
    }
    public void addItem(String otherName, String otherHobby, String otherImg, String otherMail){
        FavoritGridViewItem item = new FavoritGridViewItem();

        item.setOtherUserImg(otherImg);
        item.setOtherUserName(otherName);
        item.setOtherUserHobby(otherHobby);
        item.setOtherUserMail(otherMail);

        listItem.add(item);
    }
}
