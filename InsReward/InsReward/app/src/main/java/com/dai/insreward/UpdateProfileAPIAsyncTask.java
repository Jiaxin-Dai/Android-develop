package com.dai.insreward;



import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class UpdateProfileAPIAsyncTask extends AsyncTask<String,Void,String > {

    private static final String TAG = "UpdateAyncTask";
    private static final String profilesEndPoint ="/profiles";
    @SuppressLint("StaticFieldLeak")
    private EditProfile editProfile;

    UpdateProfileAPIAsyncTask(EditProfile editProfile) {
        this.editProfile = editProfile;
    }

    @SuppressLint("LongLogTag")
    @Override

    protected void onPostExecute(String connectionResult) {

        String result=connectionResult;

        UserProfiles updateResult;

        if (result.contains("error")) // If there is "error" in the results...
            result = "FAILED";
        else
            result = "SUCCESS";

        updateResult = parseJSON(connectionResult);
        editProfile.setProfile(updateResult);

        Log.d(TAG, result);

    }

    @Override
    protected String doInBackground(String... strings) {

        String studentID = "A20454885";
        String username = strings[0];
        String password = strings[1];
        String firstName =strings[2];
        String lastName=strings[3];
        String department =strings[4];
        String story=strings[5];
        String position =strings[6];
        String checkbox = strings [7];
        String location = strings[8];
        String image = strings[9];
        Integer pointsToAward = Integer.parseInt(strings[10]);
        String rewardRecords = strings[11];

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", studentID);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("pointsToAward", pointsToAward);
            jsonObject.put("department", department);
            jsonObject.put("story", story);
            jsonObject.put("position", position);
            jsonObject.put("admin", checkbox);
            jsonObject.put("location", location);
            jsonObject.put("imageBytes", image);
            jsonObject.put("rewardRecords", rewardRecords);

            String updateResult = jsonObject.toString();

            String a= doAuth(jsonObject.toString());

            return (updateResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressLint("LongLogTag")
    private String doAuth(String jsonObjectText) {

        HttpURLConnection connection = null;

        BufferedReader reader = null;

        try {

            String urlString = editProfile.getResources().getString(R.string.base_url) + profilesEndPoint;

            Uri.Builder buildURL = Uri.parse(urlString).buildUpon();
            String urlToUse = buildURL.build().toString();
            URL url = new URL(urlToUse);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
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

            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                }


            }

        }
        return null;

    }


    private UserProfiles parseJSON(String s) {

        try {

            JSONObject juser = new JSONObject(s);
            String studentID = juser.getString("studentId");
            String firstName = juser.getString("firstName");
            String lastName = juser.getString("lastName");
            String username = juser.getString("username");
            String department = juser.getString("department");
            String story = juser.getString("story");
            String position = juser.getString("position");
            String password = juser.getString("password");

            Integer pointsToAward = juser.getInt("pointsToAward");
            String admin = juser.getString("admin");
            String imageBytes = juser.getString("imageBytes");
            String location = juser.getString("location");
            UserProfiles user = new UserProfiles(studentID, firstName, lastName, username, department,story,position,password,pointsToAward,admin,imageBytes,location);
            return user;

        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
