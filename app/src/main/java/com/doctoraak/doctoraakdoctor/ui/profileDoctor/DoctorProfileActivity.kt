package com.doctoraak.doctoraakdoctor.ui.profileDoctor

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.util.keyIterator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakdoctor.Adapter.SpinnerAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityDoctorProfileBinding
import com.doctoraak.doctoraakdoctor.databinding.DownloadUpdateCvDialogBinding
import com.doctoraak.doctoraakdoctor.model.Doctor
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.utils.*
import com.tiper.MaterialSpinner
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import java.lang.StringBuilder

class DoctorProfileActivity : BaseActivity()
{
    private val PERMISSION_READ_EXTERNAL_STORAGE_CV_CODE = 23
    private val PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE = 45
    private val PICK_PROFILE_IMAGE_REQ: Int = 6
    private val PICK_CV_REQ: Int = 3

    private lateinit var binding: ActivityDoctorProfileBinding
    private lateinit var doctor: Doctor
    private var isPhotoChanged = false
    private var isCvChanged = false
    private val viewModel by lazy { ViewModelProviders.of(this).get(DoctorProfileViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_profile)
        setToolBar(binding.toolBarProfile.toolBar)

        binding.lifecycleOwner = this
        binding.clickHandler = DoctorProfileClickHandler()
        binding.viewModel = viewModel

        doctor = SessionManager.getDoctorData()
        viewModel.doctor.setValue(doctor)

        initViews()

        observeData()
    }

    private fun initViews()
    {
        val specialization = SessionManager.getSpecialization()
        val degree = SessionManager.getDegree()

        binding.splDegree.adapter = SpinnerAdapter(this, degree)
        binding.splSpecialization.adapter = SpinnerAdapter(this, specialization)
        
        binding.splDegree.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)
            {
                viewModel.doctor.value?.degreeId = degree[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner) {}
        }

        binding.splSpecialization.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)
            {
                viewModel.doctor.value?.specializationId = specialization[position].id
            }
            override fun onNothingSelected(parent: MaterialSpinner) {}
        }

        degree.forEachIndexed { index, degree ->
            if (degree.id == viewModel.doctor.value?.degreeId)
            {
                binding.splDegree.selection = index
                return@forEachIndexed
            }
        }

        specialization.forEachIndexed { index, specialization ->
            if (specialization.id==viewModel.doctor.value?.specializationId)
            {
                binding.splSpecialization.selection = index
                return@forEachIndexed
            }
        }

        viewModel.doctor.value?.insuranceList?.let {
            binding.etInsurance.setText(getDoctorInsuranceNames(it))
            viewModel.doctor.value?.insuranceIds = getDoctorInsuranceIds(it)
        }

        binding.etCv.setText(viewModel.doctor.value?.cv)
    }

    private fun startLoading()
    {
        binding.btnSave.isEnabled = false

        with(binding.loading.loadingView)
        {
            show()
            alpha = 1f
            playAnimation()
        }
    }

    private fun stopLoading()
    {
        binding.btnSave.isEnabled = true

        with(binding.loading.loadingView)
        {
            hide()
            alpha = 0f
            pauseAnimation()
        }
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

        viewModel.updateProfile.observe(this, Observer {
            Utils.showSuccess(this, getString(R.string.done))
            it.data?.let {
                SessionManager.logIn(UserType.DOCTOR, it)
            }
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
                    isPhotoChanged = true
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
                    isCvChanged = true
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

    inner class DoctorProfileClickHandler
    {
        fun onUpdateProfile()
        {
            Log.d("saif", "onUpdateProfile: doctor= " + viewModel.doctor.value?.toString())
            showWarning(this@DoctorProfileActivity, getString(R.string.are_you_sure_you_want_to_update_profile_), okClick = {

                viewModel.doctor.value?.let {
                    if (!isPhotoChanged)
                        it.photo = ""
                    if (!isCvChanged)
                        it.cv = ""
                    viewModel.updateDoctorProfile(it)
                }
            })
        }

        fun onProfileImage()
        {
            requestReadExternalPermission({pickImageFromGallery()}, PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE)
        }

        fun onCv()
        {
            val bindingDownloadUpdate: DownloadUpdateCvDialogBinding = DataBindingUtil.inflate(layoutInflater
                , R.layout.download_update_cv_dialog, null, false)
            bindingDownloadUpdate.tvUpdate.setOnClickListener {
                requestReadExternalPermission({pickCvFromGallery()}, PERMISSION_READ_EXTERNAL_STORAGE_CV_CODE)
            }
            bindingDownloadUpdate.tvDownload.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.doctor.value?.cv)))
            }

            AlertDialog.Builder(this@DoctorProfileActivity)
                .setView(bindingDownloadUpdate.root)
                .show()
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

    }

}
