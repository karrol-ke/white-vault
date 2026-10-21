package com.white_vault.app.data_base;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Video video);

    @Query("SELECT * FROM videos WHERE identifier = :identifier LIMIT 1")
    Video getVideo(String identifier);

    @Query("SELECT * FROM videos")
    List<Video> getAllVideo();

    @Query("DELETE FROM videos WHERE identifier = :identifier")
    void deleteVideo(String identifier);
}
