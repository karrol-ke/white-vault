package com.white_vault.app.data_base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "passwords")
public class Password {
    @PrimaryKey
    @NonNull

    public String index;
    public String value;

    public Password(@NonNull String index, String value){
        this.index = index;
        this.value = value;
    }
}
