package com.bangkit.evomo.data

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.evomo.databinding.ActivityScreenCameraBinding
import com.bangkit.evomo.ml.Yolov5DatasetSaveme
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.nnapi.NnApiDelegate
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScreenCamera : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var binding: ActivityScreenCameraBinding
    private lateinit var cameraBridgeViewBase: CameraBridgeViewBase
    private lateinit var mCurrentFrame: Mat
    private lateinit var tflite: Yolov5DatasetSaveme
    private lateinit var viewModel: ScreenCameraViewModel
    private lateinit var label : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScreenCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraBridgeViewBase = binding.cameraView

        initListener()
        initializeTFLiteModel()
        label = FileUtil.loadLabels(this, "label.txt")

        viewModel = ViewModelProvider(this).get(ScreenCameraViewModel::class.java)

        if (OpenCVLoader.initDebug()) {
            cameraBridgeViewBase.enableView()
        }
    }

    private fun initListener() {
        cameraBridgeViewBase.setCvCameraViewListener(this)
    }

    private fun initializeTFLiteModel() {

        val tfliteOptions = Interpreter.Options()

        // Apply TensorFlow Lite NnApi delegate
        tfliteOptions.addDelegate(NnApiDelegate())

        tflite = Yolov5DatasetSaveme.newInstance(this)

    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        // Initialize any resources when the camera view starts
    }

    override fun onCameraViewStopped() {
        // Release any resources when the camera view stops
        tflite.close()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {

        if (inputFrame != null) {
            // Get the current frame
            mCurrentFrame = inputFrame.rgba();
            Log.d("TAG", "DISINI")

            viewModel.runOnBackground {

                val resizedFrame = Mat()
                Imgproc.resize(mCurrentFrame, resizedFrame, Size(320.0, 240.0))

                Log.d("TAG", "DISITU")
                val byteBuffer = convertMatToByteBuffer(resizedFrame)
                // Convert the current frame to a TensorBuffer
                val inputFeature0 =
                    TensorBuffer.createFixedSize(intArrayOf(1, 416, 416, 3), DataType.FLOAT32);
                // ... Convert mCurrentFrame to inputFeature0 format ...

                // Load the converted frame into the input tensor
                inputFeature0.loadBuffer(byteBuffer);

                // Initialize the ConvertedModel instance
                val model = Yolov5DatasetSaveme.newInstance(this);

                // Run model inference and get results
                val outputs = model.process(inputFeature0);
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer;

                // ... Process and display the results ...
                processTFLiteOutput(outputFeature0)


                // Release model resources
                model.close();

                Log.d("TAG", "DISINI1")
            }
        }
        return mCurrentFrame
    }

    private fun convertMatToByteBuffer(mat: Mat): ByteBuffer {
        val width = mat.width()
        val height = mat.height()
        val channels = mat.channels()

        val byteBuffer = ByteBuffer.allocateDirect(2076672)
        byteBuffer.order(ByteOrder.nativeOrder())

        // Loop through each pixel
        for (y in 0 until height) {
            for (x in 0 until width) {
                // Get the pixel data
                val data = mat.get(y, x)

                // Access and copy each channel data

                val value = FloatArray(channels)
                for (channel in 0 until channels) {
                    value[channel] = data[channel].toFloat()
                }

                var float = 0.0f
                for (x in value) {
                    float += x
                }

                byteBuffer.putFloat(float)
            }
        }

        return byteBuffer
    }

    private fun processTFLiteOutput(outputFeature0: TensorBuffer) {
        // Get the output data as a FloatArray
        val outputData = outputFeature0.floatArray
        Log.d("TAG", outputData.size.toString())

        val targetClassIndex = label.indexOf("bottle")

        // Check if the model output represents a single value or a list of values
        if (outputData.size > 1) {
            // Single output value, likely representing a classification score or confidence level
            val confidence = outputData[0]

            if (confidence > 0.5f) {
                // Object detected with high confidence
                // Update UI or take further actions
                Log.d("TAG", "Object detected with confidence: $confidence")
            } else {
                // No object detected with sufficient confidence
                Log.d("TAG", "No object detected")
            }
        } else if (outputData.size == 1) {
            // Multiple output values, likely representing bounding boxes or other complex data
            if (outputData.size % 4 == 0) {
                // Assume output format is a list of bounding boxes with x1, y1, x2, y2 coordinates
                for (i in 0 until outputData.size step 4) {
                    val x1 = outputData[i]
                    val y1 = outputData[i + 1]
                    val x2 = outputData[i + 2]
                    val y2 = outputData[i + 3]

                    // Process each bounding box based on the application requirements
                    // You can draw the bounding box on the camera preview here
                    Log.d("TAG", "Detected bounding box: x1=$x1, y1=$y1, x2=$x2, y2=$y2")
                }
            } else {
                // Unrecognized output format
                Log.e("TAG", "Unexpected TFLite model output format")
            }
        } else {
            // Unexpected output size
            Log.e("TAG", "Unexpected TFLite model output size")
        }
    }
}