package com.example.firebasemlkit

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Facing
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var cameraView: CameraView
    private val PERMISION_REQUEST_CODE = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraView = findViewById<CameraView>(R.id.cameraView)
        checkAndRequestCameraPermission()

    }

    private fun checkAndRequestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
                    PERMISION_REQUEST_CODE)
        } else {
            startFaceProcessor()
        }
    }

    private fun startFaceProcessor() {
        cameraView.setLifecycleOwner(this)

        // Start the face processing
        val faceProcessor = FaceProcessor(cameraView, graphic_overlay,this)
        faceProcessor.startProcessing()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISION_REQUEST_CODE) {
            if (android.Manifest.permission.CAMERA == permissions[0] &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFaceProcessor()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}