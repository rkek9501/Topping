package com.example.topping.topping.Activitys;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.StringTokenizer;

public class FindActivity extends AbstractActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private String Tag = "FindActivity";
    private Button submitBtn,modifyBtn;
    private Spinner category1,category2;

    private EditText detail;
    private LinearLayout dateFrom, dateTo;
    private TextView place, dateFromTime,dateFromDate, dateToTime, dateToDate;
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

    private int getIndex;
    private int REQUEST_TEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_find);

        initView();
        initListener();


        Intent intent = getIntent();
        getIndex = intent.getIntExtra("indexData",0);
        if(getIndex!=0){
            new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/content.php","index="+getIndex, "");
            submitBtn.setVisibility(View.GONE);
            modifyBtn.setVisibility(View.VISIBLE);
        }else {
            submitBtn.setVisibility(View.VISIBLE);
            modifyBtn.setVisibility(View.GONE);
        }
    }

    private void initView(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        modifyBtn = (Button) findViewById(R.id.modifyBtn);
        submitBtn = (Button)findViewById(R.id.submitBtn);
        category1 = (Spinner)findViewById(R.id.category1);
        category2 = (Spinner)findViewById(R.id.category2);
        place = (TextView)findViewById(R.id.place);
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
        modifyBtn.setOnClickListener(this);
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
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),MapActivity.class), REQUEST_TEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TEST) {
            if(resultCode == RESULT_OK) {
                place.setText(data.getStringExtra("result"));
            }else {
                place.setText("장소를 다시 정해주세요.");
            }
        }
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
            if(place.getText().equals("장소를 다시 정해주세요.") || place.getText().equals("클릭하여 장소를 정해주세요.")) {
                Toast.makeText(getApplicationContext(), "장소가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }else {
                setSubmitDialog();

            }
        }else if(v==modifyBtn){
            setModifyDialog();
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
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("hobby",hobby);
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
    private void setModifyDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("수정된 내용을 저장하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hobby = category1.getSelectedItem().toString();
                String hobbyDetail = category2.getSelectedItem().toString();
                String placeText = place.getText().toString();
                String fromDate = dateFromDate.getText() + " " + dateFromTime.getText();
                String toDate = dateToDate.getText() + " " + dateToTime.getText();
                String detailText = detail.getText().toString();
                new soyuHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/modify.php",
                        "index="+getIndex+"&userMail=" + user.getEmail() + "&hobby=" + hobby+ "&hobbyDetail=" + hobbyDetail+ "&place=" + placeText+
                                "&fromDate=" + fromDate+ "&toDate=" + toDate+ "&detail=" + detailText, "");
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("hobby",hobby);
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
            doJSONParser(msg.obj.toString());
        }
    }
    void doJSONParser(String str) {
        StringTokenizer tokens = new StringTokenizer(str);

        String url = tokens.nextToken("|").trim().toString();
        String data = tokens.nextToken("|").toString();

        Log.e(Tag + " url", url);
        Log.e(Tag + " data", data);

        if (url.equals("http://61.84.24.188/topping3/content.php")) {
            try {
                JSONArray jarray = new JSONArray(data);   // JSONArray 생성
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출

                    int indexData = jObject.getInt("index");
                    String userMailData = jObject.getString("userMail");
                    String hobbyData = jObject.getString("hobby");
                    String hobbyDetailData = jObject.getString("hobbyDetail");
                    String placeData = jObject.getString("place");
                    String fromDateData = jObject.getString("fromDate");
                    String toDateData = jObject.getString("toDate");
                    String detailData = jObject.getString("detail");
                    int participantData = jObject.getInt("participant");
                    String membersData = jObject.getString("members");

                    timePaser(fromDateData, toDateData);

                    this.detail.setText(detailData);
                    this.place.setText(placeData);
                    Log.e("JSON", indexData + ", " + userMailData + ", " + hobbyDetailData + ", " + fromDateData + ", " + place + ", " + membersData + ", " + participantData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(url.equals("http://61.84.24.188/topping3/modify.php")){
            if(data.equals("successModify"))
                Toast.makeText(getApplicationContext(),"글이 수정 되었습니다.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"글이 수정 되지않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private void timePaser(String from, String to){
        StringTokenizer fromtk = new StringTokenizer(from);
        StringTokenizer totk = new StringTokenizer(to);
        String date1 = fromtk.nextToken(" ").trim().toString();
        String time1 = fromtk.nextToken(" ").substring(0,5).trim().toString();
        String date2 = totk.nextToken(" ").trim().toString();
        String time2 = totk.nextToken(" ").substring(0,5).trim().toString();

        this.dateFromDate.setText(date1);
        this.dateToDate.setText(date2);
        this.dateFromTime.setText(time1);
        this.dateToTime.setText(time2);
    }
}
