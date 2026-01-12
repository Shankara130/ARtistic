package com.example.artistic.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.yourname.artistic.R
import com.yourname.artistic.databinding.ActivitySplashBinding
import com.yourname.artistic.ui.auth.LoginActivity
import com.yourname.artistic.ui.main.MainActivity
import com.yourname.artistic.utils.PreferenceManager

class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferenceManager = PreferenceManager(this)
        
        // Setup animations
        setupAnimations()
        
        // Navigate after delay
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DELAY)
    }
    
    private fun setupAnimations() {
        // Fade in animation for logo
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in).apply {
            duration = 1000
        }
        
        // Slide up animation for tagline
        val slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left).apply {
            duration = 800
        }
        
        binding.ivLogo.startAnimation(fadeIn)
        binding.tvTagline.startAnimation(slideUp)
    }
    
    private fun navigateToNextScreen() {
        val intent = if (preferenceManager.isLoggedIn()) {
            // User is already logged in
            Intent(this, MainActivity::class.java)
        } else {
            // User needs to login
            Intent(this, LoginActivity::class.java)
        }
        
        startActivity(intent)
        finish()
        
        // Smooth transition
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    
    companion object {
        private const val SPLASH_DELAY = 2000L // 2 seconds
    }
}