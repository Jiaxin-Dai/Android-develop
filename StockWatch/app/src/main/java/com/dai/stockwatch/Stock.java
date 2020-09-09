package com.dai.stockwatch;

import java.io.Serializable;

public class Stock implements Comparable<Stock>{

    private String stockSymbol;
    private String companyName;
    private double price;
    private double priceChange;
    private double changePercentage;

    public Stock(String stockSymbol,String companyName,double price,double priceChange,double changePercentage){
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
        this.price = price;
        this.priceChange = priceChange;
        this.changePercentage = changePercentage;
    }
    public Stock(String stockSymbol,String companyName){
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
    }
    public Stock(){}

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
    }
    @Override
    public int compareTo(Stock s){
        return this.getStockSymbol().compareTo( s.getStockSymbol() );
    }

}
