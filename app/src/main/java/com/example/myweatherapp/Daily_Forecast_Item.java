package com.example.myweatherapp;

public class Daily_Forecast_Item {
    String url;
    String temp;

    public String getUrl() {
        return url;
    }

    public String getTemp() {
        return temp;
    }

    public Daily_Forecast_Item(String url, String temp) {
        this.url = url;
        this.temp = temp;
    }
}
