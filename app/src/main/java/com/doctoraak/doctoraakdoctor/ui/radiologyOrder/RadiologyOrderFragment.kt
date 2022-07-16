package com.doctoraak.doctoraakdoctor.ui.radiologyOrder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakdoctor.Adapter.RadiologyAnalysisAdapter

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.customView.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakdoctor.databinding.FragmentRadiologyOrderBinding
import com.doctoraak.doctoraakdoctor.model.RadiologyOrder
import com.doctoraak.doctoraakdoctor.ui.*
import com.doctoraak.doctoraakdoctor.ui.radiologyHome.RadiologyHomeActivity
import com.doctoraak.doctoraakdoctor.utils.Utils
import com.doctoraak.doctoraakdoctor.utils.hide
import com.doctoraak.doctoraakdoctor.utils.show
import com.doctoraak.doctoraakdoctor.utils.showError
import com.google.gson.Gson

class RadiologyOrderFragment(val itemCanceled: ((Int)->Unit)? = null) : Fragment()
{
    private lateinit var binding: FragmentRadiologyOrderBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(RadiologyOrderViewModel::class.java) }

    private lateinit var radiologyOrder: RadiologyOrder
    private var itemPosition = -1

    companion object
    {
        private const val ORDER_ARGS = "ORDER_ARGS"
        private const val ACTION_ARGS = "ACTION_ARGS"
        const val POSITION_NOTIFICATION_ACTION = -2
        private const val SHOW_TOOL_BAR_ARGS = "TOOL_BAR_ARGS"

        @JvmStatic
        fun newInstance(radiologyOrder: RadiologyOrder, action: Int = 0, itemCanceled: ((Int)->Unit)? = null, showToolBar: Boolean = true)
                = RadiologyOrderFragment(itemCanceled).apply {
            arguments =  Bundle().apply {
                putString(ORDER_ARGS, Gson().toJson(radiologyOrder))
                putInt(ACTION_ARGS, action)
                putBoolean(SHOW_TOOL_BAR_ARGS, showToolBar)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_radiology_order, container, false)

        binding.clickHandler = RadiologyClickHandler()

        val data = activity?.intent?.getStringExtra(RadiologyOrderActivity.ORDER_INTENT)
            ?: arguments?.getString(ORDER_ARGS)
        data?.let {
            radiologyOrder = Gson().fromJson(it, RadiologyOrder::class.java) }

        itemPosition = activity?.intent?.getIntExtra(RadiologyOrderActivity.ACTION_INTENT, -1)!!
        itemPosition = if (itemPosition == -1)
            arguments?.getInt(ACTION_ARGS, -1)!!
        else itemPosition

        observeData()
        handleToolBar()
        initViews()

        return binding.root
    }

    private fun handleToolBar()
    {
        if (arguments?.getBoolean(SHOW_TOOL_BAR_ARGS, false) == true
            || activity?.intent?.getBooleanExtra(RadiologyOrderActivity.SHOW_TOOL_BAR_INTENT, false) == true)
        {
            binding.toolbar.toolBar.visibility = View.VISIBLE
            BaseActivity.setToolBar((activity as AppCompatActivity), binding.toolbar.toolBar)
        }
        else
            binding.toolbar.toolBar.visibility = View.GONE
    }

    private fun initViews()
    {
        binding.rvAnalysis.addItemDecoration(
            DividerItemDecorationExceptLast(context!!
                , ResourcesCompat.getDrawable(resources, R.drawable.drawable_divider, null)!!))
        binding.rvAnalysis.layoutManager =  LinearLayoutManager(context!!)
        binding.rvAnalysis.adapter = RadiologyAnalysisAdapter(
            context!!,
            radiologyOrder.details
        )

        binding.order = radiologyOrder
        binding.ivImage.visibility = if (TextUtils.isEmpty(radiologyOrder.photo)) View.GONE else View.VISIBLE
    }

    private fun startLoading()
    {
        binding.btnAccept.isEnabled = false
        binding.btnCancel.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnAccept.isEnabled = true
        binding.btnCancel.isEnabled = true

        binding.loading.loadingView.hide()
    }

    private fun observeData()
    {
        viewModel.isLoading.observe(this, Observer {
            if (it) startLoading() else stopLoading()
        })

        viewModel.errorInt.observe(this, Observer {
            it.showError(context!!)
        })

        viewModel.errorMsg.observe(this, Observer { Utils.showError(context!!, it) })

        viewModel.acceptRadiologyOrder.observe(this, Observer {
            SweetDialog.newInstance(context!!, DialogType.SUCCESS).apply {
                show()
                setCancelable(false)
                setMessage(it.msg)
            }
        })

        viewModel.cancelRadiologyOrder.observe(this, Observer {
            SweetDialog.newInstance(context!!, DialogType.SUCCESS).apply {
                show()
                setCancelable(false)
                setMessage(it.msg)
                setOkClickListener {
                    it.dismiss()
                    if (resources.getBoolean(R.bool.is_tab))
                    {
                        fragmentManager?.popBackStack()
                        itemCanceled!!(itemPosition)
                    }
                    else
                    {
                        with(Intent())
                        {
                            putExtra(RadiologyHomeActivity.RESULT_ITEM_CANCELED, itemPosition)
                            activity!!.setResult(Activity.RESULT_OK, this)
                        }
                        activity!!.finish()
                    }
                }
            }
        })
    }


    inner class RadiologyClickHandler
    {
        fun acceptOrderClick()
        {
            if (::radiologyOrder.isInitialized)
            {
                Utils.showWarning(context!!, getString(R.string.are_you_sure_you_want_to_accept_this_order_), okClick = {
                    viewModel.acceptRadiologyOrder(SessionManager.getApiToken(), SessionManager.getUserId(), radiologyOrder.id)
                })
            }
        }

        fun refuseOrderClick()
        {
            if (::radiologyOrder.isInitialized)
            {
                InputUserReasonDialog.newInstance {
                    viewModel.cancelRadiologyOrder(
                        SessionManager.getApiToken(), SessionManager.getUserId()
                        , radiologyOrder.id, it)
                }.show(fragmentManager!!, "InputUserReasonDialog")
            }
        }

        fun patientImageClick() {
            PreviewImageDialog.newInstance(radiologyOrder.patient.photo, R.drawable.ic_default_user_icon)
                .show(fragmentManager!!, "")
        }

        fun orderImageClick() {
            PreviewImageDialog.newInstance(radiologyOrder.photo)
                .show(fragmentManager!!, "")
        }
    }

}
