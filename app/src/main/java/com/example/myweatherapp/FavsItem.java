package com.example.myweatherapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favslist")
public class FavsItem {
    @ColumnInfo(name = "city_name")
    String name;
    @PrimaryKey(autoGenerate = true)
    int id;

    public FavsItem() {

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
