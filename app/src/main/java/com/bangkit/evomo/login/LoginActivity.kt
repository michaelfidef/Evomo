package com.bangkit.evomo.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bangkit.evomo.main.MainActivity
import com.bangkit.evomo.R
import com.bangkit.evomo.ViewModelFactory
import com.bangkit.evomo.databinding.ActivityLoginBinding
import com.bangkit.evomo.utils.SettingPreferences
import com.bangkit.evomo.utils.SettingViewModel
import com.bangkit.evomo.utils.SettingViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var username: String
    private lateinit var password: String
    private val dialogFragment = ModalTnC()
    private val isAgreeLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = obtainViewModel(this)
        val pref = SettingPreferences.getInstance((this).dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        binding.buttonSignin.setOnClickListener {
            username = binding.edLoginUsername.text.toString().trim()
            password = binding.edLoginPassword.text.toString().trim()

            if (TextUtils.isEmpty(username)) {
                binding.edLoginUsername.error = "Field must be filled"
            } else if (TextUtils.isEmpty(password)) {
                binding.edLoginPassword.error = "Field must be filled"
            } else if (binding.edLoginPassword.error?.length ?: 0 > 0) {
                binding.edLoginPassword.requestFocus()
            } else {
                viewModel.login(username, password)
                viewModel.loginUser.observe(this) { loginResponse ->
                    if (loginResponse.success == true) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val mIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(mIntent)
                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                viewModel.isError.observe(this) { isError ->
                    if (isError) {
                        val builder = AlertDialog.Builder(this, R.style.LogoutDialog)
                        with(builder) {
                            setTitle(R.string.modal_login_error_title)
                            setMessage(R.string.modal_login_error)
                            setNegativeButton(R.string.btn_try_again) { dialogInterface: DialogInterface, i: Int ->
                                dialogInterface.cancel()
                            }
                            setIcon(R.drawable.ic_baseline_warning_24_yellow);
                        }

                        val alertDialog = builder.create()
                        alertDialog.show()

                        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                        with(negativeButton) {
                            isAllCaps = false
                            setTextColor(resources.getColor(R.color.red))
                        }
                    }
                }
            }
        }

        viewModel.isLoading.observe(this)
        {
            showLoading(it)
        }

// belum bisa menampilkan fragment
        viewModel.loginUser.observe(this)
        { login ->
            login.data?.isAgreeTnc?.let { setIsAgree(it) }
            isAgreeLiveData.observe(this) { isAgree ->
                if (!isAgree) {
                    // popup
                    val bundle = Bundle()
                    bundle.putString("UserId", login.data?.userid)
                    dialogFragment.arguments = bundle
                    dialogFragment.show(supportFragmentManager, ModalTnC::class.java.simpleName)
                } else {
                    val currentDate = Calendar.getInstance().time
                    val calendar = Calendar.getInstance()
                    calendar.time = currentDate
                    calendar.add(Calendar.MINUTE, 100)
                    val expiredDate = calendar.time
                    val dateFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val formattedDate = dateFormat.format(expiredDate)

//                    settingViewModel.setUserPreferences(
//                        login.data.accessToken,
//                        login.data.name,
//                        username,
//                        login.data.userid,
//                        formattedDate.toString()
//                    )
                }

//                showLoading(true)
//                    showLoading(false)
            }
        }
    }

    fun setIsAgree(value: Boolean) {
        isAgreeLiveData.value = value
    }

    private fun obtainViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}