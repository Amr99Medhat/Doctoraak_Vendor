package com.doctoraak.doctoraakdoctor.ui.mobileVerification

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Telephony
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityMobileVerificationBinding
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.services.SmsBroadcastReceiver
import com.doctoraak.doctoraakdoctor.services.SmsListener
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.payment.PaymentActivity
import com.doctoraak.doctoraakdoctor.ui.signUpClinic.ClinicSignUpActivity
import com.doctoraak.doctoraakdoctor.utils.*


class MobileVerificationActivity : BaseActivity(), SmsListener
{
    private val REQ_CODE_READ_SMS: Int = 5

    private lateinit var binding: ActivityMobileVerificationBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(MobileVerificationViewModel::class.java) }

    private lateinit var smsReceiver: SmsBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mobile_verification)
        binding.clickHandler = MobileVerificationClickHandler()
        observeData()
        startSmsReceiverPermission()

        viewModel.continueUserId.value = SessionManager.getUserIdForRegisterMobileVerificationMode()

        movingCursorToNext()
        startCounterForSms()
    }

    private fun movingCursorToNext()
    {
        val handleEditText: (EditText,EditText)->Unit = { et, nextEt ->
            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int)
                {
                    if (!text.isNullOrBlank())
                        nextEt.requestFocus(0)
                }
            })
        }

        with(binding)
        {
            handleEditText(etSmsCode1, etSmsCode2)
            handleEditText(etSmsCode2, etSmsCode3)
            handleEditText(etSmsCode3, etSmsCode4)
            handleEditText(etSmsCode4, etSmsCode5)
        }
    }

    private fun startCounterForSms()
    {
        binding.tvResendSmsCounter.setTextColor(ResourcesCompat.getColor(resources, R.color.sms_counter_play, null))
        binding.tvResendSms.setTextColor(ResourcesCompat.getColor(resources, R.color.gray_2, null))
        binding.tvResendSms.disable()

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendSmsCounter.setText("${millisUntilFinished/1000}")
            }
            override fun onFinish() {
                binding.tvResendSmsCounter.setText("00:00")
                binding.tvResendSmsCounter.setTextColor(ResourcesCompat.getColor(resources, R.color.gray_2, null))
                binding.tvResendSms.setTextColor(ResourcesCompat.getColor(resources, R.color.sms_counter_stop, null))
                binding.tvResendSms.enable()
            }
        }.start()
    }

    private fun startSmsReceiverPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                this
                , arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS), REQ_CODE_READ_SMS
            )
        } else
            startSmsReceiver()
    }

    private fun startSmsReceiver() {
        smsReceiver = SmsBroadcastReceiver().apply {
            listener = this@MobileVerificationActivity
        }

        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        registerReceiver(smsReceiver, filter, Manifest.permission.READ_SMS, null)
    }

    private fun stopSmsReceiver() {
        if (::smsReceiver.isInitialized)
            unregisterReceiver(smsReceiver)
    }

    private fun startLoading()
    {
        binding.btnConfirmPhone.disable()
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnConfirmPhone.enable()
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

        viewModel.isActive.observe(this, Observer {
            when (it)
            {
                is DoctorResponse? -> (it as DoctorResponse?)?.data?.let { successVerification(it, it.id) }
                is PharmacyResponse? -> (it as PharmacyResponse?)?.data?.let { successVerification(it, it.id) }
                is LabResponse? -> (it as LabResponse?)?.data?.let { successVerification(it, it.id) }
                is RadiologyResponse? -> (it as RadiologyResponse?)?.data?.let { successVerification(it, it.id) }
            }
        })

        viewModel.isResendSms.observe(this, Observer {
            Utils.showSuccess(this, getString(R.string.sms_code_is_resent))
        })
    }

    private fun successVerification(user: User, userId: Long)
    {
        SessionManager.clearUserIdForRegisterMobileVerificationMode()

        val userType = SessionManager.getUserType()!!
        SessionManager.logIn(userType, user)
        when (userType)
        {
            UserType.DOCTOR -> {
                startActivity(Intent(this@MobileVerificationActivity, ClinicSignUpActivity::class.java))
            }
            else -> {
                PaymentActivity.launch(this@MobileVerificationActivity, userId, userType)
            }
        }

        finishAffinity()
    }


    override fun onSmsCodeReceived(text: String)
    {
        Log.d("saif", "onSmsCodeReceived text= $text")

        binding.etSmsCode1.setText(text.getOrNull(0)?.toString() ?: "")
        binding.etSmsCode2.setText(text.getOrNull(1)?.toString() ?: "")
        binding.etSmsCode3.setText(text.getOrNull(2)?.toString() ?: "")
        binding.etSmsCode4.setText(text.getOrNull(3)?.toString() ?: "")
        binding.etSmsCode5.setText(text.getOrNull(4)?.toString() ?: "")
        binding.clickHandler?.onConfirmSms()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        if (requestCode == REQ_CODE_READ_SMS && grantResults.size > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            startSmsReceiver()
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSmsReceiver()
    }

    inner class MobileVerificationClickHandler
    {
        fun onConfirmSms()
        {
            val smsCode = "${binding.etSmsCode1.text}${binding.etSmsCode2.text}${binding.etSmsCode3.text}${binding.etSmsCode4.text}${binding.etSmsCode5.text}"

            if (smsCode. validateEmpty(binding.viewGroup) && smsCode.length == 5)
                viewModel.active(SessionManager.getUserType()!!, viewModel.continueUserId.value!!, smsCode.toInt())
        }

        fun onResendSmsCode()
        {
            viewModel.resendSms(SessionManager.getUserType()!!, viewModel.continueUserId.value!!)
        }
    }

}
