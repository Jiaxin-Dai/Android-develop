package com.dai.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DBHandler extends SQLiteOpenHelper {
    private static final String TAG = "DBHandler";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    // DB Name
    private static final String DATABASE_NAME = "StocksAppDB";
    // DB Table Name
    private static final String TABLE_NAME = "StockWatchTable";

    private static final String TABLE_STOCKINFO = "StockInfo";


    ///DB Columns
    private static final String SYMBOL = "stockSymbol";
    private static final String COMPANYNAME = "companyName";
    private static final String PRICE = "price";
    private static final String PRICECHANGE = "priceChange";
    private static final String CHANGEPERCENTAGE = "changePercentage";

    // save symbol and companyName
    private static final String SQL_CREATE_TABLE1 =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANYNAME + " TEXT not null )";
    // save stock info
    private static final String SQL_CREATE_TABLE2 =
            "CREATE TABLE " + TABLE_STOCKINFO + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANYNAME + " TEXT not null ," +
                    PRICE +" DOUBLE not null ,"+
                    PRICECHANGE +" DOUBLE not null ,"+
                    CHANGEPERCENTAGE+" DOUBLE not null"+
                    ")";

    private SQLiteDatabase database;

    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase(); // Inherited from SQLiteOpenHelper
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        db.execSQL(SQL_CREATE_TABLE1);
        db.execSQL(SQL_CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<Stock> loadStocksFromDB(){
        ArrayList<Stock> stocks = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_STOCKINFO,
                new String[]{SYMBOL,COMPANYNAME,PRICE,PRICECHANGE,CHANGEPERCENTAGE},
                null,
                null,
                null,
                null,
                null);

        if(cursor != null){
            cursor.moveToFirst();
            for(int i = 0;i<cursor.getCount();i++){
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
                double price = cursor.getDouble(2);
                double priceChange = cursor.getDouble(3);
                double changePercentage = cursor.getDouble(4);

                Stock s = new Stock(symbol,company,price,priceChange,changePercentage);
                stocks.add(s);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stocks;
    }

    public boolean searchBySymbol(String symbol){

        String select = "select " + SYMBOL + "," + COMPANYNAME + " from " + TABLE_NAME
                + " where " + SYMBOL + " like '%"+symbol+"%'" + " and " + COMPANYNAME+" like '%"+symbol+"%'";

        Cursor cursor = database.rawQuery(select,null);

        if (cursor != null&&cursor.getCount()>0) {
            return true;
        }
        return false;
    }
    public boolean searchExactSymbol(String symbol){

        Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{SYMBOL,COMPANYNAME},
                SYMBOL+"='"+symbol+"'",

                null,
                null,
                null,
                null);
        if (cursor != null&&cursor.getCount()>0) {
            return true;
        }
        return false;
    }
//    public boolean searchByName(String symbol){
//
//        Cursor cursor = database.query(
//                TABLE_NAME,
//                new String[]{SYMBOL,COMPANYNAME},
//                //SYMBOL+" like '%"+symbol+"%'",
//                COMPANYNAME+" like '%"+symbol+"%'",
//                null,
//                null,
//                null,
//                null);
//        if (cursor != null&&cursor.getCount()>0) {
//            return true;
//        }
//        return false;
//    }
    public ArrayList<Stock> loadStockList(String sym){
        ArrayList<Stock> stocks = new ArrayList<>();
        String select = "select " + SYMBOL + "," + COMPANYNAME + " from " + TABLE_NAME
                + " where " + SYMBOL + " like '%"+sym+"%'" + " and " + COMPANYNAME+" like '%"+sym+"%'";

        Cursor cursor = database.rawQuery(select,null);

        if(cursor != null){
            cursor.moveToFirst();
            for(int i = 0;i<cursor.getCount();i++){
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);

                Stock s = new Stock(symbol,company);
                stocks.add(s);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stocks;
    }

    public ArrayList<Stock> loadStock(String sym){
        ArrayList<Stock> stocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{SYMBOL,COMPANYNAME},
                SYMBOL+"='"+sym+"'",

                null,
                null,
                null,
                null);

        if(cursor != null){
            cursor.moveToFirst();
            for(int i = 0;i<cursor.getCount();i++){
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);

                Stock s = new Stock(symbol,company);
                stocks.add(s);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stocks;
    }
    public void saveSymbolToDB(Stock stock){
        Log.d(TAG, "saveSymbolToDB:Adding" + stock.getStockSymbol());
        boolean exist = false;
        ArrayList<Stock> temp = loadStocksFromDB();

        for(int i=0;i<temp.size();i++){
            if(stock.getStockSymbol().equals( temp.get(i).getStockSymbol())){
                exist = true;
                break;
            }
        }
        if(exist == false) {
            ContentValues values = new ContentValues();
            values.put(SYMBOL, stock.getStockSymbol());
            values.put(COMPANYNAME, stock.getCompanyName());
            database.insert(TABLE_NAME, null, values);

            Log.d(TAG, "saveSymbolToDB: Add Complete");
        }
    }

    public void saveSelectedStockToDB(Stock stock){
        Log.e(TAG, "saveSelectedStockToDB:Adding" + stock.toString());
        Log.e("--djx--", "saveSelectedStockToDB:" + stock.toString());
        boolean exist = false;
        ArrayList<Stock> temp = loadStocksFromDB();

        for(int i=0;i<temp.size();i++){
            if(stock.getStockSymbol().equals( temp.get(i).getStockSymbol())){
                exist = true;
                break;
            }
        }
        if(exist == false) {
            ContentValues values = new ContentValues();
            values.put(SYMBOL, stock.getStockSymbol());
            values.put(COMPANYNAME, stock.getCompanyName());
            values.put(PRICE, stock.getPrice());
            values.put(PRICECHANGE, stock.getPriceChange());
            values.put(CHANGEPERCENTAGE, stock.getChangePercentage());

            database.insert(TABLE_STOCKINFO, null, values);

            Log.d(TAG, "saveSymbolToDB: Add Complete");
            Log.e("--djx--", "saveSelectedStockToDB: done!");
        }
    }


    void deleteStock(String stocksymbol){
        Log.d(TAG, "deleteStock: Deleting Stock" + stocksymbol);

        int cnt = database.delete(
                TABLE_STOCKINFO,"stockSymbol ='"+stocksymbol+"'",null);

        Log.d(TAG, "deleteStock: " + cnt);
    }


    void shutDown(){database.close();}
}
