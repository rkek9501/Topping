package com.example.topping.topping.Activitys;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

import java.util.Calendar;

public class FindActivity extends AbstractActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private String Tag = "FindActivity";
    private Button submitBtn;
    private Spinner category1,category2;

    private EditText place, detail;
    private LinearLayout dateFrom, dateTo;
    private TextView dateFromTime,dateFromDate, dateToTime, dateToDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private  int year, month, day, hour, minute;
    int dateCheck;
    final int dateCheckFrom = 1;
    final int dateCheckTo = 2;

    private CheckBox checkBox;
    private boolean check;

    private AlertDialog.Builder builder;
    private Handler handler = new MessageHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        initView();
        initListener();
    }

    private void initView(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        submitBtn = (Button)findViewById(R.id.submitBtn);
        category1 = (Spinner)findViewById(R.id.category1);
        category2 = (Spinner)findViewById(R.id.category2);
        place = (EditText)findViewById(R.id.place);
        detail = (EditText)findViewById(R.id.detail);
        checkBox = (CheckBox)findViewById(R.id.dateCheckBox);

        dateFrom = (LinearLayout) findViewById(R.id.datePickerFrom);
        dateFromDate = (TextView)findViewById(R.id.datePickerFromDate);
        dateFromTime = (TextView)findViewById(R.id.datePickerFromTime);
        dateTo = (LinearLayout) findViewById(R.id.datePickerTo);
        dateToDate = (TextView)findViewById(R.id.datePickerToDate);
        dateToTime = (TextView)findViewById(R.id.datePickerToTime);

        dateFromDate.setText(dateFormat.format(today));
        dateToDate.setText(dateFormat.format(today));
        dateFromTime.setText(timeFormat.format(today));
        dateToTime.setText(timeFormat.format(today));

    }

    private void initListener(){
        submitBtn.setOnClickListener(this);
        category1.setOnItemSelectedListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check = isChecked;
                if(isChecked){
                    dateToDate.setText(dateFromDate.getText());
                }else {
                    Toast.makeText(getApplicationContext(),"종료일을 정해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dateFrom.setOnClickListener(this);
        dateTo.setOnClickListener(this);
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                view = new DatePicker(getApplicationContext());

                String date = year + "." + changeInt(month) + "." + changeInt(dayOfMonth);
                if(dateCheck== dateCheckFrom){
                    dateFromDate.setText(date);
                    if(check==true){
                        dateToDate.setText(date);
                    }
                }else if(dateCheck == dateCheckTo){
                    if (check==false){
                        dateToDate.setText(date);
                    }
                }
            }
        };
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = changeInt(hourOfDay)+":"+changeInt(minute);
                if(dateCheck== dateCheckFrom){
                    dateFromTime.setText(time);
                }else if(dateCheck == dateCheckTo){
                    dateToTime.setText(time);
                }
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private String changeInt(int num){
        String numString = String.valueOf(num);
        if(num<10){
            numString = "0"+num;
            return numString;
        }
        return numString;
    }
    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);

        DatePickerDialog dateDialog = new DatePickerDialog(this, android.R.style.ThemeOverlay_Material_Dialog,
                dateSetListener, year, month, day);
        TimePickerDialog timeDialog = new TimePickerDialog(this,
                timeSetListener,hour, minute, false);

        if(v==submitBtn){
            setSubmitDialog();
        }
        if((v==dateFrom)||(v==dateTo)){
            if(v==dateFrom){
                dateCheck = dateCheckFrom;
                timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timeDialog.show();
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dateDialog.show();
            }else if(v==dateTo){
                dateCheck = dateCheckTo;
                timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timeDialog.show();
                if(!check){
                    dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dateDialog.show();
                }
            }
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
                String fromDate = dateFromDate.getText() + " " + dateFromTime.getText();
                String toDate = dateToDate.getText() + " " + dateToTime.getText();
                String detailText = detail.getText().toString();
                new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/write.php",
                        "userMail=" + user.getEmail() + "&hobby=" + hobby+ "&hobbyDetail=" + hobbyDetail+ "&place=" + placeText+
                        "&fromDate=" + fromDate+ "&toDate=" + toDate+ "&detail=" + detailText, "");

                Toast.makeText(getApplicationContext(),"글이 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
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
