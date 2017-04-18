
package com.example.vipinkr.nodeapi;

import android.os.AsyncTask;

import android.util.Base64;
import android.util.Log;
//import org.json.simple.JSONObject;

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

/**
 * Created by Vipin K R on 22-12-2016.
 */

public class ProfileGet extends AsyncTask<String, Void, String> {
    private static final String TAG = "ProfileGet";

    HttpURLConnection conn;
    BufferedReader in;

    @SuppressWarnings("deprecation")
    @Override
    protected String doInBackground(String... params) {


        try {

            URL url = new URL(params[0]); // here is your URL path

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            //  String tmp= Base64.encode(params[1].getBytes(),Base64.DEFAULT);


            //  String basicAuth =URLEncoder.encode("Bearer " +URLEncoder.encode(params[1]));
            // JSONParser parser = new JSONParser();
            JSONObject json = new JSONObject(params[1]);
            String jwtToken=json.getString("token");
            Log.d(TAG, "doInBackground: json" + jwtToken);
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);
            conn.setRequestMethod("GET");
          /*
            conn.setDoOutput(false);
            conn.setDoInput(true);
           */
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "doInBackground: response code" + conn.getResponseCode());
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d(TAG, "doInBackground: valid response code");

                in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }


                Log.d("profile response", sb.toString());


                return sb.toString();

            } else {
                Log.d(TAG, "doInBackground: invalid response code");

                return new String("false : " + responseCode);
            }
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: exception");

            return new String("Exception: " + e.getMessage());
        } finally {
            conn.disconnect();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }


}
