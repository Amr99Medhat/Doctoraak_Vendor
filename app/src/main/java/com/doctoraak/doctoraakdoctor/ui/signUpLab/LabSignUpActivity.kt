package com.doctoraak.doctoraakdoctor.ui.signUpLab

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.util.SparseIntArray
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.util.keyIterator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakdoctor.Adapter.SpinnerAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityLabSignUpBinding
import com.doctoraak.doctoraakdoctor.model.Area
import com.doctoraak.doctoraakdoctor.model.WorkingHour
import com.doctoraak.doctoraakdoctor.services.SmsBroadcastReceiver
import com.doctoraak.doctoraakdoctor.services.SmsListener
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.PlacePickerDialog
import com.doctoraak.doctoraakdoctor.ui.WorkingHoursDialog
import com.doctoraak.doctoraakdoctor.ui.mobileVerification.MobileVerificationActivity
import com.doctoraak.doctoraakdoctor.ui.onWorkingHoursListener
import com.doctoraak.doctoraakdoctor.ui.payment.PaymentActivity
import com.doctoraak.doctoraakdoctor.utils.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.tiper.MaterialSpinner
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import java.lang.StringBuilder
import kotlin.math.min

class LabSignUpActivity : BaseActivity()
{
    private val PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE = 45
    private val PICK_PROFILE_IMAGE_REQ: Int = 6
    private val REQ_CODE_READ_SMS: Int = 5

    private lateinit var binding: ActivityLabSignUpBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LabSignUpViewModel::class.java)
    }

    private val areaAdapter by lazy { SpinnerAdapter(this@LabSignUpActivity) }
    private val areas by lazy { SessionManager.getArea() }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lab_sign_up)

        val clickHandler = LabSignUpClickHandler()
        binding.clickHandler = clickHandler
        binding.viewModel = viewModel

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

    private fun initViews()
    {
        val city = SessionManager.getCity()
        Log.d("saif", "city= ${Gson().toJson(city)}")
        Log.d("saif", "areas= ${Gson().toJson(areas)}")

        binding.splCity.adapter =
            SpinnerAdapter(this@LabSignUpActivity, city)
        binding.splCity.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)
            {
                with (city[position].id)
                {
                    viewModel.lab.value?.city_id = this

                    setAreasForCity(this)
                }
            }
            override fun onNothingSelected(parent: MaterialSpinner) {}
        }

        binding.splArea.adapter = areaAdapter
        binding.splArea.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)
            {
                areaAdapter.list.getOrNull(position)?.id?.let {
                    viewModel.lab.value?.area_id = it
                }?: run {
                    viewModel.lab.value?.area_id = -1
                }

                Log.d("saif", "area_id= ${viewModel.lab.value?.area}")
            }
            override fun onNothingSelected(parent: MaterialSpinner) {}
        }
    }

    private fun startLoading() {
        binding.btnSignUp.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading() {
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

    private fun setAreasForCity(cityId: Int)
    {
        val newAreas = areas.filter { it.city_id == cityId }

        areaAdapter.clear()
        areaAdapter.list = newAreas
        binding.splArea.selection = -1
        viewModel.lab.value?.area_id = -1

        if (!newAreas.isNullOrEmpty())
            newAreas.forEach { areaAdapter.add(it.toString()) }
        else
            areaAdapter.add(getString(R.string.no_available_areas_for_that_city))
    }

    private fun pickImageFromGallery() {
        FilePickerBuilder.instance
            .setMaxCount(1)
            .enableCameraSupport(true)
            .setActivityTheme(R.style.AppTheme)
            .pickPhoto(this, PICK_PROFILE_IMAGE_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_PROFILE_IMAGE_REQ && resultCode == AppCompatActivity.RESULT_OK) {
            data?.let {
                val arr = it.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
                if (arr != null && arr.size > 0)
                {
                    Log.d("saif", "onActivityResult: Image arr= $arr")
                    binding.ivProfile.setImageURI(Uri.parse(arr[0]))
                    viewModel.lab.value?.photo = arr[0]
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            pickImageFromGallery()
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onPlacePicker(address: String, position: LatLng)
    {
        binding.etAddress.setText(address)

        viewModel.lab.value?.let {
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


    inner class LabSignUpClickHandler
    {
        fun onSignUp()
        {
            val lab = viewModel.lab.value!!

            lab.delivery = if (binding.swDelivery.isChecked) 1 else 0
            val isName = lab.name.validateUsername(binding.etlName)
            val isPhone = lab.phone.validatePhone(binding.etlPhone)
            val isCity = lab.city_id.validateId(binding.splCity, getString(R.string.city_is_required))
            val isArea = lab.area_id.validateId(binding.splArea, getString(R.string.area_is_required))
            val isAddress = lab.lang.validateEmpty(lab.latt, binding.etlAddress, getString(R.string.pick_clinic_address))
            val isPassword = lab.password.validatePasswordAndConfirmPassword(binding.etlPassword
                , binding.etConfirmPassword.text.toString(), binding.etlConfirmPassword)
            val isWorkingHours = if (lab.workingHours.isEmpty()) {
                binding.etlWorkingHours.error = getString(R.string.select_your_working_hours)
                false
            } else true


            if (isName && isPhone && isPassword && isWorkingHours && isCity && isArea && isAddress) {
                Log.d("saif", "onSignUp: lab= " + lab.toString())
                viewModel.registerLab(lab)
            }
        }

        fun onProfileImage() {
            requestReadExternalPermission(
                { pickImageFromGallery() },
                PERMISSION_READ_EXTERNAL_STORAGE_PROFILE_CODE
            )
        }

        fun onWorkingHours() = WorkingHoursDialog.newInstance(
            this@LabSignUpActivity, workingHoursList = viewModel.lab.value?.workingHours
            , listener = object : onWorkingHoursListener {
                override fun onWorkingHoursSelected(workingHours: List<WorkingHour>) {
                    viewModel.lab.value?.let {
                        Log.d("saif", "ClinicActivity: workingHours= $workingHours")
                        it.workingHours = workingHours
                        binding.etWorkingHours.setText(Utils
                            .getWorkingDays(this@LabSignUpActivity, workingHours))
                    }
                }
            }).show()

        fun onAddressClick() = PlacePickerDialog.newInstance().apply {
            show(
                supportFragmentManager,
                PlacePickerDialog.TAG
            )
            listener = object : PlacePickerDialog.onPlacePickerListener {
                override fun onLocationSelected(address: String, position: LatLng) {
                    onPlacePicker(address, position)
                }
            }
        }

        fun onInsurance()
        {
            val insurance = SessionManager.getInsurance()
            val list = Array(insurance.size, { i -> insurance.get(i).name })
            val selectedList = SparseIntArray()
            val itemSelected = getSelectedInsuranceItems(viewModel.lab.value?.insuranceIds
                , selectedList, list.size, insurance)

            alertDialogMultiChoice(title = getString(R.string.select_your_insurance_company), list = list, itemsSelected = itemSelected
                , choiceListener = { dialog, which, isChecked ->
                    if (isChecked)
                        selectedList.put(which, insurance.get(which).id.toInt())
                    else
                        selectedList.delete(which)
                }
                , positiveText = getString(R.string.ok)
                , positiveClickListener = { dialog, which ->
                    if (selectedList.size() > 0) {
                        val ids = StringBuilder("[")
                        val names = StringBuilder()
                        selectedList.keyIterator().forEach {
                            ids.append(selectedList.get(it)).append(",")
                            names.append(insurance.get(it).name).append(" - ")
                        }
                        ids.deleteCharAt(ids.length - 1)
                        ids.append("]")
                        names.delete(names.length - 2, names.length - 1)
                        viewModel.lab.value?.insuranceIds = ids.toString()

                        binding.etInsurance.setText(names.toString())
                    }
                    else
                    {
                        viewModel.lab.value?.insuranceIds = ""
                        binding.etInsurance.setText("")
                    }
                }
            )
        }

        fun onTerms() {
            // todo make Terms;
        }

        fun onBack() {
            finishAndBack()
        }
    }

}
