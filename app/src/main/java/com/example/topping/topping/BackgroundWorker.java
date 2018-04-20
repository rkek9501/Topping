package com.example.topping.topping;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Arktic on 2018-04-17.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    private Handler handler = null;
    private String flag = "";

    public BackgroundWorker(Handler mHandler) {
        this.handler = mHandler;
    }

    BackgroundWorker(Context context){
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpURLConnection conn = null;

        try {
            Log.d("soyuHttpTask", "args[0] = " + params[0]);
            Log.d("soyuHttpTask", "args[1] = " + params[1]);
            this.flag = params[0];
            String urlString = this.flag;
            Log.d("soyuHttpTask", "urlString = " + urlString);
            URL url = new URL(urlString);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(false);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("POST");
            String text = "";
            text += params[1];
            PrintWriter output = new PrintWriter(conn.getOutputStream());
            output.print(text);
            output.close();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while(true) {
                String line = br.readLine();
                if(line == null) {
                    br.close();
                    conn.disconnect();
                    conn = null;
                    result = sb.toString();
                    break;
                }

                sb.append(line).append("\n");
            }
        } catch (MalformedURLException var15) {
            var15.printStackTrace();
        } catch (IOException var16) {
            var16.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onPostExecute(String result) {
        Message message = new Message();
        message.obj = result.trim();
        handler.sendMessage(message);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
