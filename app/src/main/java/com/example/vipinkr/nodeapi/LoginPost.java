package com.example.vipinkr.nodeapi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Created by Vipin K R on 02-08-2016.
 */

public class LoginPost extends AsyncTask<String, Void, String[]> {
    private static final String TAG = "LoginPost";
    HttpURLConnection conn;
    BufferedReader in;
    Context context;

    public LoginPost(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    protected String[] doInBackground(String... params) {
        String[] array=new String[2];

        Log.d(TAG, "doInBackground: starts");
        try {

            URL url = new URL(params[0]); // here is your URL path

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("email", params[1]);
            postDataParams.put("password", params[2]);
            Log.e("params", postDataParams.toString());


            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(25000 /* milliseconds */);
            conn.setConnectTimeout(25000 /* milliseconds */);


            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            //conn.setDoInput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }


                Log.i("payload", sb.toString());
                array[0] = "success";
                array[1] = sb.toString();
                Log.d(TAG, "doInBackground: array[0] ="+array[0]+"\n"+"array[1]="+array[1]);
                return array;


            } else {
                array[0] = "failed";
                array[1] = new String("false : " + responseCode);
                Log.d(TAG, "doInBackground: array[0] ="+array[0]+"\n"+"array[1]="+array[1]);
/*
                MainActivity.login="fail";
                return new String("false : " + responseCode);

                */
                return array;
            }
        } catch (Exception e) {
            array[0] = "failed";
            array[1] =new String("Exception: " + e.getMessage());
            Log.d(TAG, "doInBackground: array[0] ="+array[0]+"\n"+"array[1]="+array[1]);
            /*
            MainActivity.login="fail";
            return new String("Exception: " + e.getMessage());
            */
            return array;
        } finally {
            conn.disconnect();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "doInBackground: ends");
        }

    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        if (result[0].equalsIgnoreCase("success")) {
            Intent i = new Intent(context, Profile.class);
            i.putExtra("url", "https://medic1.herokuapp.com/api/user/profile");
            i.putExtra("token",result[1]);
            context.startActivity(i);

        } else if(result[0].equalsIgnoreCase("failed")) {
            Toast.makeText(context,result[1], Toast.LENGTH_SHORT).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}