package com.example.topping.topping.Activitys;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.topping.topping.R;

public class MemberActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("name",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("key", "value");
        editor.remove("key");
        editor.commit();


        String text = sp.getString("key",null);
    }
}
