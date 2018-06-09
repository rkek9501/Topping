package com.example.topping.topping.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topping.topping.Adapters.HorizontalListViewAdapter;
import com.example.topping.topping.GoogleMapFragment;
import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ContentActivity extends AbstractActivity implements View.OnClickListener {
    private String Tag = "ContentActivity";
    private Button requestBtn, changeBtn, cancleBtn, deleteBtn;
    private TextView title, date, time, detail, place;
    private LinearLayout writerBtn;
    private AlertDialog.Builder builder;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mMails = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private static int[] index;
    private boolean joinCheck = false;
    int get;
    private String outString;

    private int indexData;
    private String userMailData;
    private String hobbyData;
    private String hobbyDetailData;
    private String placeData;
    private String fromDateData;
    private String toDateData;
    private String detailData;
    private int participantData;
    private String membersData;

    Handler handler = new ContentHandler();

    private ViewGroup mapLayout;

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
        cancleBtn = (Button)findViewById(R.id.cancleBtn);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        requestBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        writerBtn = (LinearLayout)findViewById(R.id.writerBtn);

        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/content.php","index="+get, "");
    }

    @Override
    public void onClick(View v) {
        if(v==requestBtn){
            setRequsetDialog();
        }else  if(v==changeBtn){
            setChangeDialog();
        }else if(v==cancleBtn){
            setCancleDialog();
        }else if(v==deleteBtn){
            setDeleteDialog();
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
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("index",get);
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                intent.putExtra("indexData",indexData);
                startActivity(intent);
                finish();
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
    private void setDeleteDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("내용을 삭제 하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/contentDelete.php","index="+get, "");
                finish();
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
    private void setCancleDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("현재 참여중인 매칭에서 나가시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(joinCheck){
                    new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/getout.php","index="+get+"&members="+outString+"&participant="+(participantData-1), "");
                    finish();
                }

            }
        });
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

                    indexData = jObject.getInt("index");
                    userMailData = jObject.getString("userMail");
                    hobbyData = jObject.getString("hobby");
                    hobbyDetailData = jObject.getString("hobbyDetail");
                    placeData = jObject.getString("place");
                    fromDateData = jObject.getString("fromDate");
                    toDateData = jObject.getString("toDate");
                    detailData = jObject.getString("detail");
                    participantData = jObject.getInt("participant");
                    membersData = jObject.getString("members");

                    timePaser(fromDateData, toDateData);
                    memberPaser(membersData, participantData, userMailData);
                    MapSetting(placeData);

                    this.title.setText(hobbyData+"/"+hobbyDetailData);
                    this.detail.setText(detailData);
                    this.place.setText(placeData);
                    Log.e("JSON",indexData+", "+userMailData+", "+hobbyDetailData+", "+fromDateData+", "+place+", "+membersData+", "+participantData);
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

                    String memberMail = jObject.getString("userMail");
                    String userName = jObject.getString("userName");
                    String userImg = jObject.getString("userImg");

                    mImageUrls.add(userImg);
                    mMails.add(memberMail);
                    mNames.add(userName);

                    Log.e("JSON","content2");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initRecyclerView();
        }else if(url.equals("http://61.84.24.188/topping3/contentInsert.php")){
            if(data.equals("successInsert"))
                Toast.makeText(getApplicationContext(),"매칭 되었습니다.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"매칭 실패 되었습니다.", Toast.LENGTH_SHORT).show();
        }else if(url.equals("http://61.84.24.188/topping3/getout.php")){
            if(data.equals("successGetOut"))
                Toast.makeText(getApplicationContext(),"매칭 취소가 되었습니다.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"매칭 취소가 실패 되었습니다.", Toast.LENGTH_SHORT).show();
        }else if(url.equals("http://61.84.24.188/topping3/contentDelete.php")){
            if(data.equals("successDelete"))
                Toast.makeText(getApplicationContext(),"매칭이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"매칭 삭제가 실패 되었습니다.", Toast.LENGTH_SHORT).show();
        }

    }
    private void memberPaser(String members, int participant, String writer) {
        Log.e("content2 aa", members + ", " + participant);
        StringTokenizer tokens = new StringTokenizer(members);
        String member[] = new String[participant];
        String returnString = "";
        outString = "";
        String CheckMember = null;
        for (int i = 0; i < participant; i++) {
            member[i] = tokens.nextToken(",").trim().toString();
            if (i > 0){
                if(member[i].equals(userMail))
                    CheckMember = member[i];
            }
            Log.e("content for + " + i, member[i]);
        }
        for (int i = 0; i < participant; i++) {
            if (i == (participant - 1)) {
                if (CheckMember!=null) {
                    if (!CheckMember.equals(member[i]))
                        outString += member[i] + ",";
                }
                returnString += "`userMail`='" + member[i] + "'";
            } else {
                if (CheckMember!=null) {
                    if (!CheckMember.equals(member[i]))
                        outString += member[i] + ",";
                }
                returnString += "`userMail`='" + member[i] + "' || ";
            }
        }
        WriterCheck(writer,CheckMember);

        Log.e("content2 bb",returnString);
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
        HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(this, mNames, mImageUrls,mMails);
        recyclerView.setAdapter(adapter);
    }
    private void MapSetting(String place){
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Place", place);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragmentHere, fragment);
        transaction.commit();
    }
    private void WriterCheck(String writer, String memberMail){
        if(userMail.equals(writer)){
            setChangeBtn();
        }else if(userMail.equals(memberMail)) {
            setCancleBtn();
            if(!writer.equals(memberMail)){
                joinCheck=true;
            }
        }else {
            setRequestBtn();
        }
    }
    private void setChangeBtn(){
        requestBtn.setVisibility(View.GONE);
        writerBtn.setVisibility(View.VISIBLE);
        cancleBtn.setVisibility(View.GONE);
    }
    private void setRequestBtn(){
        requestBtn.setVisibility(View.VISIBLE);
        writerBtn.setVisibility(View.GONE);
        cancleBtn.setVisibility(View.GONE);
    }
    private void setCancleBtn(){
        requestBtn.setVisibility(View.GONE);
        writerBtn.setVisibility(View.GONE);
        cancleBtn.setVisibility(View.VISIBLE);
    }
}