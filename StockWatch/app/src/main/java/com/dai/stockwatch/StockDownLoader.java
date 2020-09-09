package com.dai.stockwatch;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockDownLoader extends AsyncTask<String,Void,String> {
    private static final String TAG = "StockDownLoader";
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private Stock stock;


    private static final String stockURL = "https://cloud.iexapis.com/stable/stock/";
    private static final String yourAPIKey = "sk_ac000ba3cc5d4aa8bd23f8b7be35a6d0";

    public StockDownLoader(MainActivity ma){
        mainActivity = ma;
    }
    @Override
    protected String doInBackground(String... params) {
        String symbol = params[0];
        Uri.Builder buildURL = Uri.parse(stockURL+symbol+"/quote").buildUpon();
        buildURL.appendQueryParameter("token",yourAPIKey);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);
        Log.e("--djx--", "doInBackground:" + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());
            Log.e("--djx--", "doInBackground:" + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            //return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        parseJSON(s);
        mainActivity.showOnView(stock);

    }

    private void parseJSON(String s){
        try {
            JSONObject stockJson = new JSONObject(s);

            stock=new Stock();
            stock.setStockSymbol(stockJson.getString("symbol"));
            stock.setCompanyName(stockJson.getString("companyName"));
            stock.setChangePercentage(stockJson.getDouble("changePercent"));
            stock.setPrice(stockJson.getDouble("latestPrice"));
            stock.setPriceChange(stockJson.getDouble("change"));

            //return stockArrayList;

        } catch (JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Response Code: 404 Not Found");
            AlertDialog alert = builder.create();
            alert.show();
        }
        //return null;
    }
}

