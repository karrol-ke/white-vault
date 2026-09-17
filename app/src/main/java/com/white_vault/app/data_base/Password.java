package com.white_vault.app.data_base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "passwords")
public class Password {
    @PrimaryKey
    @NonNull
    public String identifier;
    public String value;
    public String service;
    public String date;
    public String description;

    public Password(@NonNull String identifier, String service, String value, String date, String description){
        this.identifier = identifier;
        this.service = service;
        this.value = value;
        this.date = date;
        this.description = description;
    }
}
