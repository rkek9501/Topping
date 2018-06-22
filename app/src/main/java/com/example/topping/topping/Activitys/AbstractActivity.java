package com.example.topping.topping.Activitys;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.topping.topping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

       /* Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/BMJUA_ttf.ttf"));*/
        if(user!=null){
            userMail = user.getEmail();
            userName = user.getDisplayName();
//            userImg = setImageURI(user.getPhotoUrl());
        }else{

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
       /* actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(true);            //홈 아이콘을 숨김처리합니다.
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.drawable.title);
        actionBar.setLogo(R.drawable.title);*/


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
       /* LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.action_bar, null);

        actionBar.setCustomView(actionbar);
        actionBar.home

        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);
        parent.setLogo(R.drawable.title);*/

        actionBar.show();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        showActionBarLogo(this, true);

        return true;
    }
    private Target logoTarget;
    public void showActionBarLogo(final Activity activity, boolean show)
    {
        if (show)
        {
            // Calculate Action bar height
            int actionBarHeight = 200;
            TypedValue tv = new TypedValue();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,activity.getResources().getDisplayMetrics());
            }

            // Using action bar background drawable
            logoTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    Drawable[] layers = new Drawable[2];
                    layers[0] = new ColorDrawable(Color.parseColor("#98D7F9")); // Background color of Action bar
                    BitmapDrawable bd = new BitmapDrawable(activity.getResources(), bitmap);
                    bd.setGravity(Gravity.CENTER);
                    Drawable drawLogo = bd;
                    layers[1] = drawLogo; // Bitmap logo of Action bar (loaded from Picasso)
                    LayerDrawable layerDrawable = new LayerDrawable(layers);

                    layers[1].setAlpha(0);

                    ((AppCompatActivity) activity).getSupportActionBar().setBackgroundDrawable(layerDrawable);

                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(layers[1], PropertyValuesHolder.ofInt("alpha", 255));
                    animator.setTarget(layers[1]);
                    animator.setDuration(0);
                    animator.start();
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) { }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) { }
            };
            Picasso.get().load(R.drawable.title_white).resize(0, actionBarHeight).into(logoTarget);

        } else {
            ((AppCompatActivity) activity).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#98D7F9")));
        }
    }
}
