package com.bangkit.evomo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var counterTextView: TextView
    private lateinit var defectiveCountTextView: TextView
    private lateinit var reworkCountTextView: TextView
    private lateinit var sumTextView: TextView
    private lateinit var incrementButton: Button
    private lateinit var defectiveButton: Button
    private lateinit var reworkButton: Button

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
    }

    private fun incrementCounter() {
        totalCounter++
        updateCounterTextView()
    }

    private fun incrementDefectiveCounter() {
        defectiveCounter++
        updateCounterTextView()
    }

    private fun incrementReworkCounter() {
        reworkCounter++
        updateCounterTextView()
    }

    private fun updateCounterTextView() {
        counterTextView.text = "Total: $totalCounter"
        defectiveCountTextView.text = "Defective: $defectiveCounter"
        reworkCountTextView.text = "Rework: $reworkCounter"

        val sum = totalCounter + defectiveCounter + reworkCounter
        sumTextView.text = "Sum: $sum"
    }
}