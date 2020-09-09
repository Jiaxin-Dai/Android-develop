package com.dai.stockwatch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private List<Stock> stockList;
    private MainActivity mainAct;

    public StockAdapter(List<Stock> stoList, MainActivity ma){
        this.stockList = stoList;
        this.mainAct = ma;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder
            (@NonNull final ViewGroup parent,int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position){
        Stock stock = stockList.get(position);
        holder.stockSymbol.setText(stock.getStockSymbol());
        holder.companyName.setText(stock.getCompanyName());
        holder.price.setText(Double.toString(stock.getPrice()));
        NumberFormat format = new DecimalFormat("0.00");
        String change = format.format(stock.getPriceChange());
        String changePer = format.format(stock.getChangePercentage());
        if(stock.getPriceChange() > 0){
            holder.stockSymbol.setTextColor(Color.GREEN);
            holder.companyName.setTextColor(Color.GREEN);
            holder.price.setTextColor(Color.GREEN);
            holder.change.setText("▲ "+change+"("+changePer+"%)");
            holder.change.setTextColor(Color.GREEN);
        }else{
            holder.stockSymbol.setTextColor(Color.RED);
            holder.companyName.setTextColor(Color.RED);
            holder.price.setTextColor(Color.RED);
            holder.change.setText("▼ "+change+"("+changePer+"%)");
            holder.change.setTextColor(Color.RED);
        }

    }
    @Override
    public int getItemCount() {
        return stockList.size();
    }

}
