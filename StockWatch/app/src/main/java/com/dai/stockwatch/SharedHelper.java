package com.dai.stockwatch;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    private Context mContext;


    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }



    public void save(boolean isFirst) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    public Boolean isFirst() {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        return sp.getBoolean("isFirst", true);

    }
}
