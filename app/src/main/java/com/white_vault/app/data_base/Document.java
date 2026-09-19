package com.white_vault.app.data_base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "documents")
public class Document {
    @PrimaryKey
    @NonNull

    public String identifier;
    public String data;
    public String date;
    public String description;

    public Document(@NonNull String identifier, String data, String date, String description){
        this.identifier = identifier;
        this.data = data;
        this.date = date;
        this.description = description;
    }
}
