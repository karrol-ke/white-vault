package com.white_vault.app.data_base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "documents")
public class Document {
    @PrimaryKey
    @NonNull

    public String identifier;
    public String document_name;
    public String data;
    public String date;
    public String description;

    public Document(@NonNull String identifier, String document_name, String data, String date, String description){
        this.identifier = identifier;
        this.document_name = document_name;
        this.data = data;
        this.date = date;
        this.description = description;
    }
}
