package com.example.topping.topping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.topping.topping.Activitys.ContentActivity;
import com.example.topping.topping.Adapters.MemberWriteListViewAdapter;
import com.soyu.soyulib.soyuHttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

public class NotifiyDialog extends DialogFragment implements View.OnClickListener {
    private String Tag = "NotifyDialogFragment";
    Button cancle;
    ListView listView;
    MemberWriteListViewAdapter adapter;
    Handler handler = new ListviewHandler();

    private static int[] index;
    private String mail, name, userHobby, img;
    private String hobby;
    private String hobbyDetail;
    private String participant;
    public NotifiyDialog(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notify_listview, container, false);

        SharedPreferences sp = getContext().getSharedPreferences("topping",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String userMail = sp.getString("user",null);

        cancle = (Button)view.findViewById(R.id.listview_cancle);
        cancle.setOnClickListener(this);
        listView = (ListView)view.findViewById(R.id.notify_listview);
        adapter = new MemberWriteListViewAdapter();
//        listView.setAdapter(adapter);
        new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/member.php","userMail="+ userMail,"");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "index : " + index[position] + ", position : " + (position), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ContentActivity.class);
                i.putExtra("index", index[position]);
                startActivity(i);
            }
        });

        /*setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //새로운 페이지 연결
                *//*Toast.makeText(getContext(), "index : " +index + ", position : " +(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ContentActivity.class);
                int sendIndex = index[position];
                intent.putExtra("index",sendIndex);
                startActivity(intent);*//*
            }
        });*/
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.dialog_fragment_width);
        int dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public void onClick(View v) {
        if(v==cancle){
            dismiss();
        }
    }

    private class ListviewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doListviewJSONParser(msg.obj.toString());
        }
    }

    private void doListviewJSONParser(String s) {
        StringTokenizer tokens = new StringTokenizer(s);

        String url = tokens.nextToken("|").toString();
        String row = tokens.nextToken("|").toString();
        try {
            JSONArray jarray = new JSONArray(row);   // JSONArray 생성
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
            }
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
