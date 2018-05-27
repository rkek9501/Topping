package com.example.topping.topping.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Arktic on 2018-04-07.
 */

public class AbstractActivity extends AppCompatActivity {
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static String userMail;
    public String userName;
    public String userImg;
    public static String loginCheck;

    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    public SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public Date today = Calendar.getInstance().getTime();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if(user!=null){
            userMail = user.getEmail();
            userName = user.getDisplayName();
//            userImg = setImageURI(user.getPhotoUrl());
        }else{

        }
    }
}
