package com.doctoraak.doctoraakdoctor.ui.updateClinic

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
import com.doctoraak.doctoraakdoctor.databinding.ActivityUpdateClinicBinding
import com.doctoraak.doctoraakdoctor.model.Clinic
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.PlacePickerDialog
import com.doctoraak.doctoraakdoctor.utils.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.tiper.MaterialSpinner
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst

class UpdateClinicActivity : BaseActivity()
{
    companion object {
        @JvmStatic
        fun launch(context: Context, clinic: Clinic)
        {
            context.startActivity(
                Intent(context, UpdateClinicActivity::class.java)
                    .apply { putExtra(INTENT_CLINIC_DETAILS, Gson().toJson(clinic)) })
        }

        private const val INTENT_CLINIC_DETAILS = "INTENT_CLINIC_DETAILS"
        private val PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE = 45
        private val PICK_PROFILE_IMAGE_REQ: Int = 6
    }

    private lateinit var binding: ActivityUpdateClinicBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(UpdateClinicViewModel::class.java) }

    private val areaAdapter by lazy { SpinnerAdapter(this@UpdateClinicActivity) }
    private val areas by lazy { SessionManager.getArea() }
    private var isAreaSet = false


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_clinic)
        binding.lifecycleOwner = this
        binding.clickHandler = ClinicUpdateClickHandler()
        binding.viewModel = viewModel

        intent.getStringExtra(INTENT_CLINIC_DETAILS)?.let {
            viewModel.clinic.value = Gson().fromJson(it, Clinic::class.java)
        }

        observeData()

        initViews()
    }

    private fun initViews()
    {
        val city = SessionManager.getCity()

        binding.splCity.adapter =
            SpinnerAdapter(this@UpdateClinicActivity, city)
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

        city.forEachIndexed { index, city_ ->
            if (viewModel.clinic.value?.city_id == city_.id)
            {
                binding.splCity.selection = index
                return@forEachIndexed
            }
        }

        isAreaSet = true

        areaAdapter.list.forEachIndexed { index, area_ ->
            if (viewModel.clinic.value?.area_id == area_.id)
            {
                binding.splArea.selection = index
                return@forEachIndexed
            }
        }

    }

    private fun startLoading()
    {
        binding.btnUpdateClinic.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnUpdateClinic.isEnabled = true
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

        viewModel.isUpdateClinic.observe(this, Observer {
            Utils.showSuccess(this, getString(R.string.success))
        })
    }

    private fun setAreasForCity(cityId: Int)
    {
        val newAreas = areas.filter { it.city_id == cityId }

        areaAdapter.clear()
        areaAdapter.list = newAreas
        if (isAreaSet)
        {
            binding.splArea.selection = -1
            viewModel.clinic.value?.area_id = -1
        }

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


    inner class ClinicUpdateClickHandler
    {
        fun onUpdateClinic()
        {
            viewModel.clinic.value?.let {
//                it.fees = binding.etFees.text.toString().toFloat()
//                it.fees2 = binding.etFees2.text.toString().toFloat()

//                val isFees = it.fees.toString().validateEmptyAndNotZeroFloat(binding.etlFees)
//                val isFees2 = it.fees.toString().validateEmptyAndNotZeroFloat(binding.etlFees2)
                val isCity = it.city_id.validateId(binding.splCity, getString(R.string.city_is_required))
                val isArea = it.area_id.validateId(binding.splArea, getString(R.string.area_is_required))
                val isAddress = it.lang.validateEmpty(it.latt, binding.etlAddress, getString(R.string.pick_clinic_address))
                val isPhone = it.phone.validatePhone(binding.etlPhone)
                val isWaitingTime = it.waitingTime.validateEmptyAndNotZero(binding.etlWaitingTime)

                if (isCity && isArea && isPhone && isAddress && isWaitingTime )
                {
                    Log.d("saif", "onUpdateProfile: radiology= " + it.toString())

                    it.doctorId = SessionManager.getUserId()
                    showWarning(this@UpdateClinicActivity, getString(R.string.are_you_sure_you_want_to_update_clinic_)
                        , okClick = {
                            viewModel.updateClinic(SessionManager.getApiToken(), it) })
                }
            }
        }

        fun turnClinicOffClick()
        {
            showWarning(this@UpdateClinicActivity,
                getString(R.string.this_clinic_will_be_unavailable_until_tomorrow)
                ,
                okClick = {
                    viewModel.changeClinicAvailability(
                        SessionManager.getApiToken()
                        , viewModel.clinic.value?.id!!, SessionManager.getUserId()
                    )
                })
        }

        fun onProfileImage()
        {
            requestReadExternalPermission({pickImageFromGallery()}, PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE)
        }

        fun onAddressPicker() = PlacePickerDialog.newInstance().apply {
            show(supportFragmentManager, PlacePickerDialog.TAG)
            listener = object : PlacePickerDialog.onPlacePickerListener {
                override fun onLocationSelected(address: String, position: LatLng)
                {
                    onPlacePicker(address, position)
                }
            }
        }

        fun onBack() = onBackPressed()

    }


}
