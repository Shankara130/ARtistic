package com.example.artistic.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "points",
    foreignKeys = [
        ForeignKey(
            entity = Stroke::class,
            parentColumns = ["strokeId"],
            childColumns = ["strokeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Point(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long = 0,
    val strokeId: Long,
    val pointOrder: Int,
    val x: Float,
    val y: Float,
    val z: Float,
    val timestamp: Long
)