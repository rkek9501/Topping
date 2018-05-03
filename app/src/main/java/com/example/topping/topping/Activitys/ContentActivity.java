package com.example.topping.topping.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.topping.topping.Adapters.ContentListViewAdapter;
import com.example.topping.topping.Adapters.SearchListViewAdapter;
import com.example.topping.topping.R;
import com.example.topping.topping.SelectDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// public class ContentActivity extends AbstractActivity implements View.OnClickListener {
public class ContentActivity extends AbstractActivity {
    // private Button requestBtn, changeBtn;
    // private AlertDialog.Builder builder;
    // private ListView listView;
    private String Tag = "ContentActivity";
    private static int index;
    private static String[] userMail;

    Handler handler = new ContentHandler();
    ContentListViewAdapter adapter = new ContentListViewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
/*
        requestBtn = (Button)findViewById(R.id.requestBtn);
        changeBtn = (Button)findViewById(R.id.changeBtn);
        requestBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
*/

        Intent i = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        //listView = (ListView) findViewById(R.id.searchListview);
        new SelectDB(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/content.php", "");
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "User : " +userMail[position] + ", index : " +(position+1), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ContentActivity.class));
            }
        });

        listView.setAdapter(adapter);
*/
    }

    private class ContentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(Tag, "obj = " + msg.obj.toString());
            String data = msg.obj.toString();

            doJSONParser(data);
        }
    }

    void doJSONParser(String str) {
        StringBuffer sb = new StringBuffer();

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            getIntent().getExtras().getInt("idx");
                JSONObject jObject = jarray.getJSONObject(1);  // JSONObject 추출
                index = jObject.getInt("index");

                String userMail = jObject.getString("userMail");
                String hobby = jObject.getString("hobby");
                String hobbyDetail = jObject.getString("hobbyDetail");
                String place = jObject.getString("place");
                String fromDate = jObject.getString("fromDate");
                String toDate = jObject.getString("toDate");
                String detail = jObject.getString("detail");
                adapter.addItem(null, userMail, hobby, detail);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/*
    @Override
    public void onClick(View v) {
        if(v==requestBtn){
            setRequsetDialog();
        }else  if(v==changeBtn){
            setChangeDialog();
        }
    }

    private void setRequsetDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("매칭 신청 하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
            }
        });
//        builder.setNegativeButton("아니오", null);
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
    private void setChangeDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("내용을 수정하시겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
        });
//        builder.setNegativeButton("아니오", null);
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
*/
}
