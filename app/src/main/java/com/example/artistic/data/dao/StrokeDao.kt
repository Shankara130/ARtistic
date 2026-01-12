package com.example.artistic.data.dao

import androidx.room.*
import com.example.artistic.data.entity.Stroke

@Dao
interface StrokeDao {
    @Insert
    suspend fun insertStroke(stroke: Stroke): Long
    
    @Query("SELECT * FROM strokes WHERE artworkId = :artworkId ORDER BY strokeOrder ASC")
    suspend fun getStrokesByArtwork(artworkId: Long): List<Stroke>
    
    @Delete
    suspend fun deleteStroke(stroke: Stroke)
}