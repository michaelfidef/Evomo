package com.bangkit.evomo.data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.evomo.databinding.ActivityScreenCameraBinding
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat

class ScreenCamera : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var binding: ActivityScreenCameraBinding

    private var cameraBridgeViewBase: CameraBridgeViewBase? = null

//    private var grayFrame: Mat? = null
//    private var blurFrame: Mat? = null
//    private var rgbFrame: Mat? = null
//    private var outFrame: Mat? = null

    private lateinit var mCurrentFrame: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScreenCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraBridgeViewBase = binding.cameraView;

        initListener()
    }

    private fun initListener() {
        cameraBridgeViewBase!!.setCvCameraViewListener(this)
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        //
    }

    override fun onCameraViewStopped() {
        //
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        if (inputFrame != null) {
            mCurrentFrame = inputFrame.rgba()
        }

        return mCurrentFrame
    }

}