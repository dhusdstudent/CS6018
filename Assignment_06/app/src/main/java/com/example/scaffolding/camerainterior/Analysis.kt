package com.example.scaffolding.camerainterior

import android.content.Context
import android.graphics.RectF
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.camera.view.TransformExperimental
import androidx.camera.view.transform.CoordinateTransform
import androidx.camera.view.transform.ImageProxyTransformFactory
import androidx.core.content.ContextCompat
import com.example.scaffolding.DetectObj
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlin.collections.emptyList

enum class  DetectionModel {
    ML_KIT,
    EFFICIENT_DET
}
fun detect_MLKit(): ObjectDetector {
    val options = ObjectDetectorOptions.Builder().setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableMultipleObjects().enableClassification().build()
    return ObjectDetection.getClient(options)
}

fun detect_EffDet() : ObjectDetector {
    val LM = LocalModel.Builder().setAssetFilePath("efficientdet-lite4.tflite").build()
    val options = CustomObjectDetectorOptions.Builder(LM).setDetectorMode(
        CustomObjectDetectorOptions.STREAM_MODE)
        .enableMultipleObjects().enableClassification().setClassificationConfidenceThreshold(0.5f)
        .setMaxPerObjectLabelCount(1).build()

    return ObjectDetection.getClient(options)
}

@OptIn(ExperimentalGetImage::class)
fun Analysis(
    imageAnalysis: ImageAnalysis,
    context: Context,
    model: DetectionModel,
    previewView: PreviewView,
    onDetectionsFound: (List<DetectObj>) -> Unit
) : ObjectDetector {

    val detector = when (model){ //everything assigned to detector
        DetectionModel.ML_KIT -> detect_MLKit()
        DetectionModel.EFFICIENT_DET -> detect_EffDet()
    }

    imageAnalysis.setAnalyzer(
        ContextCompat.getMainExecutor(context)
    ) { image ->

        processImage(
            imageProxy = image,
            detector = detector,
            previewView = previewView,
            onDetectionsFound = onDetectionsFound
        )
    }

    return detector
}

@ExperimentalGetImage
@OptIn(TransformExperimental::class)
fun processImage(
    imageProxy: ImageProxy,
    detector: ObjectDetector,
    previewView: PreviewView,
    onDetectionsFound: (List<DetectObj>) -> Unit
) {
    val theImage = imageProxy.image

    if (theImage == null){
        imageProxy.close()
        return
    }

    val image = InputImage.fromMediaImage(
        theImage,
        imageProxy.imageInfo.rotationDegrees
    )

    detector.process(image).addOnSuccessListener { detectedObjects ->
        val previewOutputTransform = previewView.outputTransform

        if (previewOutputTransform == null){
            onDetectionsFound(emptyList())
            return@addOnSuccessListener
        }

        val imageAnalysisTransform = ImageProxyTransformFactory().getOutputTransform(imageProxy)
        val coordinateTransform = CoordinateTransform(imageAnalysisTransform, previewOutputTransform)

        val detections = detectedObjects.map { detectedObjects ->

            val box = detectedObjects.boundingBox
            val rect = RectF(
                box.left.toFloat(),
                box.top.toFloat(),
                box.right.toFloat(),
                box.bottom.toFloat()
            )

            coordinateTransform.mapRect(rect)

            val label = detectedObjects.labels.firstOrNull()?.text?: "Unknown"
            val accuracy = detectedObjects.labels.firstOrNull()?.confidence?: 0f

            DetectObj(
                theBox = androidx.compose.ui.geometry.Rect(
                    left = rect.left,
                    top = rect.top,
                    right = rect.right,
                    bottom = rect.bottom
                ),
                label = label,
                accuracy = accuracy
            )
        }

        onDetectionsFound(detections)
    }
        .addOnFailureListener { exception ->
            Log.e("Object Detection", "Failed", exception)
            onDetectionsFound(emptyList())
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}