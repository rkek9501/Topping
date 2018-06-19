package com.example.topping.topping.Activitys;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.topping.topping.R;
import com.soyu.soyulib.soyuHttpTask;

public class IntroActivity extends AbstractActivity {
    private String Tag = "IntroActivity";
//    Handler logingHandler = new MessageHandler();
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            // 3초뒤에 다음화면(MainActivity)으로 넘어가기 Handler 사용
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent); // 다음화면으로 넘어가기
            finish(); //Intro Activity 화면 제거
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

//        new soyuHttpTask(logingHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/loginCheck.php", "userMail="+userMail, "");

    }




    @Override
    protected void onResume() {
        super.onResume();
        // 다시 화면에 들어어왔을 때 예약 걸어주기
        handler.postDelayed(r, 2000); // 2초 뒤에 Runnable 객체 수행
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 화면을 벗어나면, handler 에 예약해놓은 작업 취소
        handler.removeCallbacks(r); // 예약 취소
    }

}
