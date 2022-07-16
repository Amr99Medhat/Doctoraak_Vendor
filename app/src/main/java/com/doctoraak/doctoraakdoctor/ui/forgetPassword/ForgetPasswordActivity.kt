package com.doctoraak.doctoraakdoctor.ui.forgetPassword

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.databinding.ActivityForgetPasswordBinding
import com.doctoraak.doctoraakdoctor.services.SmsBroadcastReceiver
import com.doctoraak.doctoraakdoctor.services.SmsListener
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.signIn.SignInActivity
import com.doctoraak.doctoraakdoctor.utils.*
import java.security.Permission
import java.security.Permissions

class ForgetPasswordActivity : BaseActivity(), SmsListener
{
    companion object{
        const val USER_TYPE_INTENT = "USER_TYPE_INTENT"
        private const val REQ_CODE_READ_SMS: Int = 5

    }
    private lateinit var binding: ActivityForgetPasswordBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(ForgetPasswordViewModel::class.java) }
    private val userType by lazy { SessionManager.getUserType() }
    private lateinit var smsReceiver: SmsBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)

        binding.lifecycleOwner = this
        binding.clickHandler = ForgetPasswordClickHandler()

        observeData()

        startSmsReceiverPermission()
    }

    private fun startSmsReceiverPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this
                , arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS), REQ_CODE_READ_SMS)
        }
        else
            startSmsReceiver()
    }

    private fun startSmsReceiver()
    {
        smsReceiver = SmsBroadcastReceiver().apply {
            listener = this@ForgetPasswordActivity
        }

        val filter = IntentFilter(SMS_RECEIVED_ACTION)
        registerReceiver(smsReceiver, filter, Manifest.permission.READ_SMS, null)
    }

    private fun stopSmsReceiver()
    {
        if (::smsReceiver.isInitialized)
            unregisterReceiver(smsReceiver)
    }

    private fun startLoading()
    {
        binding.btnConfirmPhone.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnConfirmPhone.isEnabled = true
        binding.loading.loadingView.hide()
    }


    private fun observeData()
    {
        viewModel.isLoading.observe(this, Observer {
            if (it) startLoading() else stopLoading()
        })

        viewModel.errorInt.observe(this, Observer {
            it.showError(this)
        })

        viewModel.errorMsg.observe(this, Observer { showError(it) })

        viewModel.isForgetPassStep.observe(this, Observer {
            binding.ivBack.hide()
            binding.etlPhone.animateOutInX(binding.viewGroupCodePassword)
        })

        viewModel.isUpdatePassStep.observe(this, Observer {
            toast(R.string.success)
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        })

        viewModel.isResendSms.observe(this, Observer {
            toast(getString(R.string.sms_sent))
        })
    }

    override fun onSmsCodeReceived(text: String)
    {
        Log.d("saif", "onSmsCodeReceived text= $text")
        binding.etCode.setText(text)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CODE_READ_SMS && grantResults.size > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startSmsReceiver()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopSmsReceiver()
    }

    inner class ForgetPasswordClickHandler
    {
        fun onConfirm()
        {
            val phone = binding.etPhone.text.toString()

            if (binding.etlPhone.visibility == View.VISIBLE)
            {
                if (phone.validatePhone(binding.etlPhone))
                    viewModel.forgetPassword(userType!!, phone)
            }
            else
            {
                val code = binding.etCode.text.toString()
                val newPassword = binding.etPassword.text.toString()

                val isCode = code.validateVerificationCode(binding.etlCode)
                val isPassword = newPassword.validatePasswordAndConfirmPassword(binding.etlPassword
                    , binding.etConfirmPassword.text.toString(), binding.etlConfirmPassword)

                if (isCode && isPassword)
                    viewModel.updatePassword(userType!!, phone, code, newPassword)
            }
        }

        fun onResendSms()
        {
            viewModel.resendSms(userType!!, binding.etPhone.text.toString())
        }

        fun onBack()
        {
            onBackPressed()
        }
    }

}
