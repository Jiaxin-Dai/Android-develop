package com.dai.insreward;


import android.annotation.SuppressLint;
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


public class LoginAPIAyncTask extends AsyncTask<String, Void, UserProfiles> {

    // Remember         android:usesCleartextTraffic="true"

    private static final String TAG = "LoginAPIAyncTask";
    private static final String loginEndPoint ="/login";
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;

    LoginAPIAyncTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    protected void onPostExecute(UserProfiles connectionResult) {

        // Normally we would parse the results and make use of the data
        // For this example, we just return the raw json
        String result = connectionResult.toString();
        if (result.contains("error")) // If there is "error" in the results...
            result = "FAILED";
        else
            result = "SUCCESS";
        Log.d(TAG, "onPostExecute: " + result);

        mainActivity.getLogin(connectionResult);

    }

    @Override
    protected UserProfiles doInBackground(String... strings) {
        String stuId = "A20454885";
        String uName = strings[0];
        String pswd = strings[1];

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", stuId);
            jsonObject.put("username", uName);
            jsonObject.put("password", pswd);

            return parseJSON(doAuth(jsonObject.toString()));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String doAuth(String jsonObjectText) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            String urlString = mainActivity.getResources().getString(R.string.base_url) + loginEndPoint;


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

    private UserProfiles parseJSON(String s){
        try{
            JSONObject jsonObject = new JSONObject(s);

            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            //String oldPassword = null;
            String firstName = jsonObject.getString("firstName");
            String lastName = jsonObject.getString("lastName");
            String location = jsonObject.getString("location");
            String department = jsonObject.getString("department");
            String position = jsonObject.getString("position");
            String story = jsonObject.getString("story");
            String admin = jsonObject.getString("admin");
            Integer pointsToAward = jsonObject.getInt("pointsToAward");
            String imageBytes = jsonObject.getString("imageBytes");

            JSONArray jsonArray = jsonObject.getJSONArray("rewards");

            List<Rewards> rewardsList = new ArrayList<>();
            if(jsonArray.length()>0){
                for(int i = 0;i<jsonArray.length();i++) {
                    JSONObject jRewards = (JSONObject)jsonArray.get(i) ;
                    String usernameSource = jRewards.getString("username");
                    String fullName = jRewards.getString("name");
                    String date = jRewards.getString("date");
                    String comment = jRewards.getString("notes");
                    Integer pointToReward = jRewards.getInt("value");
                    Rewards rewards = new Rewards(usernameSource,date,fullName,pointToReward,comment);
                    rewardsList.add(rewards);
                }
            }
            else
                rewardsList = null;


            UserProfiles userProfiles = new UserProfiles(username,password,firstName,lastName,location,department,position,story,admin,pointsToAward,imageBytes,rewardsList);
            return userProfiles;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

