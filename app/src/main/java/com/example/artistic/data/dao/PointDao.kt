package com.example.artistic.data.dao

import androidx.room.*
import com.example.artistic.data.entity.Point

@Dao
interface PointDao {
    @Insert
    suspend fun insertPoint(point: Point): Long
    
    @Insert
    suspend fun insertPoints(points: List<Point>)
    
    @Query("SELECT * FROM points WHERE strokeId = :strokeId ORDER BY pointOrder ASC")
    suspend fun getPointsByStroke(strokeId: Long): List<Point>
}