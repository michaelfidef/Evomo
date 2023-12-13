package com.bangkit.evomo.main.profile

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.evomo.R
import com.bangkit.evomo.databinding.FragmentProfileBinding
import com.bangkit.evomo.login.LoginActivity
import com.bangkit.evomo.main.MainActivity
import com.bangkit.evomo.utils.SettingPreferences
import com.bangkit.evomo.utils.SettingViewModel
import com.bangkit.evomo.utils.SettingViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance((activity as MainActivity).dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getUserName().observe(viewLifecycleOwner) { dataName ->
            binding.userName.text = dataName.toString()
        }

        settingViewModel.getUserUsername().observe(viewLifecycleOwner) { dataName ->
            binding.userEmail.text = dataName.toString()
        }

        binding.logoutGroup.setOnClickListener {
            val builder = AlertDialog.Builder(activity, R.style.LogoutDialog)
            with(builder) {
                setTitle(R.string.modal_logout_title)
                setMessage(R.string.modal_logout_desc)
                setNegativeButton(R.string.btn_cancel) { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                }
                setPositiveButton(R.string.btn_logout) { dialogInterface: DialogInterface, i: Int ->
                    signOut()
                }
                setIcon(R.drawable.ic_baseline_warning_24_yellow);
            }

            val alertDialog = builder.create()
            alertDialog.show()

            val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            with(positiveButton) {
                isAllCaps = false
                setTextColor(resources.getColor(R.color.red))
            }

            val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            with(negativeButton) {
                isAllCaps = false
                setTextColor(resources.getColor(R.color.black))
            }
        }
    }

    private fun signOut() {
        val pref = SettingPreferences.getInstance((activity as MainActivity).dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.clearUserPreferences()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()
    }
}