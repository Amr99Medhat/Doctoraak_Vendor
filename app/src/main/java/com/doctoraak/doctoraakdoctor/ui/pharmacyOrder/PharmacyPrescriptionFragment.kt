package com.doctoraak.doctoraakdoctor.ui.pharmacyOrder

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakdoctor.Adapter.PharmacyMedicineAdapter

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.customView.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakdoctor.databinding.FragmentPharmacyPrescriptionBinding
import com.doctoraak.doctoraakdoctor.model.PharmacyOrder
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.DialogType
import com.doctoraak.doctoraakdoctor.ui.PreviewImageDialog
import com.doctoraak.doctoraakdoctor.ui.SweetDialog
import com.doctoraak.doctoraakdoctor.ui.labOrder.LabOrderFragment
import com.doctoraak.doctoraakdoctor.utils.Utils
import com.doctoraak.doctoraakdoctor.utils.hide
import com.doctoraak.doctoraakdoctor.utils.show
import com.doctoraak.doctoraakdoctor.utils.showError
import com.google.gson.Gson

class PharmacyPrescriptionFragment : Fragment()
{
    private lateinit var binding: FragmentPharmacyPrescriptionBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(PharmacyOrderViewModel::class.java) }
    private lateinit var pharmacyOrder: PharmacyOrder

    companion object
    {
        private const val ORDER_ARGS = "ORDER_ARGS"
        private const val ACTION_ARGS = "ACTION_ARGS"
        const val POSITION_NOTIFICATION_ACTION = -2
        private const val SHOW_TOOL_BAR_ARGS = "TOOL_BAR_ARGS"


        @JvmStatic
        fun newInstance(pharmacyOrder: PharmacyOrder, action: Int = 0, showToolBar: Boolean = true)
                = PharmacyPrescriptionFragment().apply {
            arguments =  Bundle().apply {
                putString(ORDER_ARGS, Gson().toJson(pharmacyOrder))
                putInt(ACTION_ARGS, action)
                putBoolean(SHOW_TOOL_BAR_ARGS, showToolBar)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pharmacy_prescription, container, false)
        binding.clickHandler = PharmacyOrderClickHandler()

        val data = activity?.intent?.getStringExtra(PharmacyPrescriptionActivity.ORDER_INTENT)
            ?: arguments?.getString(ORDER_ARGS)
        data?.let {
            pharmacyOrder = Gson().fromJson(it, PharmacyOrder::class.java)
        }

        initViews()

        handleToolBar()
        observeData()

        return binding.root
    }

    private fun handleToolBar()
    {
        if (arguments?.getBoolean(SHOW_TOOL_BAR_ARGS, false) == true
            || activity?.intent?.getBooleanExtra(PharmacyPrescriptionActivity.SHOW_TOOL_BAR_INTENT, false) == true)
        {
            binding.toolbar.toolBar.visibility = View.VISIBLE
            BaseActivity.setToolBar((activity as AppCompatActivity), binding.toolbar.toolBar)
        }
        else
            binding.toolbar.toolBar.visibility = View.GONE
    }

    private fun initViews()
    {
        binding.rvPrescription.addItemDecoration(
            DividerItemDecorationExceptLast(context!!
                , ResourcesCompat.getDrawable(resources, R.drawable.drawable_divider, null)!!)
        )
        binding.rvPrescription.layoutManager =  LinearLayoutManager(context!!)
        Log.d("saif", "initViews, detialsSize= ${pharmacyOrder.details}")
        binding.rvPrescription.adapter = PharmacyMedicineAdapter(context!!, pharmacyOrder.details)

        binding.order = pharmacyOrder

        if (pharmacyOrder.photo.isNullOrEmpty())
        {
            binding.ivPrescription.hide()
            binding.tvPrescriptionText.hide()
        } else binding.ivPrescription.show()

        if (pharmacyOrder.accept.trim() == "0" || pharmacyOrder.accept.trim() == "0.0" )
            binding.btnAccept.visibility = VISIBLE
        else if(pharmacyOrder.pharmacyId == SessionManager.getUserId())
            binding.btnAccept.visibility = GONE

//        binding.scrollView.fullScroll(View.FOCUS_UP)
//        binding.scrollView.scrollTo(0,0)
    }

    private fun startLoading()
    {
        binding.btnAccept.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnAccept.isEnabled = true
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

        viewModel.acceptPharmacyOrder.observe(this, Observer {
            binding.btnAccept.visibility = GONE
            SweetDialog.newInstance(context!!, DialogType.SUCCESS).apply {
                show()
                setCancelable(false)
                setMessage(it.msg)
                setOkClickListener {
                    it.dismiss()
                }
            }
        })
    }


    inner class PharmacyOrderClickHandler
    {
        fun acceptOrder()
        {
            if (::pharmacyOrder.isInitialized)
            {
                Utils.showWarning(context!!, getString(R.string.accept_order_only_if_you_have_all_its_medicines), okClick = {
                    viewModel.acceptPharmacyOrder(SessionManager.getApiToken(), SessionManager.getUserId(), pharmacyOrder.id)
                })
            }
        }

        fun patientImageClick() {
            PreviewImageDialog.newInstance(pharmacyOrder.patient.photo, R.drawable.ic_default_user_icon)
                .show(fragmentManager!!, "")
        }

        fun OrderImageClick() {
            PreviewImageDialog.newInstance(pharmacyOrder.photo)
                .show(fragmentManager!!, "")
        }
    }

}
