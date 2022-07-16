package com.doctoraak.doctoraakdoctor.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityUserTypeBinding
import com.doctoraak.doctoraakdoctor.utils.*

class UserTypeActivity : BaseActivity()
{
    private lateinit var binding: ActivityUserTypeBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_type)

        val showSignFragment: ()-> Unit = {
            supportFragmentManager.beginTransaction()
                .add(R.id.view_group_fragment, SignFragment.newInstance())
                .addToBackStack("SignFragment")
                .commit()
        }

        binding.itcvMedicalCenter.setOnClickListener {
            showSignFragment()
            SessionManager.setUserType(UserType.MEDICAL_CENTER)
        }

        binding.itcvHospital.setOnClickListener {
            showSignFragment()
            SessionManager.setUserType(UserType.HOSPITAL)
        }

        binding.itcvOpticalCenter.setOnClickListener {
            showSignFragment()
            SessionManager.setUserType(UserType.OPTICAL_CENTER)
        }

        binding.itcvDoctor.setOnClickListener {
            showSignFragment()
            SessionManager.setUserType(UserType.DOCTOR)
        }

        binding.itcvLab.setOnClickListener {
            showSignFragment()
            SessionManager.setUserType(UserType.LAB)
        }

        binding.itcvPharmacy.setOnClickListener {
            showSignFragment()
            SessionManager.setUserType(UserType.PHARMACY)
        }

        binding.itcvRadiology.setOnClickListener {
            showSignFragment()
            SessionManager.setUserType(UserType.RADIOLOGY)
        }

        binding.viewGroup.invisible()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus)
            binding.viewGroup.post {
                binding.viewGroup.show()
                binding.viewGroup.animateScaleInWithOrder() }
    }


}
