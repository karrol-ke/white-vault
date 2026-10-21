package com.white_vault.app.data_base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "videos")
public class Video {
    @PrimaryKey

    @NonNull
    public String identifier;
    public String name;
    public String data;
    public String date;

    public Video(@NonNull String identifier, @NonNull String name, @NonNull String data, @NonNull String date){
        this.identifier = identifier;
        this.name = name;
        this.data = data;
        this.date = date;
    }
}
