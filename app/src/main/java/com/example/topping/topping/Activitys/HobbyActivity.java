package com.example.topping.topping.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.topping.topping.Adapters.GridViewAdapter;
import com.example.topping.topping.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.soyu.soyulib.soyuHttpTask;

import java.util.ArrayList;

public class HobbyActivity extends AbstractActivity {
    private String Tag = "HobbyActivity";
    private Context context;
    GridViewAdapter adapter;
    private ArrayList<String> arrayList;

    private Button selectButton;
    LinearLayout container;

    Handler handler = new MessageHandler();
    String selectedItem;
    private static final String[] text = new String[]{
            "농구", "자전거", "캠핑", "사진", "기타",
            "영화", "피아노", "미술", "보드", "보드게임"};
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        this.context = newBase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_hobby);

        container = (LinearLayout)findViewById(R.id.activity_hobby);
        selectButton = (Button) container.findViewById(R.id.select_button);

        loadGridView(container);
        onClickEvent(container);
    }
    private void loadGridView(View view) {
        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        arrayList = new ArrayList<>();
        for (int i = 0; i <10; i++) {
            arrayList.add(text[i]);
        }
        adapter = new GridViewAdapter(context, false);
        gridView.setAdapter(adapter);
    }
    private void onClickEvent(View view) {
        view.findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                if (selectedRows.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedRows.size(); i++) {

                        //Check if selected rows have value i.e. checked item
                        if (selectedRows.valueAt(i)) {

                            //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                            String selectedRowLabel = arrayList.get(selectedRows.keyAt(i));

                            //append the row label text
                            if(i == (selectedRows.size()-1)) {
                                stringBuilder.append(selectedRowLabel);
                            }else {
                                stringBuilder.append(selectedRowLabel + ", ");
                            }
                        }
                    }
                    selectedItem = stringBuilder.toString();
                    Toast.makeText(context, "Selected Rows\n" + selectedItem, Toast.LENGTH_SHORT).show();
                    new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/loginTest.php",
                            "userMail=" + user.getEmail() + "&userName=" + user.getDisplayName() + "&userHobby=" + selectedItem +
                                    "&userImg="+ user.getPhotoUrl()+"&userToken="+FirebaseInstanceId.getInstance().getToken(), "");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }

            }
        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check the current text of Select Button
                if (selectButton.getText().toString().equals(getResources().getString(R.string.select_all))) {

                    //If Text is Select All then loop to all array List items and check all of them
                    for (int i = 0; i < arrayList.size(); i++)
                        adapter.checkCheckBox(i, true);

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.deselect_all));
                } else {
                    //If button text is Deselect All remove check from all items
                    adapter.removeSelection();

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.select_all));
                }
            }
        });
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Log.e(Tag, "obj = "+msg.obj.toString());
        }
    }
}
