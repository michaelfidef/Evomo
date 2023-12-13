package com.bangkit.evomo.main.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.evomo.main.MainActivity
import com.bangkit.evomo.R
import com.bangkit.evomo.data.ScreenCamera
import com.bangkit.evomo.databinding.FragmentHomeBinding
import com.bangkit.evomo.login.LoginActivity
import com.bangkit.evomo.utils.SettingPreferences
import com.bangkit.evomo.utils.SettingViewModel
import com.bangkit.evomo.utils.SettingViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.MPPointF

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var nameList: List<String>
//    private lateinit var machineList: List<Machine>

    var machineTextView: AutoCompleteTextView? = null
    var machineAdapterItems: ArrayAdapter<String>? = null
    private var selectedMachine: String? = null

    //    private lateinit var machineInfo : List<MachineInfo>
    private var In: Int = 0
    private var Out: Int = 0
    private var Reject: Int = 0
    private lateinit var pieChart: PieChart
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance((activity as MainActivity).dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getUserName().observe(viewLifecycleOwner) { dataName ->
            userName = dataName
            binding.titleWelcome.text =
                resources.getString(R.string.title_welcome, dataName.split(" ").firstOrNull())
        }

//        binding.date.text = DateHelper.getCurrentDateNoTime()


        binding.btnCamera.setOnClickListener {
//            dialogFragment.show(childFragmentManager, ModelDialogFragment::class.java.simpleName)
            val intent = Intent(activity, ScreenCamera::class.java)
            startActivity(intent)
        }

        pieChart = binding.pieChart
        val entries: ArrayList<PieEntry> = ArrayList()

        //initialize empty info
        entries.add(PieEntry(0F))
        entries.add(PieEntry(0F))
        entries.add(PieEntry(0F))
        binding.totalNum.text = (In + Out + Reject).toString()
        binding.inOutNum.text = (In + Out).toString()
        binding.rejectNum.text = Reject.toString()
        setChart(entries)

        settingViewModel.getToken().observe(viewLifecycleOwner) { token ->
            Log.d("TOKEN CHECK", token)
            if (token == "Not Set") {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
    }

    private fun setChart(entries: ArrayList<PieEntry>) {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        pieChart.setDrawCenterText(true)

        pieChart.rotationAngle = 0f

        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        val dataSet = PieDataSet(entries, "Product Count")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.green_500))
        colors.add(resources.getColor(R.color.orange_500))
        colors.add(resources.getColor(R.color.red))
        dataSet.colors = colors

        val data = PieData(dataSet)
        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "%.1f%%".format(value)
            }
        }
        data.setValueFormatter(formatter)
        data.setValueTextSize(11f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data

        pieChart.highlightValues(null)

        pieChart.invalidate()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}