package com.example.artistic.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.artistic.R
import com.example.artistic.databinding.ActivityLoginBinding
import com.example.artistic.ui.main.MainActivity
import com.example.artistic.utils.PreferenceManager
import com.example.artistic.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferenceManager = PreferenceManager(this)
        
        setupViews()
        observeViewModel()
    }
    
    private fun setupViews() {
        // Login button click
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (validateInput(username, password)) {
                performLogin(username, password)
            }
        }
        
        // Sign up link click
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        
        // Forgot password (optional)
        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun validateInput(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                binding.tilUsername.error = getString(R.string.error_empty_username)
                false
            }
            password.isEmpty() -> {
                binding.tilPassword.error = getString(R.string.error_empty_password)
                false
            }
            password.length < 6 -> {
                binding.tilPassword.error = getString(R.string.error_short_password)
                false
            }
            else -> {
                binding.tilUsername.error = null
                binding.tilPassword.error = null
                true
            }
        }
    }
    
    private fun performLogin(username: String, password: String) {
        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "Logging in..."
        
        lifecycleScope.launch {
            val user = authViewModel.login(username, password)
            
            if (user != null) {
                // Login successful
                preferenceManager.saveLoginData(user.userId, user.username)
                
                Toast.makeText(
                    this@LoginActivity,
                    "Welcome back, ${user.username}!",
                    Toast.LENGTH_SHORT
                ).show()
                
                // Navigate to main activity
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
                
            } else {
                // Login failed
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_login_failed),
                    Toast.LENGTH_SHORT
                ).show()
                
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = getString(R.string.login)
            }
        }
    }
    
    private fun observeViewModel() {
        // Add observers for LiveData if needed
    }
}