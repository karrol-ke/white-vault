package com.white_vault.app.data_base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "document")
public class Document {
    @PrimaryKey
    @NonNull

    public String identifier;
    public String data_one;
    public String data_two;

    public Document(@NonNull String identifier, String data_one, String data_two){
        this.identifier = identifier;
        this.data_one = data_one;
        this.data_two = data_two;
    }
}
