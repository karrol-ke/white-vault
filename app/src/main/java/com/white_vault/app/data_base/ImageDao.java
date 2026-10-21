package com.white_vault.app.data_base;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Query("SELECT * FROM images WHERE identifier = :identifier LIMIT 1")
    Image getImage(String identifier);

    @Query("SELECT * FROM images")
    List<Image> getAllImages();

    @Query("DELETE FROM images WHERE identifier = :identifier")
    void deleteImage(String identifier);
}
