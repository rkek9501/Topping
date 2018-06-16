package com.example.topping.topping.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topping.topping.DownloadImageTask;
import com.example.topping.topping.NotifiyDialog;
import com.example.topping.topping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.soyu.soyulib.soyuHttpTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

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
//    Handler handler2 = new PushHandler();
    FloatingActionButton fab;
    Button logoutBtn;
    String edit;
    LinearLayout sportimg;
    LinearLayout travelimg;
    LinearLayout musicimg;
    LinearLayout movieimg;
    LinearLayout cameraimg;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("topping",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user",user.getEmail());
//        editor.remove("user");
        editor.commit();

//        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/timeCheck.php", "userMail="+userMail, "");

        editText = (EditText)findViewById(R.id.mainEditText);
        findBtn = (Button)findViewById(R.id.main_find_btn);
        logoutBtn = (Button)findViewById(R.id.log_out);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        sportimg = (LinearLayout) findViewById(R.id.sportimg);
        travelimg = (LinearLayout) findViewById(R.id.travelimg);
        musicimg = (LinearLayout) findViewById(R.id.musicimg);
        movieimg = (LinearLayout) findViewById(R.id.moiveimg);
        cameraimg = (LinearLayout) findViewById(R.id.cameraimg);

        findBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        sportimg.setOnClickListener(this);
        travelimg.setOnClickListener(this);
        musicimg.setOnClickListener(this);
        movieimg.setOnClickListener(this);
        cameraimg.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        Log.e("handler : ", handler+"");
        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/index.php","");

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
        }else if (id == R.id.write) {
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
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("hobby",String.valueOf(editText.getText()));
            startActivity(intent);

        }else if(v==fab){
            startActivity(new Intent(getApplicationContext(), FindActivity.class));
        }else if(v==logoutBtn){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
            user.delete();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        else if(v==sportimg){
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("hobby","운동");
            startActivity(intent);
        }
        else if(v==travelimg){
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("hobby","여행");
            startActivity(intent);
        }
        else if(v==musicimg){
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("hobby","음악");
            startActivity(intent);
        }
        else if(v==movieimg){
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("hobby","영화");
            startActivity(intent);
        }
        else if(v==cameraimg){
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("hobby","사진");
            startActivity(intent);
        }
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Log.e(Tag, "obj = "+msg.obj.toString());
//            FCMJSONParser(msg.obj.toString());
        }
    }
}
