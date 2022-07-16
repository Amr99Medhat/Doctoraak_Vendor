package com.doctoraak.doctoraakdoctor.ui.labHome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.doctoraak.doctoraakdoctor.Adapter.LabOrderAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.customView.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakdoctor.databinding.ActivityLabHomeBinding
import com.doctoraak.doctoraakdoctor.databinding.NavHeaderBinding
import com.doctoraak.doctoraakdoctor.model.Lab
import com.doctoraak.doctoraakdoctor.model.LabOrder
import com.doctoraak.doctoraakdoctor.model.WorkingHour
import com.doctoraak.doctoraakdoctor.ui.*
import com.doctoraak.doctoraakdoctor.ui.labOrder.LabOrderActivity
import com.doctoraak.doctoraakdoctor.ui.labOrder.LabOrderFragment
import com.doctoraak.doctoraakdoctor.ui.notification.NotificationActivity
import com.doctoraak.doctoraakdoctor.ui.profileLab.LabProfileActivity
import com.doctoraak.doctoraakdoctor.utils.*

class LabHomeActivity : BaseHome()
{
    companion object{
        internal const val RESULT_ITEM_CLICK = 5
        internal const val RESULT_ITEM_CANCELED = "RESULT_ITEM_CANCELED"
    }

    private lateinit var binding: ActivityLabHomeBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(LabHomeViewModel::class.java) }

    private lateinit var adapter: LabOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lab_home)
        setToolBarNav(binding.labMainContent.toolBar.toolBar, binding.drawer)

        binding.lifecycleOwner = this
        sendFCMToken()

        observeData()

        initViews()

        if (savedInstanceState == null)
            getLabOrders()
    }

    private fun getLabOrders()
    {
        viewModel.getLabOrders(SessionManager.getApiToken(), SessionManager.getUserId())
    }

    private fun initViews()
    {
        adapter = LabOrderAdapter(
            this,
            null,
            { position, order -> onItemClick(position, order) })
        binding.labMainContent.recyclerView.layoutManager =  LinearLayoutManager(this)
        binding.labMainContent.recyclerView.adapter = adapter

        val user = SessionManager.getLabData()
        binding.labMainContent.tvName.text = user.name

        setUpNavigationView(user)

        handleRefreshWithMotionLayouts(binding.labMainContent.refreshLayout
            , binding.labMainContent.motionLayout)

        binding.labMainContent.refreshLayout.setOnRefreshListener {
            binding.labMainContent.refreshLayout.isRefreshing = false
            getLabOrders()
        }
    }

    private fun setUpNavigationView(user: Lab)
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
            val image = ResourcesCompat.getDrawable(resources, R.drawable.ic_lab_back, null)
            Glide.with(this@LabHomeActivity).load(user.photo).transform(RoundedCorners(18))
                .error(image).placeholder(image).into(it.ivProfile)
            it.ivProfile.setOnClickListener {
                startActivity(Intent(this, LabProfileActivity::class.java))
            }
        }

        binding.navView.setNavigationItemSelectedListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            when(it.itemId)
        {
            R.id.nav_profile ->
            {
                startActivity(Intent(this, LabProfileActivity::class.java))
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
                startActivity(Intent(this@LabHomeActivity, NotificationActivity::class.java))
                true
            }
            R.id.nav_analysis ->
            {
                startActivity(Intent(this@LabHomeActivity, AnalyticActivity::class.java))
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
        with(binding.labMainContent.loading.loadingView)
        {
            show()
            alpha = 1f
            playAnimation()
        }
    }

    private fun stopLoading()
    {
        with(binding.labMainContent.loading.loadingView)
        {
            hide()
            alpha = 0f
            pauseAnimation()
        }
    }

    private fun emptyData()
    {
        binding.labMainContent.empty.emptyView.show()
        binding.labMainContent.recyclerView.hide()
    }

    private fun showData()
    {
        binding.labMainContent.recyclerView.show()
        binding.labMainContent.empty.emptyView.hide()
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

        viewModel.labOrders.observe(this, Observer {
            adapter.list = it.data

            if (it.data.isNullOrEmpty()) emptyData() else showData()

            it.data?.let {
                binding.labMainContent.tvReservationCount.text = it.size.toString() +" "+getString(R.string.reservations)
            }
        })
    }

    private fun onItemClick(position: Int, it: LabOrder)
    {
        if (resources.getBoolean(R.bool.is_tab))
        {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.fragment_container, LabOrderFragment.newInstance(it, position, { onItemCanceled(it) }, false))
                .addToBackStack(null)
                .commit()
        }
        else
            LabOrderActivity.launch(this, it, position)
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
            binding.labMainContent.tvReservationCount.text = "${adapter.itemCount} ${getString(R.string.reservations)}"
        }
    }



}
