package com.dai.newsgateway;

import android.os.AsyncTask;
import android.util.Log;

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

import static android.content.ContentValues.TAG;

public class ArticleAsyncTask extends AsyncTask<String, Void, String> {

    private static final String URL_START = "https://newsapi.org/v2/everything?sources=";
    private static final String URL_END ="&language=en&pageSize=100&apiKey=26fdd402c3e9453bb63f2331a30a8ea5";

    private NewsService newsService;
    private String sourceId;

    private ArrayList<Article> articlesList;

    public ArticleAsyncTask(NewsService newsService, String sourceId) {
        this.newsService = newsService;
        this.sourceId = sourceId;
    }

    @Override
    protected void onPostExecute(String s) {
        try{
            newsService.getArticles(parseJSON(s));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(URL_START + sourceId + URL_END);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }


    private ArrayList<Article> parseJSON(String s) {
        articlesList = new ArrayList<>();
        Article a;
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray articles = jsonObject.getJSONArray("articles");
            for (int i = 0; i < articles.length(); i++) {
                JSONObject jObj = articles.getJSONObject(i);
                String author = jObj.getString("author");
                String title = jObj.getString("title");
                String description = jObj.getString("description");
                String urlToImage = jObj.getString("urlToImage");
                String publishedAt = jObj.getString("publishedAt");

                String url = jObj.getString("url");
                Log.d(TAG, "parseJSON: title" + title);
                a = new Article(author,title,description,urlToImage,publishedAt,url);
                articlesList.add(a);
            }
            return articlesList;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
