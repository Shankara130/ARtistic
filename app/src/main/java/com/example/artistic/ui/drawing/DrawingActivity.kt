package com.example.artistic.ui.drawing

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.artistic.databinding.ActivityDrawingBinding

class DrawingActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityDrawingBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
    }
    
    private fun setupViews() {
        // Close button
        binding.btnClose.setOnClickListener {
            finish()
        }
        
        // Save button (mock implementation)
        binding.btnSave.setOnClickListener {
            Toast.makeText(
                this,
                "Drawing saved! (Mock implementation)",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        
        // Mock drawing info
        Toast.makeText(
            this,
            "Mock Drawing Activity - AR feature coming soon!",
            Toast.LENGTH_LONG
        ).show()
    }
}