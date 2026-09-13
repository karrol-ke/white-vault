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

    public Password(@NonNull String identifier, String value){
        this.identifier = identifier;
        this.value = value;
    }
}
