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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.topping.topping.Adapters.SearchListViewAdapter;
import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class SearchActivity extends AbstractActivity {
    private String Tag = "SearchActivity";
    private static int[] index;
    private static String userMail;

    private EditText editText;
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
//        getSupportActionBar().
        getSupportActionBar().setIcon(R.drawable.title);

        findBtn = (Button) findViewById(R.id.search_find_btn);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("hobby", String.valueOf(editText.getText()));
                startActivity(intent);
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.searchListview);
        editText = (EditText) findViewById(R.id.search_editText);
        Intent intent = getIntent();
        String get = intent.getStringExtra("hobby" );
        Log.e("get", get+"");
        editText.setText(get);

        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/search.php", "hobby="+get,"");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "index : " +index[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                int sendIndex = index[position];
                intent.putExtra("index",sendIndex);
                startActivity(intent);
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
        StringTokenizer tokens = new StringTokenizer(str);

        String url = tokens.nextToken("|");
        String data = tokens.nextToken("|").toString();

        Log.e(Tag +" url", url);
        Log.e(Tag +" data", data);

        loginCheck = data.trim().toString();
        try {
            JSONArray jarray = new JSONArray(data);   // JSONArray 생성
            index = new int[jarray.length()];
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                index[i] = jObject.getInt("index");

                userMail = jObject.getString("userMail");
                String hobby = jObject.getString("hobby");
                String fromDate = jObject.getString("fromDate");

                adapter.addItem(null, userMail, hobby, fromDate);
                Log.e("JSON",index[i]+", "+userMail+", "+hobby+", "+fromDate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
