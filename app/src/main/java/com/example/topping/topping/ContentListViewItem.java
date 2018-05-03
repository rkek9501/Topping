package com.example.topping.topping;

import android.graphics.drawable.Drawable;

/**
 * Created by user on 2018-05-03.
 */

public class ContentListViewItem {
    private Drawable userImg;
    private String userName;
    private String hobby,hobbyDetail,place;
    private String fromDate, toDate, detail;
    public Drawable getUserImg(){return  userImg;}
    public void setUserImg(Drawable userImg) {this.userImg = userImg;}

    public String getUserName(){return  userName;}
    public void setUserName(String userName) {this.userName=userName;}

    public String getHobby(){return  hobby;}
    public void setHobby(String hobby) {this.hobby=hobby;}

    public String getHpbbyDetail() {return  hobbyDetail;}
    public void setHobbyDetail(String hobbyDetail) { this.hobbyDetail = hobbyDetail;}

    public String getPlace() {return  place;}
    public void setPlace(String plcae) { this.place = place;}

    public String getFromDate(){return  fromDate;}
    public void setFromDate(String fromDate){this.fromDate = fromDate;}

    public String getToDate(){return  toDate;}
    public void setToDate(String toDate){this.toDate = toDate;}

    public String getDetail() {return  detail;}
    public void setDetail(String detail) { this.detail = detail;}
}
