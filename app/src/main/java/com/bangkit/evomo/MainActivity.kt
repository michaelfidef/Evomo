package com.bangkit.evomo

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bangkit.evomo.data.ScreenCamera
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader


class MainActivity : AppCompatActivity() {

    private lateinit var counterTextView: TextView
    private lateinit var defectiveCountTextView: TextView
    private lateinit var reworkCountTextView: TextView
    private lateinit var sumTextView: TextView
    private lateinit var incrementButton: Button
    private lateinit var defectiveButton: Button
    private lateinit var reworkButton: Button
    private lateinit var btnCamera: Button

    private lateinit var pieChart: PieChart

    private var totalCounter = 0
    private var defectiveCounter = 0
    private var reworkCounter = 0

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        get_permission()


        counterTextView = findViewById(R.id.counterTextView)
        defectiveCountTextView = findViewById(R.id.defectiveCountTextView)
        reworkCountTextView = findViewById(R.id.reworkCountTextView)
        sumTextView = findViewById(R.id.sumTextView)
        incrementButton = findViewById(R.id.incrementButton)
        defectiveButton = findViewById(R.id.defectiveButton)
        reworkButton = findViewById(R.id.reworkButton)
        btnCamera = findViewById(R.id.btnCamera)
        pieChart = findViewById(R.id.pieChart)

        incrementButton.setOnClickListener {
            incrementCounter()
        }

        defectiveButton.setOnClickListener {
            incrementDefectiveCounter()
        }

        reworkButton.setOnClickListener {
            incrementReworkCounter()
        }

        btnCamera.setOnClickListener {
            onClickCamera()
        }

        updateCounterTextView()
        setupPieChart()
    }

    private fun incrementCounter() {
        totalCounter++
        updateCounterTextView()
        updatePieChart()
    }

    private fun incrementDefectiveCounter() {
        defectiveCounter++
        updateCounterTextView()
        updatePieChart()
    }

    private fun incrementReworkCounter() {
        reworkCounter++
        updateCounterTextView()
        updatePieChart()
    }

    private fun onClickCamera() {
        val intent = Intent(this@MainActivity, ScreenCamera::class.java)
        startActivity(intent)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            Log.d("info", "URI: $currentImageUri")

            testDetect()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun testDetect() {
        var cameraBridgeViewBase: CameraBridgeViewBase

    }

    private fun updateCounterTextView() {
        counterTextView.text = "Pass: $totalCounter"
        defectiveCountTextView.text = "Defective: $defectiveCounter"
        reworkCountTextView.text = "Rework: $reworkCounter"

        val sum = totalCounter + defectiveCounter + reworkCounter
        sumTextView.text = "Sum: $sum"
    }

    private fun setupPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setHoleColor(Color.TRANSPARENT)

        updatePieChart()
    }

    private fun updatePieChart() {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(defectiveCounter.toFloat(), "Defective"))
        entries.add(PieEntry(reworkCounter.toFloat(), "Rework"))
        entries.add(PieEntry(totalCounter.toFloat(), "Pass"))

        val dataSet = PieDataSet(entries, "Categories")
        dataSet.colors = mutableListOf(Color.RED, Color.YELLOW, Color.GREEN)
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        pieChart.data = data
        pieChart.invalidate()
    }

    fun get_permission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }
}
