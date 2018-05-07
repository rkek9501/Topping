package com.example.topping.topping.Activitys;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

import java.util.Date;

public class FindActivity extends AbstractActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private String Tag = "FindActivity";
    private Button submitBtn;
    private Spinner category1,category2;

    private EditText place, detail;
    private TextView datePicker;

    private AlertDialog.Builder builder;
    private Handler handler = new MessageHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        submitBtn = (Button)findViewById(R.id.submitBtn);
        category1 = (Spinner)findViewById(R.id.category1);
        category2 = (Spinner)findViewById(R.id.category2);
        place = (EditText)findViewById(R.id.place);
        detail = (EditText)findViewById(R.id.detail);
        datePicker = (TextView)findViewById(R.id.datePicker);

        submitBtn.setOnClickListener(this);
        category1.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==submitBtn){
            setSubmitDialog();
        }
    }

    private void setSubmitDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("매칭 신청 하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hobby = category1.getSelectedItem().toString();
                String hobbyDetail = category2.getSelectedItem().toString();
                String placeText = place.getText().toString();
                String fromDate = "2018-05-07";
                String toDate ="2018-05-07";
                String detailText = detail.getText().toString();
                new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/write.php",
                        "userMail=" + user.getEmail() + "&hobby=" + hobby+ "&hobbyDetail=" + hobbyDetail+ "&place=" + placeText+
                        "&fromDate=" + fromDate+ "&toDate=" + toDate+ "&detail=" + detailText, "");
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                subAdapter(R.array.sport);
                break;
            case 1:
                subAdapter(R.array.travel);
                break;
            case 2:
                subAdapter(R.array.study);
                break;
            case 3:
                subAdapter(R.array.movie);
                break;
            case 4:
                subAdapter(R.array.art);
                break;
            case 5:
                subAdapter(R.array.music);
                break;
            case 6:
                subAdapter(R.array.ect);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void subAdapter(int itemNum) {
        ArrayAdapter<CharSequence> subAdapter;
        subAdapter = ArrayAdapter.createFromResource(this,
                itemNum, android.R.layout.simple_spinner_item);
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category2.setAdapter(subAdapter);
    }
    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Log.e(Tag, "obj = "+msg.obj.toString());
        }
    }
}
