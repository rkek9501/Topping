package com.example.topping.topping.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topping.topping.R;

import java.util.ArrayList;

/**
 * Created by Arktic on 2018-05-01.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> arrayList;
    private LayoutInflater inflater;
    private boolean isListView;
    private SparseBooleanArray mSelectedItemsIds;

    private static final int[] images = new int[]{
            R.drawable.basketball,R.drawable.bike, R.drawable.camping, R.drawable.camera2, R.drawable.playguitar,
            R.drawable.movie, R.drawable.piano, R.drawable.art, R.drawable.skateboard, R.drawable.boardgame};

    private static final String[] text = new String[]{
          "농구", "자전거", "캠핑", "사진", "기타",
            "영화", "피아노", "미술", "보드", "보드게임", };

    private class ViewHolder {
        private TextView label;
        private CheckBox checkBox;
        private ImageView image;
    }

    public GridViewAdapter(Context context, /*ArrayList<String> arrayList,*/  boolean isListView) {
        this.context = context;
//        this.arrayList = arrayList;
        this.isListView = isListView;
        inflater = LayoutInflater.from(context);
        mSelectedItemsIds = new SparseBooleanArray();

    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            //inflate the layout on basis of boolean
            if (isListView)
                view = inflater.inflate(R.layout.list_custom_row_layout, viewGroup, false);
            else
                view = inflater.inflate(R.layout.grid_custom_row_layout, viewGroup, false);
            viewHolder.image = (ImageView)view.findViewById(R.id.image);
            viewHolder.label = (TextView) view.findViewById(R.id.label);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.image.setImageResource(images[i]);
        viewHolder.label.setText(text[i]);
        viewHolder.checkBox.setChecked(mSelectedItemsIds.get(i));

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(i, !mSelectedItemsIds.get(i));
            }
        });
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(i, !mSelectedItemsIds.get(i));
            }
        });
        viewHolder.label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(i, !mSelectedItemsIds.get(i));
            }
        });

        return view;
    }

    //전체선택, 전체제거
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    //체크박스가 체크되었는지 확인
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //체크된 체크박스 아이디 리턴
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
