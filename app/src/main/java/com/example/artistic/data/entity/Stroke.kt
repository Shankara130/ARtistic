package com.example.artistic.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "strokes",
    foreignKeys = [
        ForeignKey(
            entity = Artwork::class,
            parentColumns = ["artworkId"],
            childColumns = ["artworkId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Stroke(
    @PrimaryKey(autoGenerate = true)
    val strokeId: Long = 0,
    val artworkId: Long,
    val strokeOrder: Int,
    val color: String,
    val thickness: Float,
    val timestampStart: Long,
    val timestampEnd: Long
)