package com.example.topping.topping;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by Arktic on 2018-04-18.
 */

public class SearchListViewItem {
    private Drawable userImg;
    private String userName;
    private String hobby,hobbyDetail;
    private String date;

    public Drawable getUserImg(){return  userImg;}
    public void setUserImg(Drawable userImg) {this.userImg = userImg;}

    public String getUserName(){return  userName;}
    public void setUserName(String userName) {this.userName=userName;}

    public String getHobby(){return  hobby;}
    public void setHobby(String hobby) {this.hobby=hobby;}

    public String getHpbbyDetail() {return  hobbyDetail;}
    public void setHobbyDetail(String hobbyDetail) { this.hobbyDetail = hobbyDetail;}

    public String getDate(){return  date;}
    public void setDate(String date){this.date = date;}
}
