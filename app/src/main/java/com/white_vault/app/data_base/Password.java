package com.white_vault.app.data_base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "passwords")
public class Password {
    @PrimaryKey
    @NonNull
    public String identifier;
    public String id;
    public String value;
    public String service;
    public String date;
    public String description;

    public Password(@NonNull String identifier, String id, String service, String value, String date, String description){
        this.identifier = identifier;
        this.id = id;
        this.value = value;
        this.service = service;
        this.date = date;
        this.description = description;
    }
}
