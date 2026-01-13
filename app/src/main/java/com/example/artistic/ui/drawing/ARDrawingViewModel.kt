package com.example.artistic.ui.drawing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.artistic.data.entity.Artwork
import com.example.artistic.data.entity.Stroke
import com.example.artistic.data.entity.Point
import kotlinx.coroutines.launch

class ARDrawingViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentColor = MutableLiveData<Int>()
    val currentColor: LiveData<Int> = _currentColor

    private val _currentBrushSize = MutableLiveData<Float>()
    val currentBrushSize: LiveData<Float> = _currentBrushSize

    private val _strokes = MutableLiveData<List<Stroke>>()
    val strokes: LiveData<List<Stroke>> = _strokes

    private val currentStrokes = mutableListOf<Stroke>()
    private var currentStrokeId = 0L

    init {
        _currentColor.value = ARDrawingActivity.Color.BLACK
        _currentBrushSize.value = 5f
    }

    fun updateColor(color: Int) {
        _currentColor.value = color
    }

    fun updateBrushSize(size: Float) {
        _currentBrushSize.value = size
    }

    fun addPoint(point: ARDrawingActivity.DrawingPoint) {
        if (point.isNewStroke) {
            // Start new stroke
            currentStrokeId++
            val stroke = Stroke(
                strokeId = currentStrokeId,
                artworkId = 0, // Will be set when saving
                color = point.color.toString(),
                strokeOrder = currentStrokes.size,
                thickness = point.brushSize,
                timestampStart = System.currentTimeMillis(),
                timestampEnd = System.currentTimeMillis()
            )
            currentStrokes.add(stroke)
        }
        
        _strokes.value = currentStrokes
    }

    fun undo() {
        if (currentStrokes.isNotEmpty()) {
            currentStrokes.removeAt(currentStrokes.size - 1)
            _strokes.value = currentStrokes
        }
    }

    fun clear() {
        currentStrokes.clear()
        currentStrokeId = 0
        _strokes.value = currentStrokes
    }

    fun saveArtwork(points: List<ARDrawingActivity.DrawingPoint>) {
        viewModelScope.launch {
            // TODO: Save to Room database
            // Convert points to Stroke and Point entities
            // Save to database using repository
        }
    }
}