package com.example.topping.topping.FCM;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.topping.topping.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FCMPush extends AsyncTask<String, Void, String>{
    private static final String TASK_TAG = "FCMPushTask";
    private Handler handler = null;
    private String flag = "";
    public FCMPush(Handler handler){this.handler = handler;}
    public final static String AUTH_KEY_FCM = "AAAABydIzaY:APA91bHLiJ7bFf5zfcg2QttGhAkLKMMH5u0sgba8LESxfFLcNcAy-W2ondwxKbvCQA7UMUyDBUql9AE8GzwytP6erbK1qcOHeywHLp3s85vj1H7WcTTXIs2mFIcn1hb1fdONv0B2VC1H";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    @Override
    protected String doInBackground(String... args) {
        String result ="";
        String FMCurl = API_URL_FCM;
        HttpURLConnection conn = null;
        try {
            Log.d("PushTask", "args[0] = " + args[0]);
            Log.d("PushTask", "args[1] = " + args[1]);
            Log.d("PushTask", "args[2] = " + args[2]);

            this.flag = args[0];
            String userToken = this.flag;
            String hobby = args[1];
            String userName = args[2];
            URL url = new URL(FMCurl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key="+AUTH_KEY_FCM);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type","application/json");

            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            notification.put("body", userName+"님이 참여하였습니다.");
            notification.put("title", userName+"님이"+hobby+"에 참여하였습니다.");
            notification.put("icon", "0");
            root.put("notification", notification);
            root.put("to", userToken);
            root.put("click_action", "OPEN_ACTIVITY");

            OutputStream os = conn.getOutputStream();
            os.write(root.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
        } catch (MalformedURLException var15) {
            var15.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("PushTask", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PushTask", e.toString());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Message message = new Message();
        message.obj = this.flag +"|"+result.trim();
        this.handler.sendMessage(message);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
