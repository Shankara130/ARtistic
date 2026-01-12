package com.example.artistic.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val username: String,
    val email: String,
    val password: String,
    val profilePict: String? = null,
    val bio: String? = null,
    val totalArtworks: Int = 0,
    val totalLikes: Int = 0,
    val joinDate: Long = System.currentTimeMillis()
)