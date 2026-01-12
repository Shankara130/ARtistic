package com.example.artistic.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.artistic.R
import com.example.artistic.data.entity.User
import com.example.artistic.databinding.ActivityRegisterBinding
import com.example.artistic.ui.main.MainActivity
import com.example.artistic.utils.PreferenceManager
import com.example.artistic.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferenceManager = PreferenceManager(this)
        setupViews()
    }
    
    private fun setupViews() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (validateInput(username, email, password)) {
                performRegister(username, email, password)
            }
        }
        
        binding.tvLogin.setOnClickListener {
            finish() // Back to login
        }
    }
    
    private fun validateInput(username: String, email: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                binding.tilUsername.error = getString(R.string.error_empty_username)
                false
            }
            email.isEmpty() -> {
                binding.tilEmail.error = getString(R.string.error_empty_email)
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = getString(R.string.error_invalid_email)
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
                binding.tilEmail.error = null
                binding.tilPassword.error = null
                true
            }
        }
    }
    
    private fun performRegister(username: String, email: String, password: String) {
        binding.btnRegister.isEnabled = false
        binding.btnRegister.text = "Creating account..."
        
        lifecycleScope.launch {
            val user = User(
                username = username,
                email = email,
                password = password
            )
            
            val result = authViewModel.register(user)
            
            result.onSuccess { userId ->
                // Registration successful
                preferenceManager.saveLoginData(userId, username)
                
                Toast.makeText(
                    this@RegisterActivity,
                    "Welcome to ARtistic, $username!",
                    Toast.LENGTH_SHORT
                ).show()
                
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
                
            }.onFailure { exception ->
                // Registration failed
                Toast.makeText(
                    this@RegisterActivity,
                    exception.message ?: "Registration failed",
                    Toast.LENGTH_SHORT
                ).show()
                
                binding.btnRegister.isEnabled = true
                binding.btnRegister.text = getString(R.string.register)
            }
        }
    }
}