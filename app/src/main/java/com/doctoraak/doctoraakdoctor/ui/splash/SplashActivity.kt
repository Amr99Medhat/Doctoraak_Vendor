package com.doctoraak.doctoraakdoctor.ui.splash

import android.animation.Animator
import android.content.Intent
import android.graphics.drawable.Animatable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.InternetConnection
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.databinding.ActivitySplashBinding
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.ui.*
import com.doctoraak.doctoraakdoctor.ui.doctorHome.DoctorHomeActivity
import com.doctoraak.doctoraakdoctor.ui.forgetPassword.ForgetPasswordActivity
import com.doctoraak.doctoraakdoctor.ui.labHome.LabHomeActivity
import com.doctoraak.doctoraakdoctor.ui.mobileVerification.MobileVerificationActivity
import com.doctoraak.doctoraakdoctor.ui.payment.PaymentActivity
import com.doctoraak.doctoraakdoctor.ui.pharmacyHome.PharmacyHomeActivity
import com.doctoraak.doctoraakdoctor.ui.profileDoctor.DoctorProfileActivity
import com.doctoraak.doctoraakdoctor.ui.radiologyHome.RadiologyHomeActivity
import com.doctoraak.doctoraakdoctor.ui.signUpDoctor.DoctorSignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpLab.LabSignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpPharmacy.PharmacySignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpRadiology.RadiologySignUpActivity


class SplashActivity : AppCompatActivity()
{
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        getSpecialization()
        getArea()
        getCity()
        getDegree()
        getInsurances()
        getContact()
    }

    override fun onResume()
    {
        super.onResume()

        startAnimateSplashScreen()

        val notLogIn = {
            startActivity(Intent(this@SplashActivity, UserTypeActivity::class.java))
        }

        val handler = Handler()
        handler.postDelayed({
            val user_id = SessionManager.getUserIdForRegisterMobileVerificationMode()

            if (user_id != -1L)
            {
                when (SessionManager.getUserType())
                {
                    UserType.DOCTOR, UserType.LAB, UserType.PHARMACY, UserType.RADIOLOGY ->
                        startActivity(Intent(this@SplashActivity, MobileVerificationActivity::class.java))
                    else -> notLogIn()
                }
            }
            else if (SessionManager.isLogIn())
            {
                when (SessionManager.getUserType())
                {
                    UserType.LAB -> startActivity(Intent(this@SplashActivity, LabHomeActivity::class.java))
                    UserType.PHARMACY -> startActivity(Intent(this@SplashActivity, PharmacyHomeActivity::class.java))
                    UserType.RADIOLOGY -> startActivity(Intent(this@SplashActivity, RadiologyHomeActivity::class.java))
                    UserType.DOCTOR, UserType.HOSPITAL, UserType.MEDICAL_CENTER, UserType.OPTICAL_CENTER ->
                        startActivity(Intent(this@SplashActivity, DoctorHomeActivity::class.java))
                    else -> notLogIn()
                }
            }
            else
                notLogIn()

            finishAffinity()
        }, 4000)
    }

    private fun startAnimateSplashScreen()
    {
        val heartAnimatable = binding.ivHeartbeat.drawable as Animatable
        heartAnimatable.start()

        animate(binding.ivLogo)
        //nimate(binding.ivCareLogo)
    }

    private fun animate(it: View, xCor :Float = -450f, scaleDownUp: Boolean = true)
    {
        it.animate().apply {
            startDelay = 500
            duration = 1000
            rotationYBy(180f)

            setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    animate(it)//, xCor * -1, !scaleDownUp)
                }
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            })
        }.start()
    }


    override fun onPause()
    {
        super.onPause()

        binding.ivHeartbeat.clearAnimation()
        binding.ivLogo.clearAnimation()
    }

    fun getSpecialization() = SplashRepository.getSpecialization(success = { model: SpecializationResponse ->
        model.data?.let { SessionManager.saveSpecialization(it) } }
        , loading = {  }
        , errorMsg = {  }
        , error = {i, s -> })

    fun getDegree() = SplashRepository.getDegree(success = { model: DegreeResponse ->
        model.list?.let { SessionManager.saveDegree(it) } }
        , loading = { }
        , errorMsg = {  }
        , error = {i, s -> })

    fun getInsurances() = SplashRepository.getInsurances(success = { model: InsuranceResponse ->
        model.data?.let { SessionManager.saveInsurance(it) } }
        , loading = {  }
        , errorMsg = {  }
        , error = {i, s -> })

    fun getCity() = SplashRepository.getCity(success = { model: CityResponse ->
        model.list?.let { SessionManager.saveCity(it) } }
        , loading = {  }
        , errorMsg = {  }
        , error = {i, s -> })

    fun getArea() = SplashRepository.getArea(success = { model: AreaResponse ->
        model.list?.let { SessionManager.saveArea(it) } }
        , loading = {  }
        , errorMsg = {  }
        , error = {i, s -> })

    fun getContact() = SplashRepository.getContact(success = { model: ContactResponse ->
        model.list?.let { SessionManager.saveContact(it) } }
        , loading = {  }
        , errorMsg = {  }
        , error = {i, s -> })


}
