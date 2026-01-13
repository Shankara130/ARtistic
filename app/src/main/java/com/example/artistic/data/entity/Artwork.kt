package com.example.artistic.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "artworks",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)

data class Artwork(
    @PrimaryKey(autoGenerate = true)
    val artworkId: Long = 0,
    val userId: Long,
    val title: String,
    val description: String? = null,
    val markerId: String,
    val thumbnailPath: String? = null,
    val creationDate: Long = System.currentTimeMillis(),
    val totalLikes: Int = 0,
    val totalViews: Int = 0,
    val isPublic: Boolean = true,
    val drawingTime: Int = 0
)