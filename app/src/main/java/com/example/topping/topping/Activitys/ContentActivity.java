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
import android.widget.Button;
import android.widget.TextView;

import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class ContentActivity extends AbstractActivity implements View.OnClickListener {
    private String Tag = "ContentActivity";
    private Button requestBtn, changeBtn;
    private TextView title, date, time, detail, place;
    private AlertDialog.Builder builder;

    private static int[] index;
//    private static String[] userMail;

    Handler handler = new ContentHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        int get = intent.getIntExtra("index",0);
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
                String userMail = jObject.getString("userMail");
                String hobby = jObject.getString("hobby");
                String hobbyDetail = jObject.getString("hobbyDetail");
                String place = jObject.getString("place");
                String fromDate = jObject.getString("fromDate");
                String toDate = jObject.getString("toDate");
                String detail = jObject.getString("detail");

                timePaser(fromDate, toDate);

                this.title.setText(hobby+"/"+hobbyDetail);
                this.detail.setText(detail);
                this.place.setText(place);

                Log.e("JSON",index+", "+userMail+", "+hobby+", "+fromDate+", "+place);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void timePaser(String from, String to){
        StringTokenizer fromtk = new StringTokenizer(from);
        StringTokenizer totk = new StringTokenizer(to);
        String date1 = fromtk.nextToken(" ").trim().toString();
        String time1 = fromtk.nextToken(" ").substring(0,5).trim().toString();
        String date2 = totk.nextToken(" ").trim().toString();
        String time2 = totk.nextToken(" ").substring(0,5).trim().toString();

        this.date.setText(date1+" ~ "+date2);
        this.time.setText(time1+" ~ "+time2);
    }
}

