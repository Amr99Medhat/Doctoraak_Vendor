package com.doctoraak.doctoraakdoctor.ui.doctorHome

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.doctoraak.doctoraakdoctor.Adapter.ClinicReservationAdapter
import com.doctoraak.doctoraakdoctor.Adapter.ClinicSpinnerAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityDoctorHomeBinding
import com.doctoraak.doctoraakdoctor.databinding.NavHeaderBinding
import com.doctoraak.doctoraakdoctor.model.Doctor
import com.doctoraak.doctoraakdoctor.model.Reservation
import com.doctoraak.doctoraakdoctor.model.ShiftWorkingHour
import com.doctoraak.doctoraakdoctor.ui.*
import com.doctoraak.doctoraakdoctor.ui.doctorReservation.DoctorReservationActivity
import com.doctoraak.doctoraakdoctor.ui.doctorReservation.DoctorReservationFragment
import com.doctoraak.doctoraakdoctor.ui.notification.NotificationActivity
import com.doctoraak.doctoraakdoctor.ui.profileDoctor.DoctorProfileActivity
import com.doctoraak.doctoraakdoctor.ui.signUpClinic.ClinicSignUpActivity
import com.doctoraak.doctoraakdoctor.ui.updateClinic.UpdateClinicActivity
import com.doctoraak.doctoraakdoctor.utils.*
import java.util.*
import kotlin.collections.ArrayList

class DoctorHomeActivity : BaseHome()
{
    companion object{
        internal const val RESULT_ITEM_CLICK = 5
        internal const val RESULT_ITEM_CANCELED = "RESULT_ITEM_CANCELED"
    }

    private lateinit var binding: ActivityDoctorHomeBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(DoctorHomeViewModel::class.java) }
    private lateinit var adapter: ClinicReservationAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_home)
        setToolBarNav(binding.doctorMainContent.toolBar.toolBar, binding.drawer)

        binding.lifecycleOwner = this

        sendFCMToken()
        observeData()
        initViews()

        if (savedInstanceState == null)
            getAllClinics()
    }

    private fun getClinicReservation(clinicId: Long)
    {
        viewModel.getClinicOrders(SessionManager.getApiToken(), SessionManager.getUserId(), clinicId)
    }

    private fun getAllClinics()
    {
        if (SessionManager.getUserType() != UserType.OPTICAL_CENTER)
            viewModel.getAllClinics(SessionManager.getApiToken(), SessionManager.getUserId())
    }

    private fun initViews()
    {
        handleOpticalCenters()
        adapter = ClinicReservationAdapter(
            this,
            null,
            { position, reservation -> onItemClick(position, reservation) })
        binding.doctorMainContent.recyclerView.layoutManager =  LinearLayoutManager(this)
        binding.doctorMainContent.recyclerView.adapter = adapter

        val user = SessionManager.getDoctorData()
        binding.doctorMainContent.tvName.text = user.name

        setUpNavigationView(user)

        handleRefreshWithMotionLayouts(binding.doctorMainContent.refreshLayout
            , binding.doctorMainContent.motionLayout)

        binding.doctorMainContent.refreshLayout.setOnRefreshListener {
            binding.doctorMainContent.refreshLayout.isRefreshing = false
            getAllClinics()
            binding.doctorMainContent.doctorBubbleReservationType.rgReservationTypes.check(R.id.tv_all)
        }

        binding.doctorMainContent.spClinics.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                viewModel.allClinics.value?.data?.get(position)?.let {
                    imageUrl(binding.doctorMainContent.ivProfile, it.photo
                        , error = ResourcesCompat.getDrawable(resources, R.drawable.ic_clinic, null))
                    getClinicReservation(it.id)
                    Log.d("saif", "clinic Id = ${it.id}")
                }
            }
        }

        binding.doctorMainContent.doctorBubbleReservationType.rgReservationTypes
            .setOnCheckedChangeListener {group: RadioGroup, checkedId: Int ->
            when (checkedId)
            {
                binding.doctorMainContent.doctorBubbleReservationType.tvAll.id ->
                    viewModel.selectedReservationFilter.value = viewModel.selectedReservationFilter.value?.apply { type = DoctorReservationType.ALL }
                binding.doctorMainContent.doctorBubbleReservationType.tvConsultation.id ->
                    viewModel.selectedReservationFilter.value =  viewModel.selectedReservationFilter.value?.apply { type = DoctorReservationType.CONSULTATION }
                binding.doctorMainContent.doctorBubbleReservationType.tvContinue.id ->
                    viewModel.selectedReservationFilter.value =  viewModel.selectedReservationFilter.value?.apply { type = DoctorReservationType.CONTINUE }
                binding.doctorMainContent.doctorBubbleReservationType.tvNew.id ->
                    viewModel.selectedReservationFilter.value =  viewModel.selectedReservationFilter.value?.apply { type = DoctorReservationType.NEW }
                else -> {}
            }
        }

        binding.doctorMainContent.btnDateFilter.setOnClickListener {
            filterReservationByDate()
        }
        binding.doctorMainContent.ivReset.setOnClickListener {
            clearDateFilter()
        }
    }

    private fun clearDateFilter()
    {
        viewModel.selectedReservationFilter.value?.date?.let {
            binding.doctorMainContent.btnDateFilter.text = getString(R.string.filter_by_date)
            viewModel.selectedReservationFilter.value = viewModel.selectedReservationFilter.value?.apply { this.date = null }
        }
    }

    private fun filterReservationByDate()
    {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { datePicker, year, month, dayOfMonth ->
                val monthWith0 = if (month+1 < 10) "0${month+1}" else "${month+1}"
                val filterDate = "$year-${monthWith0}-$dayOfMonth"
                binding.doctorMainContent.btnDateFilter.text = filterDate
                viewModel.selectedReservationFilter.value = viewModel.selectedReservationFilter.value?.apply { this.date = filterDate }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
            .show()
    }

    private fun handleOpticalCenters()
    {
        if (SessionManager.getUserType() == UserType.OPTICAL_CENTER)
        {
            binding.doctorMainContent.refreshLayout.disable()
            binding.doctorMainContent.doctorBubbleReservationType.root.hide()
        }
    }

    private fun setUpNavigationView(user: Doctor)
    {
        val headView = binding.navView.getHeaderView(0)
        DataBindingUtil.bind<NavHeaderBinding>(headView)?.let {
            it.tvName.text = user.name
            SessionManager.getSpecialization().find{ user.specializationId == it.id }?.let { spec ->
                it.tvSubTitle.text = if (SessionManager.getLanguage() == Language.ENGLISH.name)
                    spec.name
                else
                    spec.name_ar
            }
            val image = ResourcesCompat.getDrawable(resources, R.drawable.ic_doctor, null)
            Glide.with(this@DoctorHomeActivity).load(user.photo).transform(RoundedCorners(18))
                .error(image).placeholder(image).into(it.ivProfile)
            it.ivProfile.setOnClickListener {
                startActivity(Intent(this, DoctorProfileActivity::class.java))
            }
        }

        binding.navView.setNavigationItemSelectedListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            when(it.itemId)
        {
            R.id.nav_profile ->
            {
                startActivity(Intent(this, DoctorProfileActivity::class.java))
                true
            }
            R.id.nav_add_clinic ->
            {
                startActivity(Intent(this@DoctorHomeActivity, ClinicSignUpActivity::class.java).apply {
                    putExtra(ClinicSignUpActivity.INTENT_CAN_RETURN, true)
                })
                true
            }
            R.id.nav_update_clinic->
            {
                viewModel.allClinics.value?.data?.getOrNull(binding.doctorMainContent.spClinics.selectedItemPosition)
                    ?.let {clinic ->
                        UpdateClinicActivity.launch(this@DoctorHomeActivity, clinic)
                    }
                true
            }
            R.id.nav_calendar ->
            {
                viewModel.allClinics.value?.data?.getOrNull(binding.doctorMainContent.spClinics.selectedItemPosition)
                    ?.let {clinic->
                        ShiftWorkingHoursDialog.newInstance( context = this@DoctorHomeActivity, workingHoursList = clinic.shiftWorkingHours, isDialogAfterChanged = true
                            , listener = object : OnShiftWorkingHoursListener {
                                override fun onWorkingHoursSelected(shiftWorkingHours: List<ShiftWorkingHour>) {
                                    viewModel.updateClinicWorkingHours(
                                        SessionManager.getApiToken(), clinic.id
                                        , SessionManager.getUserId(), shiftWorkingHours)
                                }
                            }).show()
                }
                true
            }
            R.id.nav_notification ->
            {
                startActivity(Intent(this@DoctorHomeActivity, NotificationActivity::class.java))
                true
            }
            R.id.nav_analysis ->
            {
                startActivity(Intent(this@DoctorHomeActivity, AnalyticActivity::class.java))
                true
            }
            R.id.nav_contact_us ->
            {
                contactUs()
                true
            }
            R.id.nav_language ->
            {
                changeLanguage()
                true
            }
            R.id.nav_log_out ->
            {
                logOut()
                true
            }
            else ->
                false
        }}
    }

    private fun startLoading()
    {
        with(binding.doctorMainContent.loading.loadingView)
        {
            show()
            alpha = 1f
            playAnimation()
        }
    }

    private fun stopLoading()
    {
        with(binding.doctorMainContent.loading.loadingView)
        {
            hide()
            alpha = 0f
            pauseAnimation()
        }
    }

    private fun emptyData()
    {
        binding.doctorMainContent.empty.emptyView.show()
        binding.doctorMainContent.recyclerView.hide()
    }

    private fun showData()
    {
        binding.doctorMainContent.recyclerView.show()
        binding.doctorMainContent.empty.emptyView.hide()
    }

    private fun observeData()
    {
        viewModel.isLoading.observe(this, Observer {
            Log.d("saif", "viewModel.isLoading= ${viewModel.isLoading.value}")

            if (it) startLoading() else stopLoading()
        })

        viewModel.errorInt.observe(this, Observer {
            it.showError(this)
        })

        viewModel.errorMsg.observe(this, Observer { showError(it) })

        viewModel.allClinics.observe(this, Observer {
            if (it.data != null && it.data?.size!! > 0)
            {
                binding.doctorMainContent.spClinics.adapter =
                    ClinicSpinnerAdapter(this, it.data!!)
                imageUrl(binding.doctorMainContent.ivProfile, it.data!![0].photo
                    , error = ResourcesCompat.getDrawable(resources, R.drawable.ic_clinic, null))

                showData()
                binding.doctorMainContent.spClinics.show()
                binding.doctorMainContent.tvEmptyClinics.hide()
            }
            else
            {
                binding.doctorMainContent.empty.emptyView.hide()
                binding.doctorMainContent.recyclerView.hide()
                binding.doctorMainContent.spClinics.hide()
                binding.doctorMainContent.tvEmptyClinics.show()
            }
        })

        viewModel.clinicReservationFiltered.observe(this, Observer {
            setReservationData(it)
        })

        viewModel.clinicAvailability.observe(this, Observer {
            Utils.showSuccess(this, getString(R.string.clinic_availability_is_changed_successfully))

            viewModel.allClinics.value?.data?.getOrNull(binding.doctorMainContent.spClinics.selectedItemPosition)
                ?.let { clinic->
                    getClinicReservation(clinic.id)
                }
        })

        viewModel.updateWorkingHour.observe(this, Observer {
            Utils.showSuccess(this, getString(R.string.your_hour_has_changed_successfully))
        })

    }

    private fun setReservationData(list: List<Reservation>?)
    {
        Log.d("saif", "clinicReservationFiltered Observes Activity")

        adapter.setData(list as ArrayList<Reservation>?)
//        if (list.isNullOrEmpty()) emptyData() else showData()

        list?.let {
            binding.doctorMainContent.tvReservationCount.text = it.size.toString() +" "+getString(R.string.reservations)
        }
    }

    private fun onItemClick(position: Int, it: Reservation)
    {
        if (resources.getBoolean(R.bool.is_tab))
        {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.fragment_container, DoctorReservationFragment.newInstance(position, it, {p-> onItemCanceled(p)}, showToolBar = false))
                .addToBackStack(null)
                .commit()
        }
        else
            DoctorReservationActivity.launch(this, position, it)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == RESULT_ITEM_CLICK && resultCode == Activity.RESULT_OK)
        {
            val itemPosition = data?.getIntExtra(RESULT_ITEM_CANCELED, -1)
            onItemCanceled(itemPosition!!)

            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun onItemCanceled(position: Int)
    {
        if (position != -1)
        {
            adapter.removeItem(position)
            binding.doctorMainContent.tvReservationCount.text = "${adapter.itemCount} ${getString(R.string.reservations)}"
        }
    }

}
