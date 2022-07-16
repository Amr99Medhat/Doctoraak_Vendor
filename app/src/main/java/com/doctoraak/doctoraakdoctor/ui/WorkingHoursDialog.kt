package com.doctoraak.doctoraakdoctor.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakdoctor.Adapter.WorkingHourAdapter

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.customView.DividerItemDecorationExceptLast
import com.doctoraak.doctoraakdoctor.databinding.FragmentWorkingHoursDialogBinding
import com.doctoraak.doctoraakdoctor.model.ShiftWorkingHour
import com.doctoraak.doctoraakdoctor.model.WorkingHour
import com.doctoraak.doctoraakdoctor.utils.Constants
import com.doctoraak.doctoraakdoctor.utils.hide
import com.doctoraak.doctoraakdoctor.utils.show
import com.google.android.material.snackbar.Snackbar

class WorkingHoursDialog(context: Context, var workingHoursList: List<WorkingHour>? = null, val isEditable: Boolean = true
                         , val listener: onWorkingHoursListener) : Dialog(context, R.style.FullScreenDialogTheme)
{
    companion object
    {
        fun newInstance(context: Context, workingHoursList: List<WorkingHour>? = null, isEditable: Boolean = true
                        , listener: onWorkingHoursListener): WorkingHoursDialog =
            WorkingHoursDialog(context, workingHoursList, isEditable, listener)
    }

    private lateinit var binding: FragmentWorkingHoursDialogBinding

    init
    {
        if (workingHoursList == null || workingHoursList?.size == 0)
        {
            workingHoursList = List(7) { WorkingHour().apply {
                active = 0; day = it+1;
                part_from = Constants.TIME_DEFAULT; part_to = Constants.TIME_DEFAULT; }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_working_hours_dialog
            , null, false)
        setContentView(binding.root)

        with(binding.rvWorkingHours)
        {
            layoutManager = LinearLayoutManager(context)
            adapter = WorkingHourAdapter(
                context,
                workingHoursList!!,
                isEditable
            )
        }

        if (isEditable)
            binding.btnSave.show()
        else
            binding.btnSave.hide()

        binding.btnSave.setOnClickListener {
            Log.d("saif", "setOnClickListener: adapter.workingHours= ${workingHoursList}")

            if (validateWorkingDays())
            {
                listener.onWorkingHoursSelected(workingHoursList!!)
                dismiss()
            }
        }

        binding.ivBack.setOnClickListener {
            dismiss()
        }
    }

    private fun validateWorkingDays(): Boolean
    {
        val days = context.resources.getStringArray(R.array.days_of_week)
        workingHoursList?.filter { it.active == 1 }?.forEach {
            if (TextUtils.isEmpty(it.part_from) || it.part_from.equals(Constants.TIME_DEFAULT))
            {
                Toast.makeText(context, days[it.day-1]+" "+context.getString(R.string.start_time_is_required_or_deactivate_day), Toast.LENGTH_LONG).show()
                return false
            }
            else if (TextUtils.isEmpty(it.part_to) || it.part_to.equals(Constants.TIME_DEFAULT))
            {
                Toast.makeText(context, days[it.day-1]+" "+context.getString(R.string.end_time_is_required_or_deactivate_day), Toast.LENGTH_LONG).show()
                return false
            }
        }

        return true
    }


}


interface onWorkingHoursListener
{
    fun onWorkingHoursSelected(workingHours: List<WorkingHour>)
}
