package com.example.artistic.data.entity

import androidx.room.*
import com.example.artistic.data.entity.Artwork
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtworkDao {
    @Insert
    suspend fun insertArtwork(artwork: Artwork): Long

    @Query("SELECT * FROM artworks WHERE isPublic = 1 ORDER BY creationDate DESC LIMIT :limit")
    fun getPublicArtworks(limit: Int = 20): Flow<List<Artwork>>
    
    @Query("SELECT * FROM artworks WHERE userId = :userId ORDER BY creationDate DESC")
    fun getArtworksByUser(userId: Long): Flow<List<Artwork>>
    
    @Query("SELECT * FROM artworks WHERE artworkId = :artworkId")
    suspend fun getArtworkById(artworkId: Long): Artwork?
    
    @Update
    suspend fun updateArtwork(artwork: Artwork)
    
    @Delete
    suspend fun deleteArtwork(artwork: Artwork)
    
    @Query("UPDATE artworks SET totalLikes = totalLikes + 1 WHERE artworkId = :artworkId")
    suspend fun incrementLikes(artworkId: Long)
    
    @Query("UPDATE artworks SET totalViews = totalViews + 1 WHERE artworkId = :artworkId")
    suspend fun incrementViews(artworkId: Long)
}