package com.example.artistic.ui.drawing

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.artistic.R
import com.example.artistic.databinding.ActivityArDrawingBinding
import com.google.ar.core.*
import com.google.ar.core.exceptions.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ARDrawingActivity : AppCompatActivity(), GLSurfaceView.Renderer {

    private lateinit var binding: ActivityArDrawingBinding
    private lateinit var viewModel: ARDrawingViewModel
    
    private var arSession: Session? = null
    private var surfaceView: GLSurfaceView? = null
    
    private val drawingPoints = mutableListOf<DrawingPoint>()
    private var currentColor = Color.BLACK
    private var currentBrushSize = 5f
    private var isDrawing = false
    
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setupARSession()
        } else {
            Toast.makeText(this, "Camera permission required for AR", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[ARDrawingViewModel::class.java]
        
        setupUI()
        checkCameraPermission()
    }

    private fun setupUI() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "AR Drawing"
        
        // Setup GLSurfaceView for AR
        surfaceView = binding.arSurfaceView
        surfaceView?.apply {
            preserveEGLContextOnPause = true
            setEGLContextClientVersion(3)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            setRenderer(this@ARDrawingActivity)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
        
        // Setup color picker
        setupColorPicker()
        
        // Setup brush size slider
        binding.brushSizeSlider.addOnChangeListener { _, value, _ ->
            currentBrushSize = value
            viewModel.updateBrushSize(value)
        }
        
        // Setup action buttons
        binding.btnUndo.setOnClickListener { undo() }
        binding.btnClear.setOnClickListener { clear() }
        binding.btnSave.setOnClickListener { saveArtwork() }
        
        // Toggle UI visibility
        binding.arSurfaceView.setOnClickListener {
            toggleUIVisibility()
        }
    }

    private fun setupColorPicker() {
        val colors = listOf(
            Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
            Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.WHITE
        )
        
        binding.colorPickerContainer.removeAllViews()
        colors.forEach { color ->
            val colorView = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(100, 100).apply {
                    setMargins(8, 8, 8, 8)
                }
                setBackgroundColor(color)
                setOnClickListener {
                    currentColor = color
                    viewModel.updateColor(color)
                    binding.selectedColorIndicator.setBackgroundColor(color)
                }
            }
            binding.colorPickerContainer.addView(colorView)
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                setupARSession()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun setupARSession() {
        try {
            // Check ARCore availability
            when (ArCoreApk.getInstance().checkAvailability(this)) {
                ArCoreApk.Availability.SUPPORTED_INSTALLED -> {
                    // ARCore is installed and supported
                }
                ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD,
                ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> {
                    // Request ARCore installation
                    ArCoreApk.getInstance().requestInstall(this, true)
                    return
                }
                else -> {
                    Toast.makeText(this, "ARCore not supported", Toast.LENGTH_LONG).show()
                    finish()
                    return
                }
            }

            // Create AR Session
            arSession = Session(this).apply {
                val config = Config(this).apply {
                    updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                    focusMode = Config.FocusMode.AUTO
                    lightEstimationMode = Config.LightEstimationMode.AMBIENT_INTENSITY
                }
                configure(config)
            }
            
            Toast.makeText(this, "AR Session ready. Start drawing!", Toast.LENGTH_SHORT).show()
            
        } catch (e: UnavailableException) {
            handleARException(e)
        }
    }

    private fun handleARException(exception: Exception) {
        val message = when (exception) {
            is UnavailableArcoreNotInstalledException -> "Please install ARCore"
            is UnavailableUserDeclinedInstallationException -> "ARCore installation declined"
            is UnavailableApkTooOldException -> "Please update ARCore"
            is UnavailableSdkTooOldException -> "Please update Android"
            is UnavailableDeviceNotCompatibleException -> "Device not compatible with ARCore"
            else -> "AR Session failed: ${exception.message}"
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDrawing = true
                addDrawingPoint(event.x, event.y, true)
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDrawing) {
                    addDrawingPoint(event.x, event.y, false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDrawing = false
            }
        }
        return true
    }

    private fun addDrawingPoint(x: Float, y: Float, isNewStroke: Boolean) {
        arSession?.let { session ->
            val frame = session.update()
            val camera = frame.camera
            
            if (camera.trackingState == TrackingState.TRACKING) {
                // Convert screen coordinates to world coordinates
                val hits = frame.hitTest(x, y)
                if (hits.isNotEmpty()) {
                    val hitResult = hits[0]
                    val pose = hitResult.hitPose
                    
                    val point = DrawingPoint(
                        x = pose.tx(),
                        y = pose.ty(),
                        z = pose.tz(),
                        color = currentColor,
                        brushSize = currentBrushSize,
                        isNewStroke = isNewStroke
                    )
                    
                    drawingPoints.add(point)
                    viewModel.addPoint(point)
                }
            }
        }
    }

    // GLSurfaceView.Renderer implementation
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        
        arSession?.let { session ->
            try {
                session.setCameraTextureName(0)
                val frame = session.update()
                val camera = frame.camera
                
                if (camera.trackingState == TrackingState.TRACKING) {
                    // Render camera background
                    renderCameraBackground(frame)
                    
                    // Render drawing points
                    renderDrawingPoints()
                }
            } catch (e: Exception) {
                // Handle rendering exceptions
            }
        }
    }

    private fun renderCameraBackground(frame: Frame) {
        // Render AR camera feed as background
        // Implementation using shaders
    }

    private fun renderDrawingPoints() {
        // Render all drawing points as 3D lines
        var lastPoint: DrawingPoint? = null
        
        drawingPoints.forEach { point ->
            if (!point.isNewStroke && lastPoint != null) {
                // Draw line from lastPoint to current point
                drawLine(lastPoint!!, point)
            }
            lastPoint = point
        }
    }

    private fun drawLine(start: DrawingPoint, end: DrawingPoint) {
        // OpenGL line drawing implementation
        // Use start and end points to render 3D line with color and brush size
    }

    private fun undo() {
        if (drawingPoints.isNotEmpty()) {
            // Remove last stroke
            val lastStroke = drawingPoints.findLast { it.isNewStroke }
            val index = drawingPoints.indexOf(lastStroke)
            if (index >= 0) {
                drawingPoints.subList(index, drawingPoints.size).clear()
                viewModel.undo()
            }
        }
    }

    private fun clear() {
        drawingPoints.clear()
        viewModel.clear()
        Toast.makeText(this, "Canvas cleared", Toast.LENGTH_SHORT).show()
    }

    private fun saveArtwork() {
        // Capture current AR scene and save
        viewModel.saveArtwork(drawingPoints)
        Toast.makeText(this, "Artwork saved!", Toast.LENGTH_SHORT).show()
    }

    private fun toggleUIVisibility() {
        val visibility = if (binding.controlsContainer.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
        binding.controlsContainer.visibility = visibility
        binding.toolbar.visibility = visibility
    }

    override fun onResume() {
        super.onResume()
        arSession?.resume()
        surfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        surfaceView?.onPause()
        arSession?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        arSession?.close()
        arSession = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    data class DrawingPoint(
        val x: Float,
        val y: Float,
        val z: Float,
        val color: Int,
        val brushSize: Float,
        val isNewStroke: Boolean
    )

    object Color {
        const val BLACK = 0xFF000000.toInt()
        const val RED = 0xFFFF0000.toInt()
        const val GREEN = 0xFF00FF00.toInt()
        const val BLUE = 0xFF0000FF.toInt()
        const val YELLOW = 0xFFFFFF00.toInt()
        const val MAGENTA = 0xFFFF00FF.toInt()
        const val CYAN = 0xFF00FFFF.toInt()
        const val WHITE = 0xFFFFFFFF.toInt()
    }
}