package com.example.firebasemlkit

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.otaliastudios.cameraview.CameraView

class FaceProcessor(private val cameraView: CameraView, private val graphic_overlay: GraphicOverlay, context: Context) {

    val ctx = context
    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(options)

    var faceDirection=""

    fun startProcessing() {
        // Getting frames from camera view
        cameraView.addFrameProcessor { frame ->
            if (frame.size != null) {
                //graphic_overlay.clear()
                val image = InputImage.fromByteArray(
                    frame.getData(),
                    frame.size.width,
                    frame.size.height,
                    frame.rotationToView,
                    InputImage.IMAGE_FORMAT_NV21
                )


                detector.process(image)
                    .addOnSuccessListener { faces ->
                        processFaceResult(faces)

                    }.addOnFailureListener {
                        it.printStackTrace()
                    }
            }
        }

    }

    private fun processFaceResult(faces: MutableList<Face>) {
        var faceCount = 0
        var onlyOne = 0
        faces.forEach {
            if (onlyOne < 1){
                graphic_overlay.clear()
                val bounds = it.boundingBox
                val rotX = it.headEulerAngleX
                val rotY = it.headEulerAngleY
                val rotZ = it.headEulerAngleZ

                upOrDownDetection(rotX)
                rightOrLeftDetection(rotY)

                val rectOverLay = RectOverlay(graphic_overlay, bounds,faceDirection)
                graphic_overlay.add(rectOverLay)
                onlyOne++

               // Log.e("rotX: ", rotX.toString())
                //Log.e("rotY: ", rotY.toString())
                //Log.e("rotZ",rotZ.toString())

            }
            faceCount++

        }

        Log.e("Face Count","Detected faces in camera: $faceCount")

        if (faceCount==0){
            graphic_overlay.clear()
        }
        else if (faceCount>1){
            graphic_overlay.clear()
            Toast.makeText(ctx, "Only one face is allowed at time", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rightOrLeftDetection(rotY: Float) {
        if (rotY<-10){
            Log.e("Face Direction","LEFT")
            faceDirection ="Turn a Little Left" // this is the case for the front camera
           // faceDirection ="Turn a Little Right" // this is the case for the back camera
        }
        else if (rotY>10){
            Log.e("Face Direction","RIGHT")
            faceDirection ="Turn a Little Right" // this is the case for the front camera
           // faceDirection ="Turn a Little Left" // this is the case for the front camera
        }
    }

    private fun upOrDownDetection(rotX: Float) {
        if (rotX<-10){
            Log.e("Face Direction X","DOWN")
            faceDirection ="Raise Your Head a Little"
            if (faceDirection==""){
                faceDirection ="Raise Your Head a Little"
            }
        }
        else if (rotX>10){
            Log.e("Face Direction X","UP")
            faceDirection ="Lower Your Head a Little"
            if (faceDirection==""){
                faceDirection ="Lower Your Head a Little"
            }
        }
        else{
            faceDirection=""
        }


    }
}