package com.example.topping.topping.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.topping.topping.Adapters.HorizontalListViewAdapter;
import com.example.topping.topping.DownloadImageTask;
import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

import net.daum.mf.map.api.MapView;
import net.daum.android.map.MapViewEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class ContentActivity extends AbstractActivity implements View.OnClickListener, MapViewEventListener {
    private String Tag = "ContentActivity";
    private Button requestBtn, changeBtn;
    private TextView title, date, time, detail, place;
    private AlertDialog.Builder builder;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private static int[] index;
//    private static String[] userMail;
    int get;
    Handler handler = new ContentHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        get = intent.getIntExtra("index",0);
        Log.e("get", get+"");
        title = (TextView)findViewById(R.id.content_title);
        date = (TextView)findViewById(R.id.content_date);
        time = (TextView)findViewById(R.id.content_time);
        detail = (TextView)findViewById(R.id.content_detail);
        place = (TextView)findViewById(R.id.content_place);
        requestBtn = (Button)findViewById(R.id.requestBtn);
        changeBtn = (Button)findViewById(R.id.changeBtn);
        requestBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);

//        MapView mapView = new MapView(this);
//        net.daum.mf.map.api.MapView mapView = new net.daum.mf.map.api.MapView(this);

//        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.map_view);
//        mapViewContainer.addView(mapView);
//        mapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
//        mapView.setPOIItemEventListener(this);
        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/content.php","index="+get, "");

    }

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
                Log.e("content3",get +", "+userMail);
                new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/contentInsert.php","index="+get+"&memberName=, "+userMail, "");
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

    @Override
    public void onLoadMapView() {

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
        StringTokenizer tokens = new StringTokenizer(str);

        String url = tokens.nextToken("|").trim().toString();
        String data = tokens.nextToken("|").toString();

        Log.e(Tag +" url", url);
        Log.e(Tag +" data", data);

        if(url.equals("http://61.84.24.188/topping3/content.php")){
            try {
                JSONArray jarray = new JSONArray(data);   // JSONArray 생성
                index = new int[jarray.length()];
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출

                    index[i] = jObject.getInt("index");
                    String userMail = jObject.getString("userMail");
                    String hobby = jObject.getString("hobby");
                    String hobbyDetail = jObject.getString("hobbyDetail");
                    String place = jObject.getString("place");
                    String fromDate = jObject.getString("fromDate");
                    String toDate = jObject.getString("toDate");
                    String detail = jObject.getString("detail");
                    int participant = jObject.getInt("participant");
                    String members = jObject.getString("members");

                    timePaser(fromDate, toDate);
                    memberPaser(members, participant);

                    this.title.setText(hobby+"/"+hobbyDetail);
                    this.detail.setText(detail);
                    this.place.setText(place);
                    Log.e("JSON",index+", "+userMail+", "+hobby+", "+fromDate+", "+place+", "+members+", "+participant);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(url.equals("http://61.84.24.188/topping3/content2.php")){
            try {
                JSONArray jarray = new JSONArray(data);   // JSONArray 생성
//            index = new int[jarray.length()];
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출

                    String userMail = jObject.getString("userMail");
                    String userName = jObject.getString("userName");
                    String userImg = jObject.getString("userImg");

                    mImageUrls.add(userImg);
                    mNames.add(userName);

                    Log.e("JSON","content2");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initRecyclerView();
        }
    }
    void doJSONParser2(String str) {
        StringTokenizer tokens = new StringTokenizer(str);

        String url = tokens.nextToken("|").trim().toString();
        String data = tokens.nextToken("|").toString();

        Log.e(Tag +" url", url);
        Log.e(Tag +" data", data);


    }
    private void memberPaser(String members, int participant){
        StringTokenizer tokens = new StringTokenizer(members);
        String member[] = new String[participant];
        String returnString="";
        for (int i=0; i<participant; i++){
            member[i] = tokens.nextToken(",").trim().toString();
            if (i==(participant-1)){
                returnString +="`userMail`='"+member[i]+"'";
            }else {
                returnString +="`userMail`='"+member[i]+"' || ";
            }
        }
        Log.e("content2",returnString);
        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/content2.php","members="+returnString.toString(), "");
    }
    private void timePaser(String from, String to){
        StringTokenizer fromtk = new StringTokenizer(from);
        StringTokenizer totk = new StringTokenizer(to);
        String date1 = fromtk.nextToken(" ").trim().toString();
        String time1 = fromtk.nextToken(" ").substring(0,5).trim().toString();
        String date2 = totk.nextToken(" ").trim().toString();
        String time2 = totk.nextToken(" ").substring(0,5).trim().toString();

        if(date1.equals(date2)){
            this.date.setText(date1);
        }else {
            this.date.setText(date1+" ~ "+date2);
        }
        this.time.setText(time1+" ~ "+time2);
    }
    private void initRecyclerView(){
        Log.d(Tag, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }
}