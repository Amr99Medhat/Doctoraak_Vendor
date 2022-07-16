package com.doctoraak.doctoraakdoctor.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.databinding.FragmentSignBinding
import com.doctoraak.doctoraakdoctor.ui.signIn.SignInActivity
import com.doctoraak.doctoraakdoctor.ui.signUpDoctor.DoctorSignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpLab.LabSignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpPharmacy.PharmacySignUpActivity
import com.doctoraak.doctoraakdoctor.ui.signUpRadiology.RadiologySignUpActivity

class SignFragment : Fragment()
{

    companion object {
        @JvmStatic
        fun newInstance() = SignFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val binding = DataBindingUtil.inflate<FragmentSignBinding>(inflater, R.layout.fragment_sign
            , container, false)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(context, SignInActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            when (SessionManager.getUserType()!!)
            {
                UserType.DOCTOR -> startActivity(Intent(context, DoctorSignUpActivity::class.java))
                UserType.LAB -> startActivity(Intent(context, LabSignUpActivity::class.java))
                UserType.PHARMACY -> startActivity(Intent(context, PharmacySignUpActivity::class.java))
                UserType.RADIOLOGY -> startActivity(Intent(context, RadiologySignUpActivity::class.java))
            }
        }

        binding.ivBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

//        binding.ivLogo.setBackgroundResource(when (SessionManager.getUserType()!!)
//        {
//            UserType.DOCTOR -> if (SessionManager.getIsDoctor_MedicalCenter()) R.drawable.ic_medical_center else R.drawable.ic_doctor_type
//            UserType.PHARMACY -> R.drawable.ic_pharmacy_type
//            UserType.LAB -> R.drawable.ic_lab_type
//            UserType.RADIOLOGY -> R.drawable.ic_radiology_type
//        })

        return binding.root
    }






}
