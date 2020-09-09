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
import java.util.List;


import static java.net.HttpURLConnection.HTTP_OK;

public class CreateProfileAPIAsyncTask extends AsyncTask<String, Void, UserProfiles > {

    private static final String TAG = "CreateAPIAsyncTask";
    private static final String CreateEndPoint ="/profiles";
    @SuppressLint("StaticFieldLeak")
    private CreatePro createPro;

    CreateProfileAPIAsyncTask(CreatePro createPro) {
        this.createPro = createPro;
    }



    @Override
    protected void onPostExecute(UserProfiles connectionResult) {

        String result = connectionResult.toString();
        if (result.contains("error")) // If there is "error" in the results...
            result = "FAILED";
        else
            result = "SUCCESS";

        Log.d(TAG, "onPostExecute: " + result);

        System.out.println(connectionResult.getFirstName()+connectionResult.getLastName());

        createPro.getProfile(connectionResult);


    }


    @Override
    protected UserProfiles doInBackground(String... strings) {

        String studentId = "A20454885";
        String username = strings[0];
        String password = strings[1];
        String firstname =strings[2];
        String lastname=strings[3];
        Integer pointsToAward = 1000;
        String department =strings[4];
        String story=strings[5];
        String position =strings[6];
        String checkbox = strings [7];
        String location = strings[8];
        String image = strings[9];

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", studentId);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("firstName", firstname);
            jsonObject.put("lastName", lastname);
            jsonObject.put("pointsToAward", pointsToAward);
            jsonObject.put("department", department);
            jsonObject.put("story", story);
            jsonObject.put("position", position);
            jsonObject.put("admin", checkbox);
            jsonObject.put("location", location);
            jsonObject.put("imageBytes", image);
            jsonObject.put("rewardRecords", null);



            System.out.println(jsonObject.toString());
            String a =  doAuth(jsonObject);
            System.out.println(a);
            return parseJSON(a);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

}
    private String doAuth(JSONObject jsonObjectText) {
        System.out.println(jsonObjectText);
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            String urlString = createPro.getResources().getString(R.string.base_url) + CreateEndPoint;

            Uri.Builder buildURL = Uri.parse(urlString).buildUpon();
            String urlToUse = buildURL.build().toString();
            URL url = new URL(urlToUse);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObjectText.toString());
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



    private UserProfiles parseJSON(String s){


        try{
            JSONObject jsonObject = new JSONObject(s);

            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            String oldPassword = null;
            String firstName = jsonObject.getString("firstName");
            String lastName = jsonObject.getString("lastName");
            String location = jsonObject.getString("location");
            String department = jsonObject.getString("department");
            String position = jsonObject.getString("position");
            String story = jsonObject.getString("story");
            String admin = jsonObject.getString("admin");
            Integer pointsToAward = jsonObject.getInt("pointsToAward");
            String imageBytes = jsonObject.getString("imageBytes");
            List<Rewards> rewards = null;
            UserProfiles userProfiles = new UserProfiles(username,password,oldPassword,firstName,lastName,location,department,position,story,admin,pointsToAward,imageBytes,rewards);
            return userProfiles;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}




