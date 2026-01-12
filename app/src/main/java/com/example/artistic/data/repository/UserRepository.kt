package com.example.artistic.data.repository

import com.example.artistic.data.dao.UserDao
import com.example.artistic.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    
    suspend fun registerUser(user: User): Long {
        return userDao.insertUser(user)
    }
    
    suspend fun loginUser(username: String, password: String): User? {
        return userDao.login(username, password)
    }
    
    fun getUserById(userId: Long): Flow<User?> {
        return userDao.getUserById(userId)
    }
    
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
    
    suspend fun checkUsernameExists(username: String): Boolean {
        return userDao.getUserByUsername(username) != null
    }
}