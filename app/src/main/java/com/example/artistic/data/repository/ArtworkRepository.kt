package com.example.artistic.data.repository

import com.example.artistic.data.dao.ArtworkDao
import com.example.artistic.data.entity.Artwork
import kotlinx.coroutines.flow.Flow

class ArtworkRepository(private val artworkDao: ArtworkDao) {
    
    suspend fun insertArtwork(artwork: Artwork): Long {
        return artworkDao.insertArtwork(artwork)
    }
    
    fun getPublicArtworks(limit: Int = 20): Flow<List<Artwork>> {
        return artworkDao.getPublicArtworks(limit)
    }
    
    fun getUserArtworks(userId: Long): Flow<List<Artwork>> {
        return artworkDao.getArtworksByUser(userId)
    }
    
    suspend fun getArtworkById(artworkId: Long): Artwork? {
        return artworkDao.getArtworkById(artworkId)
    }
    
    suspend fun updateArtwork(artwork: Artwork) {
        artworkDao.updateArtwork(artwork)
    }
    
    suspend fun deleteArtwork(artwork: Artwork) {
        artworkDao.deleteArtwork(artwork)
    }
    
    suspend fun likeArtwork(artworkId: Long) {
        artworkDao.incrementLikes(artworkId)
    }
    
    suspend fun viewArtwork(artworkId: Long) {
        artworkDao.incrementViews(artworkId)
    }
}