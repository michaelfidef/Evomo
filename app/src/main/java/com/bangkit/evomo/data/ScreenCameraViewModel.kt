package com.bangkit.evomo.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.util.concurrent.Executors

class ScreenCameraViewModel(application: Application) : AndroidViewModel(application) {

    private val executor = Executors.newSingleThreadExecutor()

    fun runOnBackground(runnable: Runnable) {
        executor.execute(runnable)
    }
}