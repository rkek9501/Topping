package com.example.topping.topping;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.soyu.soyulib.soyuHttpTask;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

public class SearchActivity extends AbstractActivity {
    private String Tag = "SearchActivity";

    private Button findBtn;
    private ListView listView;
    Handler handler = new SearchHandler();
    SearchListViewAdapter adapter = new SearchListViewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        findBtn = (Button) findViewById(R.id.search_find_btn);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContentActivity.class));
            }
        });

//        LinearLayout container = (LinearLayout) findViewById(R.id.searchParent);
//        View listitem = View.inflate(getApplicationContext(), R.layout.listview_item, container);

        listView = (ListView) findViewById(R.id.searchListview);
        new BackgroundWorker(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/search.php", "");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "User ", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(adapter);

    }

    private class SearchHandler extends Handler {
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
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String index = jObject.getString("index");
                String userMail = jObject.getString("userMail");
                String hobby = jObject.getString("hobby");
                String fromDate = jObject.getString("fromDate");

                adapter.addItem(null, userMail, hobby, fromDate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
