package com.dai.stockwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.net.Uri;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private List<Stock> stockList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout
    private SharedHelper sh;
    private NameDownloader nameDownloader;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sh = new SharedHelper(this);

        recyclerView = findViewById(R.id.recycler);



        //use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDoNetCheck();
            }
        });

        dbHandler = new DBHandler(MainActivity.this);
        stockList= dbHandler.loadStocksFromDB();
        //specify an adapter
        stockAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(stockAdapter);

        if (sh.isFirst()) {
            nameDownloader = new NameDownloader(this);
            nameDownloader.execute();
        }
        sh.save(false);

    }

    @Override
    protected void onResume() {
        Collections.sort(stockList);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        dbHandler.shutDown();
        super.onDestroy();
    }

    private void doRefresh() {
        ArrayList<String> stockNames = new ArrayList<>();
        for (Stock stock : stockList) {
            stockNames.add(stock.getStockSymbol());
            dbHandler.deleteStock(stock.getStockSymbol());
        }

        stockList.clear();

        for (String symbol : stockNames) {
            new StockDownLoader(MainActivity.this).execute(symbol);
        }


        stockAdapter.notifyDataSetChanged();
        Collections.sort(stockList);
        Toast.makeText(this, "refreshed", Toast.LENGTH_SHORT).show();
        swiper.setRefreshing(false);

    }

    @Override
    public void onClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);

        String symbol = stockList.get(pos).getStockSymbol();
        if (symbol.trim().isEmpty()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        String url = "http://www.marketwatch.com/investing/stock/" + symbol;
        i.setData(Uri.parse(url));
        Toast.makeText(this, "open the browser", Toast.LENGTH_SHORT).show();
        startActivity(i);
    }

    @Override
    public boolean onLongClick(View v) {

        final int pos = recyclerView.getChildLayoutPosition(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dbHandler.deleteStock(stockList.get(pos).getStockSymbol());
                stockList.remove(pos);
                stockAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMessage("Delete Stock Symbol " + stockList.get(pos).getStockSymbol() + "?");
        builder.setTitle("Delete Stock");

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addDoNetCheck();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * click menu "+"
     */
    public void clickAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setTitle("Stock Selection");
        builder.setMessage("Please enter a Stock Symbol:");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String in = et.getText().toString();

                if(dbHandler.searchBySymbol(in)){
                    stockSelect(in);
                }
                else if(dbHandler.searchExactSymbol(in)){
                        for (Stock stock : stockList) {
                            if (stock.getStockSymbol().trim().equals(in)) {
                                //Toast.makeText(MainActivity.this, "Symbol already exist!", Toast.LENGTH_SHORT).show();
                                duplicateDialog(stock.getStockSymbol());
                                return;
                            }
                        }
                        dbHandler.loadStock(in);
                        new StockDownLoader(MainActivity.this).execute(in);

                    }
//                    if(dbHandler.searchExactSymbol(in)){
//                        for (Stock stock : stockList) {
//                            if (stock.getStockSymbol().trim().equals(in)) {
//                                //Toast.makeText(MainActivity.this, "Symbol already exist!", Toast.LENGTH_SHORT).show();
//                                duplicateDialog(stock.getStockSymbol());
//                                return;
//                            }
//                        }
//                        dbHandler.loadStock(in);
//                        new StockDownLoader(MainActivity.this).execute(in);
//
//                    }
//                    else if(dbHandler.searchBySymbol(in) && dbHandler.searchByName(in))
//                        stockSelect(in);

                    //new StockDownLoader(MainActivity.this).execute(in);

                else {
                    Toast.makeText(MainActivity.this, "please check the symbol ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void stockSelect(String input){
        final List<Stock> sList;
        final String in = input;

        sList = dbHandler.loadStockList(in);
        final CharSequence[] sArr = new CharSequence[sList.size()];
        for (int i = 0; i < sList.size(); i++) {
            sArr[i] = sList.get(i).getStockSymbol() + " - " + sList.get(i).getCompanyName();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a selection");

        builder.setItems(sArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String ss;
                ss = (sList.get(which).getStockSymbol());
                //mainActivity.getFinancialData(selected);
                for (Stock stock : stockList) {
                    if (stock.getStockSymbol().trim().equals(ss)) {
                        //Toast.makeText(MainActivity.this, "Symbol already exist!", Toast.LENGTH_SHORT).show();
                        duplicateDialog(stock.getStockSymbol());
                        return;
                    }
                }
                new StockDownLoader(MainActivity.this).execute(ss);
            }
        });

        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing, return
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void duplicateDialog(String symbol) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stock Symbol " + symbol + " is already displayed");
        builder.setTitle("Duplicate Notification");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * network checking before add stock
     */
    public void addDoNetCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            clickAdd();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks Cannot Be Added Without A Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();

            for(int i=0;i<stockList.size();i++){
                stockList.get(i).setPriceChange(0.00);
                stockList.get(i).setPrice(0.00);
                stockList.get(i).setChangePercentage(0.00);
            }
            stockAdapter.notifyDataSetChanged();

        }
    }

    /**
     * refresh
     */
    public void updateDoNetCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            doRefresh();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks Cannot Be Updated Without A Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();

            for(int i=0;i<stockList.size();i++){
                stockList.get(i).setPriceChange(0.00);
                stockList.get(i).setPrice(0.00);
                stockList.get(i).setChangePercentage(0.00);
            }
            stockAdapter.notifyDataSetChanged();

            swiper.setRefreshing(false);
        }
    }


    /**
     * save info to SQLite
     */
    public void saveInfoToDB(ArrayList<Stock> stocks) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Data is Loading...");
//        builder.setMessage("Wait");
//        AlertDialog dialog = builder.create();
//        dialog.show();

        for (Stock stock : stocks) {
            dbHandler.saveSymbolToDB(stock);
        }

    }

    /**
     * show stock info on view
     *
     * @param stock
     */
    public void showOnView(Stock stock) {
        stockList.add(stock);
        dbHandler.saveSelectedStockToDB(stock);
        Collections.sort(stockList);
        stockAdapter.notifyDataSetChanged();
    }

}
