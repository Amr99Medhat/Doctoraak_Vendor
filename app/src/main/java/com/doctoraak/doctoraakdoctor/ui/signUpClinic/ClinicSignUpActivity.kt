package com.doctoraak.doctoraakdoctor.ui.signUpClinic

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakdoctor.Adapter.SpinnerAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityClinicSignUpBinding
import com.doctoraak.doctoraakdoctor.model.ShiftWorkingHour
import com.doctoraak.doctoraakdoctor.ui.*
import com.doctoraak.doctoraakdoctor.ui.payment.PaymentActivity
import com.doctoraak.doctoraakdoctor.utils.*
import com.google.android.gms.maps.model.LatLng
import com.tiper.MaterialSpinner
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst

class ClinicSignUpActivity : BaseActivity()
{
    private val PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE = 45
    private val PICK_PROFILE_IMAGE_REQ: Int = 6

    companion object{
        const val INTENT_CAN_RETURN: String = "INTENT_CAN_RETURN"
        const val INTENT_IS_FIRST_CLINIC = "INTENT_IS_FIRST_CLINIC"

        fun launch(context: Context, isFirstClinic: Boolean)
        {
            context.startActivity(Intent(context, ClinicSignUpActivity::class.java).apply {
                putExtra(INTENT_IS_FIRST_CLINIC, isFirstClinic)
            })
        }
    }

    private lateinit var binding: ActivityClinicSignUpBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(ClinicSignUpViewModel::class.java) }
    private val areaAdapter by lazy { SpinnerAdapter(this@ClinicSignUpActivity) }
    private val areas by lazy { SessionManager.getArea() }

    private val canReturn by lazy { intent.getBooleanExtra(INTENT_CAN_RETURN, false) }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_clinic_sign_up)

        binding.lifecycleOwner = this
        binding.clickHandler = ClinicSignUpClickHandler()
        binding.viewModel = viewModel

        observeData()

        initViews()
    }

    private fun initViews()
    {
        val city = SessionManager.getCity()

        binding.splCity.adapter =
            SpinnerAdapter(this@ClinicSignUpActivity, city)
        binding.splCity.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)
            {
                viewModel.clinic.value?.city_id = city[position].id

                setAreasForCity(city[position].id)
            }
            override fun onNothingSelected(parent: MaterialSpinner) {}
        }

        binding.splArea.adapter = areaAdapter
        binding.splArea.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)
            {
                areaAdapter.list.getOrNull(position)?.id?.let {
                    viewModel.clinic.value?.area_id = it
                }?: run {
                    viewModel.clinic.value?.area_id = -1
                }

                Log.d("saif", "area_id= ${viewModel.clinic.value?.area}")
            }
            override fun onNothingSelected(parent: MaterialSpinner) {}
        }

//        if (viewModel.clinic.value?.fees == 0f)
//            binding.etFees.setText("")
//        else
//            binding.etFees.setText(viewModel.clinic.value?.fees.toString())
//        if (viewModel.clinic.value?.fees2 == 0f)
//            binding.etNotes.setText("")
//        else
//            binding.etNotes.setText(viewModel.clinic.value?.notes)
    }

    private fun startLoading()
    {
        binding.btnCreateClinic.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnCreateClinic.isEnabled = true
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

        viewModel.isCreateClinic.observe(this, Observer {
//            binding.signUpViewGroup.hide() // todo

            it.data?.let {
                PaymentActivity.launch(this@ClinicSignUpActivity, it.id, UserType.DOCTOR)
            }
            finish()
        })
    }

    private fun setAreasForCity(cityId: Int)
    {
        val newAreas = areas.filter { it.city_id == cityId }

        areaAdapter.clear()
        areaAdapter.list = newAreas
        binding.splArea.selection = -1
        viewModel.clinic.value?.area_id = -1

        if (!newAreas.isNullOrEmpty())
            newAreas.forEach { areaAdapter.add(it.toString()) }
        else
            areaAdapter.add(getString(R.string.no_available_areas_for_that_city))
    }

    private fun pickImageFromGallery()
    {
        FilePickerBuilder.instance
            .setMaxCount(1)
            .enableCameraSupport(true)
            .setActivityTheme(R.style.AppTheme)
            .pickPhoto(this, PICK_PROFILE_IMAGE_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == PICK_PROFILE_IMAGE_REQ && resultCode == RESULT_OK)
        {
            data?.let {
                val arr = it.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
                if (arr != null && arr.size > 0)
                {
                    Log.d("saif", "arr= $arr")
                    viewModel.clinic.value?.photo = arr[0]

                    imageUrl(binding.ivProfile, arr[0])
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
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onPlacePicker(address: String, position: LatLng)
    {
        binding.etAddress.setText(address)

        viewModel.clinic.value?.let {
            it.latt = position.latitude.toString()
            it.lang = position.longitude.toString()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)

        (supportFragmentManager.findFragmentByTag(PlacePickerDialog.TAG) as PlacePickerDialog?)?.let {
            it.listener = object : PlacePickerDialog.onPlacePickerListener {
                override fun onLocationSelected(address: String, position: LatLng) {
                    onPlacePicker(address, position)
                }
            }
        }
    }

    inner class ClinicSignUpClickHandler
    {
        fun onCreateClinic()
        {
            viewModel.clinic.value?.let {
//                it.fees = binding.etFees.text.toString().toFloatOrNull() ?: 0f
//                it.fees2 = binding.etFees2.text.toString().toFloatOrNull() ?: 0f

//                val isFees = it.fees.toString().validateEmptyAndNotZeroFloat(binding.etlFees)
//                val isFees2 = it.fees2.toString().validateEmptyAndNotZeroFloat(binding.etlFee)
                val isCity = it.city_id.validateId(binding.splCity, getString(R.string.city_is_required))
                val isArea = it.area_id.validateId(binding.splArea, getString(R.string.area_is_required))
                val isAddress = it.lang.validateEmpty(it.latt, binding.etlAddress, getString(R.string.pick_clinic_address))
                val isPhone = it.phone.validatePhone(binding.etlPhone)
                val isWaitingTime = it.waitingTime.validateEmptyAndNotZero(binding.etlWaitingTime)
                val isWorkingHours = if (it.shiftWorkingHours.isEmpty()) {
                    binding.etlWorkingHours.error = getString(R.string.select_your_working_hours)
                    false
                }
                else true

                if (isCity && isArea && isPhone && isAddress && isWaitingTime && isWorkingHours)
                {
                    it.doctorId = SessionManager.getUserId()
                    viewModel.createClinic(SessionManager.getApiToken()!!, it)
                }
            }
        }

        fun onProfileImage()
        {
            requestReadExternalPermission({pickImageFromGallery()}, PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE)
        }

        fun onWorkingHours() = ShiftWorkingHoursDialog.newInstance(
            this@ClinicSignUpActivity, workingHoursList = viewModel.clinic.value?.shiftWorkingHours
            , listener = object : OnShiftWorkingHoursListener {
                override fun onWorkingHoursSelected(shiftWorkingHours: List<ShiftWorkingHour>) {
                    viewModel.clinic.value?.let {
                        Log.d("saif", "ClinicActivity: workingHours= $shiftWorkingHours")
                        it.shiftWorkingHours = shiftWorkingHours
                        binding.etWorkingHours.setText(Utils
                            .getShiftWorkingDays(this@ClinicSignUpActivity, shiftWorkingHours))
                    }
                }
            }).show()


        fun onAddressPicker() = PlacePickerDialog.newInstance().apply {
            show(supportFragmentManager, PlacePickerDialog.TAG)
            listener = object : PlacePickerDialog.onPlacePickerListener {
                override fun onLocationSelected(address: String, position: LatLng)
                {
                    onPlacePicker(address, position)
                }
            }
        }

        fun onBack()
        {
            if (canReturn)
                onBackPressed()
        }

    }

}
