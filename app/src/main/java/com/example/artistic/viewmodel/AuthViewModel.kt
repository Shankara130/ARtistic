package com.example.artistic.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.artistic.data.AppDatabase
import com.example.artistic.data.entity.User
import com.example.artistic.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository: UserRepository
    
    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }
    
    suspend fun login(username: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userRepository.loginUser(username, password)
        }
    }
    
    suspend fun register(user: User): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                // Check if username already exists
                if (userRepository.checkUsernameExists(user.username)) {
                    Result.failure(Exception("Username already exists"))
                } else {
                    val userId = userRepository.registerUser(user)
                    Result.success(userId)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}