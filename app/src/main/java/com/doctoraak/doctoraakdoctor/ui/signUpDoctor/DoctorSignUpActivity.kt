package com.doctoraak.doctoraakdoctor.ui.signUpDoctor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.view.View
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.core.util.keyIterator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakdoctor.databinding.ActivityDoctorSignUpBinding
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.utils.*
import java.lang.StringBuilder
import com.doctoraak.doctoraakdoctor.Adapter.SpinnerAdapter
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.ui.mobileVerification.MobileVerificationActivity
import com.tiper.MaterialSpinner
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst

class DoctorSignUpActivity : BaseActivity()
{
    private val REQ_CODE_READ_SMS: Int = 5
    private val PERMISSION_READ_EXTERNAL_STORAGE_CV_CODE = 23
    private val PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE = 45
    private val PICK_PROFILE_IMAGE_REQ: Int = 6
    private val PICK_CV_REQ: Int = 3

    private lateinit var binding: ActivityDoctorSignUpBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(DoctorSignUpViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_sign_up)

        val clickHandler = DoctorSignUpClickHandler()
        binding.lifecycleOwner = this
        binding.clickHandler = clickHandler
        binding.viewModel = viewModel

        handleIsDoctorOrElse()

        observeData()
        initViews()
        askReadSmsPermission()
    }

    private fun askReadSmsPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                this
                , arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS), REQ_CODE_READ_SMS
            )
        }
    }

    private fun handleIsDoctorOrElse()
    {
        if (SessionManager.getUserType() == UserType.DOCTOR)
        {
            binding.tvSubTitleText.text = getString(R.string.create_doctor_account)
            binding.genderGroup.rgGender.show()
        }
        else
        {
            binding.tvSubTitleText.text = getString(when (SessionManager.getUserType())
            {
                UserType.MEDICAL_CENTER -> R.string.create_medical_center_account
                UserType.OPTICAL_CENTER -> R.string.create_optical_center_account
                UserType.HOSPITAL -> R.string.create_hospital_account
                else -> R.string.create_hospital_account
            })

            binding.genderGroup.rgGender.hide()
            viewModel.doctor.value!!.gender = Gender.MALE.value
        }

    }

    private fun initViews()
    {
        val specialization = SessionManager.getSpecialization()
        val degree = SessionManager.getDegree()

        binding.splSpecialization.adapter = SpinnerAdapter(
            this@DoctorSignUpActivity,
            specialization
        )
        binding.splSpecialization.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)
            {

                viewModel.doctor.value?.specializationId = specialization[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner) {}
        }

        binding.splDegree.adapter =
            SpinnerAdapter(this@DoctorSignUpActivity, degree)
        binding.splDegree.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)
            {
                viewModel.doctor.value?.degreeId = degree[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner) {}
        }

    }

    private fun startLoading()
    {
        binding.btnSignUp.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnSignUp.isEnabled = true
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

        viewModel.isRegister.observe(this, Observer {
            SessionManager.setUserIdForRegisterMobileVerificationMode(it.data?.id!!)

            startActivity(Intent(this, MobileVerificationActivity::class.java))
            finish()
        })
    }

    private fun pickImageFromGallery() {
        FilePickerBuilder.instance
            .setMaxCount(1)
            .enableCameraSupport(true)
            .setActivityTheme(R.style.AppTheme)
            .pickPhoto(this, PICK_PROFILE_IMAGE_REQ)
    }

    private fun pickCvFromGallery()
    {
        FilePickerBuilder.instance
            .setMaxCount(1)
            .setActivityTheme(R.style.AppTheme)
            .pickFile(this, PICK_CV_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == PICK_PROFILE_IMAGE_REQ && resultCode == RESULT_OK)
        {
            data?.let {
                val arr = it.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
                if (arr != null && arr.size > 0)
                {
                    Log.d("saif", "onActivityResult: Image arr= $arr")
                    binding.ivProfile.setImageURI(Uri.parse(arr[0]))
                    viewModel.doctor.value?.photo = arr[0]
                }
            }
        }
        else if (requestCode == PICK_CV_REQ && resultCode == RESULT_OK)
        {
            data?.let {
                val arr = it.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)
                if (arr != null && arr.size > 0)
                {
                    Log.d("saif", "onActivityResult: File arr= $arr")
                    binding.etCv.setText(Uri.parse(arr[0]).lastPathSegment)
                    viewModel.doctor.value?.cv = arr[0]
                }
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            pickImageFromGallery()
        }
        else if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE_CV_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            pickCvFromGallery()
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun goToSignUpTwo()
    {
        binding.btnSignUp.apply {
            text = getString(R.string.sign_up)
            setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
            compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.drawable_padding)
            setPaddingRelative(paddingStart, paddingTop
                , resources.getDimensionPixelOffset(R.dimen.padding_end_start_button), paddingBottom)
        }
        binding.viewGroupPersonalInfo.animateSlideOutInWithOrder(binding.viewGroupData)
        binding.scrollView.smoothScrollTo(0, 0)
    }

    private fun goToSignUpOne()
    {
        binding.btnSignUp.apply {
            text = getString(R.string.next)
            setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_back_white,0)
            compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.drawable_padding_8)
            setPaddingRelative(paddingStart, paddingTop
                , resources.getDimensionPixelOffset(R.dimen.padding_end_start_button_icon), paddingBottom)
        }
        binding.viewGroupData.animateSlideOutInWithOrderReverse(binding.viewGroupPersonalInfo)
    }

    override fun onBackPressed()
    {
        if (binding.viewGroupData.visibility == VISIBLE)
            goToSignUpOne()
        else
            super.onBackPressed()
    }


    inner class DoctorSignUpClickHandler
    {
        fun onSignUp()
        {
            val doctor = viewModel.doctor.value!!

            if (binding.viewGroupPersonalInfo.visibility == VISIBLE)
            {
                val isName = doctor.name.validateUsername(binding.etlName)
                val isPhone = doctor.phone.validatePhone(binding.etlPhone)
                val isPassword = doctor.password.validatePasswordAndConfirmPassword(binding.etlPassword
                    , binding.etConfirmPassword.text.toString(), binding.etlConfirmPassword)

                if (isName && isPhone && isPassword)
                    goToSignUpTwo()
            }
            else
            {
//                val isCv = doctor.cv.validateFilePath(binding.etlCv, getString(R.string.pick_your_cv))
                val isSpecialization = doctor.specializationId.validateId(binding.splSpecialization, getString(R.string.select_your_specialization))
                val isDegree = doctor.degreeId.validateId(binding.splDegree, getString(R.string.select_your_degree))
                val isAcceptTerms = binding.cbTerms.validateChecked()

                if (isDegree && isSpecialization && isAcceptTerms)
                {
                    doctor.gender = if (binding.genderGroup.rgGender.checkedRadioButtonId == R.id.tv_female)
                            Gender.FEMALE.value else Gender.MALE.value

                    Log.d("saif", "onSignUp: doctor= "+doctor.toString())
                    viewModel.registerDoctor(doctor)
                }
            }
        }

        fun onProfileImage()
        {
            requestReadExternalPermission({pickImageFromGallery()}, PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE)
        }

        fun onCv()
        {
            requestReadExternalPermission({pickCvFromGallery()}, PERMISSION_READ_EXTERNAL_STORAGE_CV_CODE)
        }

        fun onInsurance()
        {
            val insurance = SessionManager.getInsurance()
            val list = Array(insurance.size, { i ->  insurance.get(i).name})
            val selectedList = SparseIntArray()
            val itemSelected = getSelectedInsuranceItems(viewModel.doctor.value?.insuranceIds
                , selectedList, list.size, insurance)

            alertDialogMultiChoice(title= getString(R.string.select_your_insurance_company), list = list, itemsSelected = itemSelected
                , choiceListener = { dialog, which, isChecked ->
                    if (isChecked)
                        selectedList.put(which, insurance.get(which).id.toInt())
                    else
                        selectedList.delete(which)
                }
                , positiveText = getString(R.string.ok)
                , positiveClickListener = { dialog, which ->
                    if (selectedList.size() > 0)
                    {
                        val ids = StringBuilder("[")
                        val names = StringBuilder()
                        selectedList.keyIterator().forEach {
                            ids.append(selectedList.get(it)).append(",")
                            names.append(insurance.get(it).name).append(" - ")
                        }
                        ids.deleteCharAt(ids.length-1)
                        ids.append("]")
                        names.delete(names.length-2, names.length-1)
                        viewModel.doctor.value?.insuranceIds = ids.toString()

                        binding.etInsurance.setText(names.toString())
                    }
                    else
                    {
                        viewModel.doctor.value?.insuranceIds = ""
                        binding.etInsurance.setText("")
                    }
                }
            )
        }

        fun onTerms()
        {
            // todo make Terms;
        }

        fun onBack()
        {
            if (binding.viewGroupData.visibility == VISIBLE)
                goToSignUpOne()
            else
                finishAndBack()
        }
    }

}
