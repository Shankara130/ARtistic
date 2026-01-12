package com.example.artistic

import android.app.Application
import com.example.artistic.data.AppDatabase

class ARtisticApplication : Application() {
    
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
    
    override fun onCreate() {
        super.onCreate()
        // Initialize app-level components here
    }
}