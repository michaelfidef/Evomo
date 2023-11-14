package com.bangkit.evomo

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var counterTextView: TextView
    private lateinit var defectiveCountTextView: TextView
    private lateinit var reworkCountTextView: TextView
    private lateinit var sumTextView: TextView
    private lateinit var incrementButton: Button
    private lateinit var defectiveButton: Button
    private lateinit var reworkButton: Button
    private lateinit var pieChart: PieChart

    private var totalCounter = 0
    private var defectiveCounter = 0
    private var reworkCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        counterTextView = findViewById(R.id.counterTextView)
        defectiveCountTextView = findViewById(R.id.defectiveCountTextView)
        reworkCountTextView = findViewById(R.id.reworkCountTextView)
        sumTextView = findViewById(R.id.sumTextView)
        incrementButton = findViewById(R.id.incrementButton)
        defectiveButton = findViewById(R.id.defectiveButton)
        reworkButton = findViewById(R.id.reworkButton)
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
}
