package com.doctoraak.doctoraakdoctor.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakdoctor.Adapter.SpinnerAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivitySignInBinding
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.ui.forgetPassword.ForgetPasswordActivity
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.labHome.LabHomeActivity
import com.doctoraak.doctoraakdoctor.ui.pharmacyHome.PharmacyHomeActivity
import com.doctoraak.doctoraakdoctor.ui.radiologyHome.RadiologyHomeActivity
import com.doctoraak.doctoraakdoctor.ui.doctorHome.DoctorHomeActivity
import com.doctoraak.doctoraakdoctor.ui.profileDoctor.DoctorProfileActivity
import com.doctoraak.doctoraakdoctor.ui.signUpDoctor.DoctorSignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpLab.LabSignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpPharmacy.PharmacySignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpRadiology.RadiologySignUpActivity
import com.doctoraak.doctoraakdoctor.utils.*

class SignInActivity : BaseActivity()
{
    private lateinit var binding: ActivitySignInBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(SignInViewModel::class.java) }
    private val userType: UserType by lazy { SessionManager.getUserType()!! }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        binding.lifecycleOwner = this
        binding.clickHandler = SignInClickHandler()

        observeData()
    }

    private fun startLoading()
    {
        binding.btnLogin.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnLogin.isEnabled = true
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

        viewModel.userResponse.observe(this, Observer {
            when (userType)
            {
                UserType.LAB -> {
                    SessionManager.logIn(userType, (it as LabResponse).data!!)
                    startActivity(Intent(this, LabHomeActivity::class.java))
                }
                UserType.PHARMACY -> {
                    SessionManager.logIn(userType, (it as PharmacyResponse).data!!)
                    startActivity(Intent(this, PharmacyHomeActivity::class.java))
                }
                UserType.RADIOLOGY -> {
                    SessionManager.logIn(userType, (it as RadiologyResponse).data!!)
                    startActivity(Intent(this, RadiologyHomeActivity::class.java))
                }
                else -> {
                    SessionManager.logIn(userType, (it as DoctorResponse).data!!)
                    startActivity(Intent(this, DoctorHomeActivity::class.java))
                }
            }
            finishAffinity()
        })
    }


    inner class SignInClickHandler
    {
        fun onForgetPassword() = with(Intent(this@SignInActivity, ForgetPasswordActivity::class.java))
        {
            putExtra(ForgetPasswordActivity.USER_TYPE_INTENT, userType)
            startActivity(this)
        }

        fun onSignUp() = when(userType)
        {
            UserType.DOCTOR, UserType.HOSPITAL, UserType.MEDICAL_CENTER, UserType.OPTICAL_CENTER ->
                startActivity(Intent(this@SignInActivity, DoctorSignUpActivity::class.java))
            UserType.LAB -> startActivity(Intent(this@SignInActivity, LabSignUpActivity::class.java))
            UserType.PHARMACY -> startActivity(Intent(this@SignInActivity, PharmacySignUpActivity::class.java))
            UserType.RADIOLOGY -> startActivity(Intent(this@SignInActivity, RadiologySignUpActivity::class.java))
        }

        fun onLogIn(phone: String = "", password: String = "")
        {
            val isPhone = phone.validatePhone(binding.etlPhone)
            val isPassword = password.validatePassword(binding.etlPassword)

            if (isPhone && isPassword)
                viewModel.logIn(userType = userType, phone = phone, password = password)
        }

        fun onBack()
        {
            onBackPressed()
        }
    }

}
