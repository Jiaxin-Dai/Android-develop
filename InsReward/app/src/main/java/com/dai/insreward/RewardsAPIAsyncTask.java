package com.dai.insreward;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;


public class RewardsAPIAsyncTask extends AsyncTask<String, Void, String> {

    // Remember         android:usesCleartextTraffic="true"

    private static final String TAG = "RewardsAPIAsyncTask";
    private static final String RewardEndPoint = "/rewards";
    @SuppressLint("StaticFieldLeak")
    private Award award;

    RewardsAPIAsyncTask(Award award) {
        this.award = award;
    }


    @Override
    protected void onPostExecute(String connectionResult) {

        // Normally we would parse the results and make use of the data
        // For this example, we just return the raw json
        String result = connectionResult;
        if (result.contains("error")) // If there is "error" in the results...
            result = "FAILED";
        else
            result = "SUCCESS";
        Log.d(TAG, "onPostExecute: " + result);


    }

    @Override
    protected String doInBackground(String... strings) {
        String stuId = "A20454885";
        String uNameSource = strings[0];
        String pswdSource = strings[1];
        String uNameTarget = strings[2];
        String fullNameTarget = strings[3];
        String date = strings[4];
        String notes = strings[5];
        String value = strings[6];

        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jTarget = new JSONObject();
            JSONObject jSource = new JSONObject();
            jTarget.put("studentId",stuId);
            jTarget.put("username",uNameTarget);
            jTarget.put("name",fullNameTarget);
            jTarget.put("date",date);
            jTarget.put("notes",notes);
            jTarget.put("value",value);
            jSource.put("studentId",stuId);
            jSource.put("username",uNameSource);
            jSource.put("password",pswdSource);
            jsonObject.put("target", jTarget);
            jsonObject.put("source", jSource);


            return doAuth(jsonObject.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String doAuth(String jsonObjectText) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            String urlString = award.getResources().getString(R.string.base_url) + RewardEndPoint;


            Uri.Builder buildURL = Uri.parse(urlString).buildUpon();
            String urlToUse = buildURL.build().toString();
            URL url = new URL(urlToUse);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObjectText);
            out.close();

            int responseCode = connection.getResponseCode();
            String responseText = connection.getResponseMessage();

            Log.d(TAG, "doAuth: " + responseCode + ": " + responseText);


            StringBuilder result = new StringBuilder();

            if (responseCode == HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                return result.toString();
            } else {

                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                return result.toString();
            }

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: Invalid URL: " + e.getMessage());
            e.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                }
            }
        }
        return null;
    }

}
