package com.example.topping.topping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.topping.topping.MemberWriteListViewItem;
import com.example.topping.topping.R;

import java.util.ArrayList;

public class MemberWriteListViewAdapter extends BaseAdapter {

    private ArrayList<MemberWriteListViewItem> listItem = new ArrayList<>();

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
            convertView = LayoutInflater.from(context).inflate(R.layout.member_listview, null);
        }
        TextView title = (TextView)convertView.findViewById(R.id.member_listview_title);
        TextView participant = (TextView)convertView.findViewById(R.id.member_listview_participant);

        MemberWriteListViewItem item = (MemberWriteListViewItem)getItem(position);

        title.setText(item.getTitle());
        participant.setText(item.getParticipant());
        return convertView;
    }

    public void addItem(String title, String participant){
        MemberWriteListViewItem item = new MemberWriteListViewItem();

        item.setTitle(title);
        item.setParticipant(participant);

        listItem.add(item);
    }
}
