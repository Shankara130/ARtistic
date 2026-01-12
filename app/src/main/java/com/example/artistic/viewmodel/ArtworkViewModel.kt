package com.example.artistic.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.artistic.data.AppDatabase
import com.example.artistic.data.entity.Artwork
import com.example.artistic.data.repository.ArtworkRepository
import kotlinx.coroutines.Dispatchers

class ArtworkViewModel(application: Application) : AndroidViewModel(application) {
    
    private val artworkRepository: ArtworkRepository
    
    init {
        val artworkDao = AppDatabase.getDatabase(application).artworkDao()
        artworkRepository = ArtworkRepository(artworkDao)
    }
    
    val publicArtworks: LiveData<List<Artwork>> = 
        artworkRepository.getPublicArtworks().asLiveData(Dispatchers.IO)
    
    fun getUserArtworks(userId: Long): LiveData<List<Artwork>> {
        return artworkRepository.getUserArtworks(userId).asLiveData(Dispatchers.IO)
    }
    
    suspend fun insertArtwork(artwork: Artwork): Long {
        return artworkRepository.insertArtwork(artwork)
    }
    
    suspend fun likeArtwork(artworkId: Long) {
        artworkRepository.likeArtwork(artworkId)
    }
    
    suspend fun deleteArtwork(artwork: Artwork) {
        artworkRepository.deleteArtwork(artwork)
    }
}