package com.dai.newsgateway;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class SourceAsyncTask extends AsyncTask<String, Void, String> {


    private String URL_PRE = "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private String APIKEY = "&apiKey=26fdd402c3e9453bb63f2331a30a8ea5";

    private MainActivity mainActivity;
    private String category;

    private ArrayList<String> categoriesList;
    private ArrayList<Source> sourceList;
    private Source source;

    public SourceAsyncTask(MainActivity mainActivity, String category) {
        this.mainActivity = mainActivity;

        if(category.equals("all") || category.equals(""))
            this.category = "";
        else
            this.category = category;
    }

    @Override
    protected void onPostExecute(String s) {
        try{
            mainActivity.getSources(parseJSON(s), categoriesList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(URL_PRE + category + APIKEY );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line).append("\n");
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Source> parseJSON(String s){
        sourceList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray sources = jsonObject.getJSONArray("sources");

            for (int i = 0; i < sources.length() ; i++) {
                JSONObject src = sources.getJSONObject(i);
                String id = src.getString("id");
                String url = src.getString("url");
                String name = src.getString("name");
                String category = src.getString("category");

                if(! categoriesList.contains(category)){
                    categoriesList.add(category);
                }
                source = new Source(id,name,url,category);
                sourceList.add(source);
            }
            return sourceList;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
