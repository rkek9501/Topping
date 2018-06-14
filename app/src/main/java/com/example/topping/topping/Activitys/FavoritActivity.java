package com.example.topping.topping.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.topping.topping.Adapters.FavoritGridViewAdapter;
import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class FavoritActivity extends AbstractActivity {

    private String Tag = "FavoritActivity";
    private GridView gridView;
    private String otherMail[];
    FavoritGridViewAdapter adapter = new FavoritGridViewAdapter();
    Handler handler = new FavoritHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/myFavorit.php", "userMail="+userMail,"");
        gridView = (GridView)findViewById(R.id.favoritGridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MemberActivity.class);
                intent.putExtra("MemberMail",otherMail[position]);
                startActivity(intent);
            }
        });
    }
    private class FavoritHandler extends Handler {
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

        try {
            JSONArray jarray = new JSONArray(data);   // JSONArray 생성
            otherMail = new String[jarray.length()];
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출

                otherMail[i] = jObject.getString("U.userMail");
                String otherName = jObject.getString("U.userName");
                String otherHobby = jObject.getString("U.userHobby");
                String otherImg = jObject.getString("U.userImg");

                adapter.addItem(otherName,otherHobby,otherImg, otherMail[i]);
                Log.e(Tag, otherName+", "+otherHobby+", "+otherImg+", "+otherMail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gridView.setAdapter(adapter);
    }
}
