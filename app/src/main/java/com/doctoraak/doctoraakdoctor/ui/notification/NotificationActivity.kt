package com.doctoraak.doctoraakdoctor.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakdoctor.Adapter.NotificationsAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityNotificationBinding
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.doctorReservation.DoctorReservationActivity
import com.doctoraak.doctoraakdoctor.ui.doctorReservation.DoctorReservationFragment
import com.doctoraak.doctoraakdoctor.ui.labOrder.LabOrderActivity
import com.doctoraak.doctoraakdoctor.ui.labOrder.LabOrderFragment
import com.doctoraak.doctoraakdoctor.ui.pharmacyOrder.PharmacyPrescriptionActivity
import com.doctoraak.doctoraakdoctor.ui.pharmacyOrder.PharmacyPrescriptionFragment
import com.doctoraak.doctoraakdoctor.ui.radiologyOrder.RadiologyOrderActivity
import com.doctoraak.doctoraakdoctor.ui.radiologyOrder.RadiologyOrderFragment
import com.doctoraak.doctoraakdoctor.utils.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.lang.Exception

class NotificationActivity : BaseActivity()
{
    private lateinit var binding : ActivityNotificationBinding
    private lateinit var adapter: NotificationsAdapter

    private val viewModel: NotificationViewModel by lazy { ViewModelProviders.of(this).get(NotificationViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_notification)
        with(binding.toolbar)
        {
            setToolBar(toolBar)
            viewGroupNotification.hide()
        }

        adapter = NotificationsAdapter(
            null,
            this,
            { onItemClick(it) })
        with(binding.recyclerView)
        {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = this@NotificationActivity.adapter
            itemTouch.attachToRecyclerView(this)
        }

        observeData()

        if (Utils.checkInternetConnection(this) && savedInstanceState == null)
            getNotification()

        handleRefreshWithMotionLayouts(binding.refreshLayout, binding.motionLayout)
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            getNotification()
        }
    }

    private val itemTouch = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0
        , ItemTouchHelper.START or ItemTouchHelper.END)
    {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
                            , target: RecyclerView.ViewHolder): Boolean
        {
            return true
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
        {
            viewModel.removeNotifications(adapter.removeItem(viewHolder.adapterPosition))
        }
    })

    private fun getNotification() = viewModel.getNotifications(
        SessionManager.getUserId()
        , SessionManager.getUserType()!! , SessionManager.getApiToken()!!)

    private fun observeData()
    {
        viewModel.notificationsResponse.observe(this,
            Observer<NotificationsResponse> { t ->

                if (t.data != null && t.data.size > 0)
                {
                    showData()
                    adapter.setData(t.data)
                    binding.recyclerView.smoothScrollToPosition(0)
                }
                else
                    emptyData()
            })

        viewModel.removeNotification.observe(this, Observer {
            Snackbar.make(binding.motionLayout, getString(R.string.notification_removed_successfully)
                , Snackbar.LENGTH_SHORT).show()
        })

        viewModel.isLoading.observe(this, Observer {
            if (it) startLoading() else stopLoading()
        })

        viewModel.errorInt.observe(this, Observer {
            it.showError(this)
        })

        viewModel.errorMsg.observe(this, Observer { showError(it) })

    }

    private fun startLoading()
    {
        with(binding.loading.loadingView)
        {
            show()
            alpha = 1f
            playAnimation()
        }
    }

    private fun stopLoading()
    {
        with(binding.loading.loadingView)
        {
            hide()
            alpha = 0f
            pauseAnimation()
        }
    }

    private fun emptyData()
    {
        binding.emptyView.show()
        binding.recyclerView.hide()
    }

    private fun showData()
    {
        binding.recyclerView.show()
        binding.emptyView.hide()
    }

    private fun onItemClick(position: Int)
    {
        adapter.notifications?.getOrNull(position)?.order?.let {
            Log.d("saif", "order= $it")

            try {
                val orderJson = Gson().toJson(it)
                when (SessionManager.getUserType()!!)
                {
                    UserType.PHARMACY -> pharmacyItem(Gson().fromJson(orderJson, PharmacyOrder::class.java))
                    UserType.LAB -> labItem(Gson().fromJson(orderJson, LabOrder::class.java))
                    UserType.RADIOLOGY -> radiologyItem(Gson().fromJson(orderJson, RadiologyOrder::class.java))
                    else -> doctorItem(Gson().fromJson(orderJson, Reservation::class.java))

                }
            }catch (e: Exception)
            {
                Log.d("saif", "onItemClick: ", e)
            }
        }?: Toast.makeText(this, getString(R.string.no_content), Toast.LENGTH_SHORT).show()
    }

    private fun doctorItem(order: Reservation)
    {
        if (resources.getBoolean(R.bool.is_tab))
        {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.fragment_container, DoctorReservationFragment.newInstance(DoctorReservationFragment.POSITION_NOTIFICATION_ACTION
                        , order, { }, showToolBar = false))
                .addToBackStack(null)
                .commit()
        }
        else
            DoctorReservationActivity.launch(this, DoctorReservationFragment.POSITION_NOTIFICATION_ACTION, order)
    }

    private fun pharmacyItem(order: PharmacyOrder)
    {
        if (resources.getBoolean(R.bool.is_tab))
        {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.fragment_container, PharmacyPrescriptionFragment.newInstance(order, PharmacyPrescriptionFragment.POSITION_NOTIFICATION_ACTION
                    , showToolBar = false))
                .addToBackStack(null)
                .commit()
        }
        else
            PharmacyPrescriptionActivity.launch(this, order, PharmacyPrescriptionFragment.POSITION_NOTIFICATION_ACTION)
    }

    private fun labItem(order: LabOrder)
    {
        if (resources.getBoolean(R.bool.is_tab))
        {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.fragment_container, LabOrderFragment.newInstance(order, LabOrderFragment.POSITION_NOTIFICATION_ACTION
                    , showToolBar = false))
                .addToBackStack(null)
                .commit()
        }
        else
            LabOrderActivity.launch(this, order, LabOrderFragment.POSITION_NOTIFICATION_ACTION)
    }

    private fun radiologyItem(order: RadiologyOrder)
    {
        if (resources.getBoolean(R.bool.is_tab))
        {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_right)
                .add(R.id.fragment_container, RadiologyOrderFragment.newInstance(order, RadiologyOrderFragment.POSITION_NOTIFICATION_ACTION, showToolBar = false))
                .addToBackStack(null)
                .commit()
        }
        else
            RadiologyOrderActivity.launch(this, order, RadiologyOrderFragment.POSITION_NOTIFICATION_ACTION)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item?.itemId == android.R.id.home)
        {
            onBackPressed()
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
