package com.dai.stockwatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.dai.stockwatch.MainActivity;
import com.dai.stockwatch.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class NameDownloader extends AsyncTask<String, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private Stock stock;
    private static final String DATA_URL = "https://api.iextrading.com/1.0/ref-data/symbols";
    private static final String TAG = "NameDownloader";
    //private HashMap<String, String> sData = new HashMap<>();

    public NameDownloader(MainActivity ma) {
        mainActivity = ma;
    }
    @Override
    protected void onPreExecute(){
        Toast.makeText(mainActivity, "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stocks = parseJSON(s);

        mainActivity.saveInfoToDB(stocks);

    }

    @Override
    protected String doInBackground(String... strings) {
        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);


        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "doInBackground: ResponseCode:" + conn.getResponseCode());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            Log.d(TAG, "doInBackground: " + sb.toString());

            conn.disconnect();

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return sb.toString();
    }

    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jName = (JSONObject) jObjMain.get(i);
                String symbol = jName.getString("symbol");
                String name = jName.getString("name");
//                stock.setStockSymbol(jStock.getString("symbol"));
//                stock.setCompanyName(jStock.getString("companyName"));
//                //sData.put(symbol, name);
                Stock stock=new Stock(symbol,name);
                stockList.add(stock);
                Log.e("--djx--", "parseJSON:" + stock.toString());

            }

            return stockList;
        } catch (JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Download Fail");
            builder.setMessage("Not found stock: 400 Bad Request");
            AlertDialog alert = builder.create();
            alert.show();
        }
        return null;
    }
}
