package com.example.artistic.utils

object Constants {
    // Database
    const val DATABASE_NAME = "artistic_database"
    
    // Artwork
    const val MAX_TITLE_LENGTH = 100
    const val MAX_DESCRIPTION_LENGTH = 500
    
    // Drawing
    const val DEFAULT_BRUSH_SIZE = 5f
    const val MIN_BRUSH_SIZE = 1f
    const val MAX_BRUSH_SIZE = 50f
    
    // Intent extras
    const val EXTRA_USER_ID = "extra_user_id"
    const val EXTRA_USERNAME = "extra_username"
    const val EXTRA_ARTWORK_ID = "extra_artwork_id"
    
    // Request codes
    const val REQUEST_CODE_DRAWING = 100
    const val REQUEST_CODE_PROFILE = 101
    
    // Colors
    val DEFAULT_COLORS = listOf(
        "#6C63FF", // Primary
        "#FF6584", // Secondary
        "#4ECDC4", // Turquoise
        "#FFD93D", // Yellow
        "#FF9A3D", // Orange
        "#00B894", // Green
        "#2D3436", // Dark Gray
        "#FFFFFF"  // White
    )
}