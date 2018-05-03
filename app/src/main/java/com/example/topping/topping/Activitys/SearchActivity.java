package com.example.topping.topping.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.topping.topping.Adapters.SearchListViewAdapter;
import com.example.topping.topping.SelectDB;
import com.example.topping.topping.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AbstractActivity {
    private String Tag = "SearchActivity";
    private static int index;
    private static String[] userMail;

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
        new SelectDB(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/search.php", "");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "User : " +userMail[position] + ", index : " +(position+1), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ContentActivity.class));
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
            userMail = new String[jarray.length()];
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                index = jObject.getInt("index");

                userMail[i] = jObject.getString("userMail");
                String hobby = jObject.getString("hobby");
                String fromDate = jObject.getString("fromDate");

                adapter.addItem(null, userMail[i], hobby, fromDate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
