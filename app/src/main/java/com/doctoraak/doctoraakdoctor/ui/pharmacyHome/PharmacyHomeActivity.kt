package com.doctoraak.doctoraakdoctor.ui.pharmacyHome

import android.content.Intent
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.doctoraak.doctoraakdoctor.Adapter.PharmacyOrderAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.customView.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakdoctor.databinding.ActivityPharmacyHomeBinding
import com.doctoraak.doctoraakdoctor.databinding.NavHeaderBinding
import com.doctoraak.doctoraakdoctor.model.Pharmacy
import com.doctoraak.doctoraakdoctor.model.PharmacyOrder
import com.doctoraak.doctoraakdoctor.model.WorkingHour
import com.doctoraak.doctoraakdoctor.ui.*
import com.doctoraak.doctoraakdoctor.ui.pharmacyOrder.PharmacyPrescriptionActivity
import com.doctoraak.doctoraakdoctor.ui.pharmacyOrder.PharmacyPrescriptionFragment
import com.doctoraak.doctoraakdoctor.ui.notification.NotificationActivity
import com.doctoraak.doctoraakdoctor.ui.profilePharmacy.PharmacyProfileActivity
import com.doctoraak.doctoraakdoctor.utils.hide
import com.doctoraak.doctoraakdoctor.utils.imageUrl
import com.doctoraak.doctoraakdoctor.utils.show
import com.doctoraak.doctoraakdoctor.utils.showError

class PharmacyHomeActivity : BaseHome()
{
    private lateinit var binding: ActivityPharmacyHomeBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(PharmacyHomeViewModel::class.java) }
    private lateinit var adapter: PharmacyOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pharmacy_home)
        setToolBarNav(binding.pharmacyMainContent.toolBar.toolBar, binding.drawer)

        sendFCMToken()

        observeData()

        initViews()

        if (savedInstanceState == null)
            getPharmacyOrders()
    }

    private fun getPharmacyOrders()
    {
        viewModel.getPharmacyOrders(SessionManager.getApiToken(), SessionManager.getUserId())
    }

    private fun initViews()
    {
        adapter = PharmacyOrderAdapter(
            this,
            null,
            { onItemClick(it) })
        binding.pharmacyMainContent.recyclerView.layoutManager =  LinearLayoutManager(this)
        binding.pharmacyMainContent.recyclerView.adapter = adapter

        val user = SessionManager.getPharmacyData()
        binding.pharmacyMainContent.tvName.text = user.name

        setUpNavigationView(user)

        handleRefreshWithMotionLayouts(binding.pharmacyMainContent.refreshLayout
            , binding.pharmacyMainContent.motionLayout)

        binding.pharmacyMainContent.refreshLayout.setOnRefreshListener {
            binding.pharmacyMainContent.refreshLayout.isRefreshing = false
            getPharmacyOrders()
        }
    }

    private fun setUpNavigationView(user: Pharmacy)
    {
        val headView = binding.navView.getHeaderView(0)
        DataBindingUtil.bind<NavHeaderBinding>(headView)?.let {
            it.tvName.text = user.name
            it.tvSubTitle.text = user.phone

            val image = ResourcesCompat.getDrawable(resources, R.drawable.ic_pharmacy_back, null)
            Glide.with(this@PharmacyHomeActivity).load(user.photo).transform(RoundedCorners(18))
                .error(image).placeholder(image).into(it.ivProfile)
            it.ivProfile.setOnClickListener {
                startActivity(Intent(this, PharmacyProfileActivity::class.java))
            }
        }

        binding.navView.setNavigationItemSelectedListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            when(it.itemId)
        {
            R.id.nav_profile ->
            {
                startActivity(Intent(this, PharmacyProfileActivity::class.java))
                true
            }
            R.id.nav_calendar->
            {
                WorkingHoursDialog.newInstance(this, SessionManager.getPharmacyData().workingHours, false, object : onWorkingHoursListener {
                    override fun onWorkingHoursSelected(workingHours: List<WorkingHour>) {} }).show()
                true
            }
            R.id.nav_notification->
            {
                startActivity(Intent(this@PharmacyHomeActivity, NotificationActivity::class.java))
                true
            }
            R.id.nav_analysis ->
            {
                startActivity(Intent(this@PharmacyHomeActivity, AnalyticActivity::class.java))
                true
            }
            R.id.nav_contact_us->
            {
                contactUs()
                true
            }
            R.id.nav_language->
            {
                changeLanguage()
                true
            }
            R.id.nav_log_out->
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
        with(binding.pharmacyMainContent.loading.loadingView)
        {
            show()
            alpha = 1f
            playAnimation()
        }
    }

    private fun stopLoading()
    {
        with(binding.pharmacyMainContent.loading.loadingView)
        {
            hide()
            alpha = 0f
            pauseAnimation()
        }
    }

    private fun emptyData()
    {
        binding.pharmacyMainContent.empty.emptyView.show()
        binding.pharmacyMainContent.recyclerView.hide()
    }

    private fun showData()
    {
        binding.pharmacyMainContent.recyclerView.show()
        binding.pharmacyMainContent.empty.emptyView.hide()
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

        viewModel.pharmacyOrders.observe(this, Observer {
            adapter.list = it.data
            if (it.data.isNullOrEmpty()) emptyData() else showData()

            it.data?.let {
                binding.pharmacyMainContent.tvReservationCount.text = it.size.toString() +" "+getString(R.string.reservations)
            }
        })
    }

    private fun onItemClick(it: PharmacyOrder)
    {
        if (resources.getBoolean(R.bool.is_tab))
        {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.fragment_container, PharmacyPrescriptionFragment.newInstance(it, showToolBar = false))
                .addToBackStack(null)
                .commit()
        }
        else
            PharmacyPrescriptionActivity.launch(this, it)
    }





}
