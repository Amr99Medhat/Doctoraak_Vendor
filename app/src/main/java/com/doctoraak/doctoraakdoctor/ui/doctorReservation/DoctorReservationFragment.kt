package com.doctoraak.doctoraakdoctor.ui.doctorReservation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.FragmentDoctorReservationBinding
import com.doctoraak.doctoraakdoctor.model.Reservation
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.DialogType
import com.doctoraak.doctoraakdoctor.ui.PreviewImageDialog
import com.doctoraak.doctoraakdoctor.ui.SweetDialog
import com.doctoraak.doctoraakdoctor.ui.doctorHome.DoctorHomeActivity
import com.doctoraak.doctoraakdoctor.utils.*
import com.google.gson.Gson
import java.lang.StringBuilder

class DoctorReservationFragment(val itemCanceled: ((Int)->Unit)? = null) : Fragment()
{
    private lateinit var binding: FragmentDoctorReservationBinding
    private val viewModel by lazy { ViewModelProviders.of(this).get(DoctorReservationViewModel::class.java) }
    private lateinit var reservation: Reservation
    private var itemPosition = -1

    companion object
    {
        private const val RESERVATION_ARGS = "RESERVATION_ARGS"
        internal const val POSITION_ARGS = "POSITION_ARGS"
        private const val SHOW_TOOL_BAR_ARGS = "TOOL_BAR_ARGS"
        const val POSITION_NOTIFICATION_ACTION = -2

        @JvmStatic
        fun newInstance(position: Int, reservation: Reservation, itemCanceled: ((Int)->Unit)? = null, showToolBar: Boolean = true)
                = DoctorReservationFragment(itemCanceled).apply {
            arguments =  Bundle().apply {
                putString(RESERVATION_ARGS, Gson().toJson(reservation))
                putInt(POSITION_ARGS, position)
                putBoolean(SHOW_TOOL_BAR_ARGS, showToolBar)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_reservation, container, false)
        binding.clickHandler = DoctorReservationClickHandler()

        val data = activity?.intent?.getStringExtra(DoctorReservationActivity.RESERVATION_INTENT) ?:
            arguments?.getString(RESERVATION_ARGS)

        itemPosition = activity?.intent?.getIntExtra(DoctorReservationActivity.POSITION_INTENT, -1)!!
        itemPosition = if (itemPosition == -1)
            arguments?.getInt(POSITION_ARGS, -1)!! else itemPosition

        data?.let {
            reservation = Gson().fromJson(it, Reservation::class.java)
        }

        handleToolBar()
        initViews()

        observeData()

        return binding.root
    }

    private fun handleToolBar()
    {
        if (arguments?.getBoolean(SHOW_TOOL_BAR_ARGS, false) == true
            || activity?.intent?.getBooleanExtra(DoctorReservationActivity.SHOW_TOOL_BAR_INTENT, false) == true)
        {
            binding.toolbar.toolBar.visibility = VISIBLE
            BaseActivity.setToolBar((activity as AppCompatActivity), binding.toolbar.toolBar)
        }
        else
            binding.toolbar.toolBar.visibility = GONE
    }

    private fun initViews()
    {
        binding.reservation = reservation
        binding.tvType.text = reservation.type.valueOfDoctorReservationType()

        binding.tvNote.text = getNotes()

        binding.tvAgeGender.text = if (reservation.patient.gender == Gender.MALE.value)
            getString(R.string.male)+", "+reservation.patient.birthdate
        else getString(R.string.female)+", "+reservation.patient.birthdate

        if (reservation.notes.isEmpty())
        {
            binding.tvNote.hide()
            binding.ivEmptyNote.show()
        }
        else
        {
            binding.ivEmptyNote.hide()
            binding.tvNote.show()
        }

        if (reservation.patient.insuranceId == 0)
            binding.cvInsurance.hide()
        else
            SessionManager.getInsurance().find { it.id==reservation.patient.insuranceId }?.let {
                binding.tvInsuranceName.text = if (getString(R.string.language_key).equals(getString(R.string.english_key)))
                    it.name else it.name_ar

                Glide.with(context!!).load(it.photo).placeholder(R.color.gray_2).error(R.drawable.ic_insurance)
                    .into(binding.ivInsurance)
            }
    }

    private fun getNotes(): String
    {
        val notes = StringBuilder()
        var isBackSlashNote = 0
        reservation.notes.split(",").forEachIndexed { index, s ->
            notes.append(s)
            if (isBackSlashNote == 1)
            {
                isBackSlashNote = 0
                notes.append("\n")
            }
            else
            {
                isBackSlashNote++
                notes.append("  -  ")
            }
        }

        return notes.toString()
    }

    private fun startLoading()
    {
        binding.btnCancel.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnCancel.isEnabled = true
        binding.loading.loadingView.hide()
    }

    private fun observeData()
    {
        viewModel.isLoading.observe(this, Observer {
            if (it) startLoading() else stopLoading()
        })

        viewModel.errorInt.observe(this, Observer {
            it.showError(activity!!)
        })

        viewModel.errorMsg.observe(this, Observer { Utils.showError(context!!, it) })

        viewModel.cancelReservation.observe(this, Observer {
            SweetDialog.newInstance(context!!, DialogType.SUCCESS).apply {
                show()
                setCancelable(false)
                setMessage(it.msg)
                setOkClickListener {
                    if (itemCanceled != null)
                    {
                        it.dismiss()
                        fragmentManager?.beginTransaction()?.
                            remove(this@DoctorReservationFragment)?.commit()
                        itemCanceled!!(itemPosition)
                    }
                    else
                    {
                        with(Intent())
                        {
                            putExtra(DoctorHomeActivity.RESULT_ITEM_CANCELED, itemPosition)
                            activity!!.setResult(RESULT_OK, this)
                        }
                        it.dismiss()
                        activity!!.finish()
                    }
                }
            }
        })
    }


    inner class DoctorReservationClickHandler
    {
        fun cancelReservationClick()
        {
            if (::reservation.isInitialized)
            {
                Utils.showWarning(context!!, getString(R.string.are_you_sure_you_want_to_cancel_this_order_), okClick = {
                    viewModel.cancelReservation(SessionManager.getApiToken(), SessionManager.getUserId(), reservation.id)
                })
            }
        }

        fun okReservationClick()
        {
            if (resources.getBoolean(R.bool.is_tab))
                fragmentManager?.popBackStack()
            else
                activity?.finish()
        }

        fun patientImageClick() {
            PreviewImageDialog.newInstance(reservation.patient.photo, R.drawable.ic_default_user_icon)
                .show(fragmentManager!!, "")
        }
    }


}
