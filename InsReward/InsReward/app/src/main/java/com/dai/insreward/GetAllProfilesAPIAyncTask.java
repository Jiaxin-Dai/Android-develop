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


public class GetAllProfilesAPIAyncTask extends AsyncTask<String, Void,List<UserProfiles>> {

    // Remember         android:usesCleartextTraffic="true"

    private static final String TAG = "GetAllAPIAyncTask";
    private static final String getAllProfileEndPoint ="/allprofiles";
    @SuppressLint("StaticFieldLeak")
    private LeaderBoard leaderBoard;

    GetAllProfilesAPIAyncTask(LeaderBoard leaderBoard) {
        this.leaderBoard = leaderBoard;
    }


    @Override
    protected void onPostExecute(List<UserProfiles> connectionResult) {

        // Normally we would parse the results and make use of the data
        // For this example, we just return the raw json
        String result = connectionResult.toString();
        if (result.contains("error")) // If there is "error" in the results...
            result = "FAILED";
        else
            result = "SUCCESS";
        Log.d(TAG, "onPostExecute: " + result);

        leaderBoard.getAllUser(connectionResult);


    }

    @Override
    protected List<UserProfiles> doInBackground(String... strings) {
        String stuId = "A20454885";
        String uName = strings[0];
        String pswd = strings[1];

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("studentId", stuId);
            jsonObject.put("username", uName);
            jsonObject.put("password", pswd);
            String a = doAuth(jsonObject.toString());

            return parseJSON(a);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String doAuth(String jsonObjectText) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            String urlString = leaderBoard.getResources().getString(R.string.base_url) + getAllProfileEndPoint;


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

    private List<UserProfiles> parseJSON(String s){
        try{
            JSONArray jsonArray = new JSONArray(s);
            UserProfiles userProfiles;
            List<UserProfiles> userProfilesList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonUser = (JSONObject)jsonArray.get(i);
                String firstName = jsonUser.getString("firstName");
                String lastName = jsonUser.getString("lastName");
                String username = jsonUser.getString("username");
                String department = jsonUser.getString("department");
                String story = jsonUser.getString("story");
                String position = jsonUser.getString("position");
                Integer pointsToAward = jsonUser.getInt("pointsToAward");
                String admin = jsonUser.getString("admin");
                String imageBytes = jsonUser.getString("imageBytes");
                String location = jsonUser.getString("location");


                userProfiles = new UserProfiles(username, firstName, lastName, location, department, position, story, admin, pointsToAward, imageBytes);
                userProfilesList.add(userProfiles);


            }

            return userProfilesList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

