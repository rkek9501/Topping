package com.example.topping.topping.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topping.topping.ContentListViewItem;
import com.example.topping.topping.R;

import java.util.ArrayList;

/**
 * Created by user on 2018-05-03.
 */

public class ContentListViewAdapter extends BaseAdapter {

    public ArrayList<ContentListViewItem> listItem = new ArrayList<>();

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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_content, null);
        }
        ImageView cImg = (ImageView)convertView.findViewById(R.id.cImg);
        TextView cName = (TextView)convertView.findViewById(R.id.cName);
        TextView cHobby = (TextView)convertView.findViewById(R.id.cHobby);
        TextView cdetail = (TextView)convertView.findViewById(R.id.cdetail);

        ContentListViewItem item = (ContentListViewItem) getItem(position);

        cImg.setImageDrawable(item.getUserImg());
        cName.setText(item.getUserName());
        cHobby.setText(item.getHobby());
        cdetail.setText((CharSequence) item.getDetail());
        return convertView;
    }

    public void addItem(Drawable cImg, String cName, String cHobby, String cdetail){
        ContentListViewItem item = new ContentListViewItem();

        item.setUserImg(cImg);
        item.setUserName(cName);
        item.setHobby(cHobby);
        item.setDetail(cdetail);

        listItem.add(item);
    }
}
