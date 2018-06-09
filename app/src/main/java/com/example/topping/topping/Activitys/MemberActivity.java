package com.example.topping.topping.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topping.topping.Adapters.MemberWriteListViewAdapter;
import com.example.topping.topping.DownloadImageTask;
import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;
import java.util.Timer;

public class MemberActivity extends AbstractActivity {
    private String Tag = "MemberActivity";
    private ImageView imageView;
    private TextView memberEmail, memberName, memberHobby;
    private ListView memberWrite, memberAfter;
    private MemberWriteListViewAdapter adapter = new MemberWriteListViewAdapter();
    Handler handler = new MemberHandler();
    private int index[];

    private String mail, name, userHobby, img;
//    private int index;
    private String hobby;
    private String hobbyDetail;
    private String participant;
    private String get;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        Intent intent = getIntent();
        get = intent.getStringExtra("MemberMail");
        Log.e("get", get+"");
        if(get==null){
            get=userMail;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        imageView = (ImageView)findViewById(R.id.memberImg);
        memberEmail = (TextView)findViewById(R.id.memberMail);
        memberName = (TextView)findViewById(R.id.memberName);
        memberHobby = (TextView)findViewById(R.id.memberHobby);
        memberWrite = (ListView)findViewById(R.id.member_write_listview);
        memberAfter = (ListView)findViewById(R.id.member_after_listview);

        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/member.php","userMail="+get,"");

        memberWrite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "index : " +index[position] + ", position : " +(position), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ContentActivity.class);
                i.putExtra("index",index[position]);
                startActivity(i);
            }
        });

        /*SharedPreferences sp = getApplicationContext().getSharedPreferences("name",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("key", "value");
        editor.remove("key");
        editor.commit();

        String text = sp.getString("key",null);*/
    }


    private class MemberHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
//            Log.e(Tag, "obj = "+msg.obj.toString());
//            Log.e(Tag, "userMail = "+user.getEmail());
            doMemberJSONParser(msg.obj.toString());
        }
    }
    private class MSGHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
    void doMemberJSONParser(String str) {
        StringTokenizer tokens = new StringTokenizer(str);

        String url = tokens.nextToken("|");
        String row = tokens.nextToken("|").toString();

//        Log.e(Tag +" url", url);
//        Log.e(Tag +" data", row);

        String data = row.trim().toString();
        try {
            JSONArray jarray = new JSONArray(data);   // JSONArray 생성
            index = new int[jarray.length()];
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출

                mail = jObject.getString("U.userMail");
                name = jObject.getString("U.userName");
                userHobby = jObject.getString("U.userHobby");
                img = jObject.getString("U.userImg");
                index[i] = jObject.getInt("W.index");
                hobby = jObject.getString("W.hobby");
                hobbyDetail = jObject.getString("W.hobbyDetail");
                participant = jObject.getString("W.participant");


                String hobbys = hobby + "/" + hobbyDetail;
                Log.e(Tag, mail+", "+name+", "+hobbys+", "+hobbyDetail+","+participant);
                adapter.addItem(hobbys, participant);
                if (i==0){
                    memberEmail.setText("아이디 : "+mail);
                    memberName.setText("이름 : "+name);
                    memberHobby.setText("취미 : "+userHobby);
                    Log.e("member", mail+", "+name+", "+userHobby);

                    new DownloadImageTask(imageView).execute(img);
                }
            }
            memberWrite.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
