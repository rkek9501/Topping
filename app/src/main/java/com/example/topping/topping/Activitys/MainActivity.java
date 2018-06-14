package com.example.topping.topping.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topping.topping.DownloadImageTask;
import com.example.topping.topping.FCM.FCMPush;
import com.example.topping.topping.FCM.MyFirebaseMessagingService;
import com.example.topping.topping.NotifiyDialog;
import com.example.topping.topping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.soyu.soyulib.soyuHttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MainActivity extends AbstractActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG_JSON="topping";
    private static final String TAG_USER_MAIL = "userMail";
    private static final String TAG_USER_NAME = "userName";
    private static final String TAG_USER_IMG ="userImg";

    private String Tag = "MainActivity";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;

    EditText editText;
    Button findBtn;
    Handler handler = new MessageHandler();
    Handler handler2 = new PushHandler();
    FloatingActionButton fab;
    Button logoutBtn;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("topping",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user",user.getEmail());
//        editor.remove("user");
        editor.commit();

//        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/timeCheck.php", "userMail="+userMail, "");


        editText = (EditText)findViewById(R.id.main_editText);
        findBtn = (Button)findViewById(R.id.main_find_btn);
        logoutBtn = (Button)findViewById(R.id.log_out);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        findBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        fab.setOnClickListener(this);

        Log.e("handler : ", handler+"");
        //new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/index.php","");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_reorder_black_24dp);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = View.inflate(this,R.layout.nav_header_main,navigationView);
        TextView googleId = (TextView)view.findViewById(R.id.googleId);
        TextView googleEmail = (TextView)view.findViewById(R.id.googleEmail);
        ImageView googleImg = (ImageView)view.findViewById(R.id.googleImg);
        googleId.setText(user.getDisplayName());
        googleEmail.setText(user.getEmail());
//        googleImg.setImageURI(user.getPhotoUrl());

        new DownloadImageTask(googleImg)
                .execute(String.valueOf(user.getPhotoUrl()));

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {
            NotifiyDialog dialog = new NotifiyDialog();
            dialog.show(getSupportFragmentManager(),"dialog");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.member_info) {
            intent = new Intent(getApplicationContext(), MemberActivity.class);
        } else if (id == R.id.like) {
            intent = new Intent(getApplicationContext(), FavoritActivity.class);
        } else if (id == R.id.search) {

        } else if (id == R.id.write) {
            intent = new Intent(getApplicationContext(), FindActivity.class);
        }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v==findBtn){
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }else if(v==fab){
            startActivity(new Intent(getApplicationContext(), FindActivity.class));
        }else if(v==logoutBtn){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
            user.delete();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Log.e(Tag, "obj = "+msg.obj.toString());
            FCMJSONParser(msg.obj.toString());
        }
    }
    void FCMJSONParser(String str) {
        StringTokenizer tokens = new StringTokenizer(str);

        String url = tokens.nextToken("|");
        String data = tokens.nextToken("|").toString();

        Log.e(Tag +" url", url);
        Log.e(Tag +" data", data);

        try {
            JSONArray jarray = new JSONArray(data);   // JSONArray 생성
//            index = new int[jarray.length()];
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
//                index[i] = jObject.getInt("index");
                String fromDate = jObject.getString("fromDate");
                int msgCheck = jObject.getInt("FCM");
                String token = "cbpkYo9cF-M:APA91bE2uTBKuN8DAj8YkJ_JB5ZnuFq_Ql2G72hRqtzWonMjxiXw8ggHFQrOQY2RCKwL0gjn9hv49SMOQdghpkj-9jeYz8KsZR-L9bXWxVD_VFqEJZwZhTL2HbSFiohB4ZEeDLOKnn-3";
                new FCMPush(handler2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,token,"");
                /*
                if(msgCheck == 0){
                    if (timeCheck(fromDate)) {
                        new Thread(){
                            public void run(){
                                try {
                                    MyFirebaseMessagingService.PushFCM("cbpkYo9cF-M:APA91bE2uTBKuN8DAj8YkJ_JB5ZnuFq_Ql2G72hRqtzWonMjxiXw8ggHFQrOQY2RCKwL0gjn9hv49SMOQdghpkj-9jeYz8KsZR-L9bXWxVD_VFqEJZwZhTL2HbSFiohB4ZEeDLOKnn-3");
                                    Log.e(Tag, "PushFCM(token)");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(Tag, "MyFirebaseMessagingService.PushFCM(token) ERR");
                                }
                            }
                        }.start();

                    }
                }*/

                Log.e("JSON",fromDate + ", "+msgCheck);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public boolean timeCheck(String date){
        Calendar currentDate =Calendar.getInstance();
        String curr = df.format(currentDate.getTime());
        StringTokenizer st = new StringTokenizer(date);

        String dates = st.nextToken(" ").toString();
        String times = st.nextToken(" ").toString();

        Log.e("dates", dates);
        Log.e("curr", curr);
//        String gets = df.format(date);
        if(curr.equals(dates)){
            Log.e(Tag, "true");
            return  true;
        }else {
            Log.e(Tag, "false");
            return false;
        }
    }
    private class PushHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Log.e(Tag, "PushHandler obj = "+msg.obj.toString());
        }
    }
}
