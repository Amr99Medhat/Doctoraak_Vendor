package com.doctoraak.doctoraakdoctor.ui.radiologyHome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.doctoraak.doctoraakdoctor.Adapter.RadiologyOrderAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.customView.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakdoctor.databinding.ActivityRadiologyHomeBinding
import com.doctoraak.doctoraakdoctor.databinding.NavHeaderBinding
import com.doctoraak.doctoraakdoctor.model.Radiology
import com.doctoraak.doctoraakdoctor.model.RadiologyOrder
import com.doctoraak.doctoraakdoctor.model.WorkingHour
import com.doctoraak.doctoraakdoctor.ui.*
import com.doctoraak.doctoraakdoctor.ui.notification.NotificationActivity
import com.doctoraak.doctoraakdoctor.ui.profileRadiology.RadiologyProfileActivity
import com.doctoraak.doctoraakdoctor.ui.radiologyOrder.RadiologyOrderActivity
import com.doctoraak.doctoraakdoctor.ui.radiologyOrder.RadiologyOrderFragment
import com.doctoraak.doctoraakdoctor.utils.*

class RadiologyHomeActivity : BaseHome()
{
    companion object{
        internal const val RESULT_ITEM_CLICK = 5
        internal const val RESULT_ITEM_CANCELED = "RESULT_ITEM_CANCELED"
    }

    private lateinit var binding: ActivityRadiologyHomeBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(RadiologyHomeViewModel::class.java) }

    private lateinit var adapter: RadiologyOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_radiology_home)
        setToolBarNav(binding.radiologyMainContent.toolBar.toolBar, binding.drawer)

        binding.lifecycleOwner = this

        sendFCMToken()

        observeData()

        initViews()

        if (savedInstanceState == null)
            getLabOrders()
    }

    private fun getLabOrders()
    {
        viewModel.getRadiologyOrders(SessionManager.getApiToken(), SessionManager.getUserId())
    }

    private fun initViews()
    {
        adapter = RadiologyOrderAdapter(
            this,
            null,
            { position, order -> onItemClick(position, order) })
        binding.radiologyMainContent.recyclerView.layoutManager =  LinearLayoutManager(this)
        binding.radiologyMainContent.recyclerView.adapter = adapter

        val user = SessionManager.getRadiologyData()
        binding.radiologyMainContent.tvName.text = user.name

        setUpNavigationView(user)

        handleRefreshWithMotionLayouts(binding.radiologyMainContent.refreshLayout
            , binding.radiologyMainContent.motionLayout)

        binding.radiologyMainContent.refreshLayout.setOnRefreshListener {
            binding.radiologyMainContent.refreshLayout.isRefreshing = false
            getLabOrders()
        }
    }

    private fun setUpNavigationView(user: Radiology)
    {
        val headView = binding.navView.getHeaderView(0)
        DataBindingUtil.bind<NavHeaderBinding>(headView)?.let {
            it.tvName.text = user.name
            it.tvSubTitle.text = SessionManager.getCity().find { user.city_id == it.id }?.run {
                if (SessionManager.getLanguage() == Language.ENGLISH.name)
                    name
                else
                    name_ar
            }
            val image = ResourcesCompat.getDrawable(resources, R.drawable.ic_radiology_back, null)
            Glide.with(this@RadiologyHomeActivity).load(user.photo).transform(RoundedCorners(18))
                .error(image).placeholder(image).into(it.ivProfile)
            it.ivProfile.setOnClickListener {
                startActivity(Intent(this, RadiologyProfileActivity::class.java))
            }
        }

        binding.navView.setNavigationItemSelectedListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            when(it.itemId)
        {
            R.id.nav_profile ->
            {
                startActivity(Intent(this, RadiologyProfileActivity::class.java))
                true
            }
            R.id.nav_calendar->
            {
                WorkingHoursDialog.newInstance(this, SessionManager.getPharmacyData().workingHours, false, object :
                    onWorkingHoursListener {
                    override fun onWorkingHoursSelected(workingHours: List<WorkingHour>) {} }).show()
                true
            }
            R.id.nav_notification->
            {
                startActivity(Intent(this@RadiologyHomeActivity, NotificationActivity::class.java))
                true
            }
            R.id.nav_analysis ->
            {
                startActivity(Intent(this@RadiologyHomeActivity, AnalyticActivity::class.java))
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
        with(binding.radiologyMainContent.loading.loadingView)
        {
            show()
            alpha = 1f
            playAnimation()
        }
    }

    private fun stopLoading()
    {
        with(binding.radiologyMainContent.loading.loadingView)
        {
            hide()
            alpha = 0f
            pauseAnimation()
        }
    }

    private fun emptyData()
    {
        binding.radiologyMainContent.empty.emptyView.show()
        binding.radiologyMainContent.recyclerView.hide()
    }

    private fun showData()
    {
        binding.radiologyMainContent.recyclerView.show()
        binding.radiologyMainContent.empty.emptyView.hide()
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

        viewModel.radiologyOrders.observe(this, Observer {
            adapter.list = it.data
            if (it.data.isNullOrEmpty()) emptyData() else showData()

            it.data?.let {
                binding.radiologyMainContent.tvReservationCount.text = it.size.toString() +" "+getString(R.string.reservations)
            }
        })
    }

    private fun onItemClick(position: Int, it: RadiologyOrder)
    {
        if (resources.getBoolean(R.bool.is_tab))
        {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.fragment_container, RadiologyOrderFragment.newInstance(it, position, {onItemCanceled(it)}, false))
                .addToBackStack(null)
                .commit()
        }
        else
            RadiologyOrderActivity.launch(this, it, position)
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
            binding.radiologyMainContent.tvReservationCount.text = "${adapter.itemCount} ${getString(R.string.reservations)}"
        }
    }




}
