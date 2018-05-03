package com.example.topping.topping;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Arktic on 2018-05-01.
 */

public class InsertDB extends AsyncTask<String, Void, String> {
//                ProgressDialog loading;
    String usermail;
    String username;
    public InsertDB(String userMail, String userName) {
        this.usermail = userMail;
        this.username = userName;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//                loading = ProgressDialog.show(getApplicationContext(), "Please Wait", null, true, true);
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//                loading.dismiss();
    }
    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL("http://61.84.24.188/topping3/loginTest.php");
            String data = "userMail=" + usermail + "&userName=" + username + "";

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

            /* 안드로이드 -> 서버 파라메터값 전달 */
            OutputStream outs = conn.getOutputStream();
            outs.write(data.getBytes("UTF-8"));
            outs.flush();
            outs.close();

            /* 서버 -> 안드로이드 파라메터값 전달 */
            InputStream is = null;
            BufferedReader in = null;
            String datas = "";

            is = conn.getInputStream();
            in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
            String line = null;
            StringBuffer buff = new StringBuffer();
            while ( ( line = in.readLine() ) != null )
            {
                buff.append(line + "\n");
            }
            datas = buff.toString().trim();

        } catch (MalformedURLException e) {
            return new String("MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            return new String("IOException: " + e.getMessage());
        }catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
        return  null;
    }
}



