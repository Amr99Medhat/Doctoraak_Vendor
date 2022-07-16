package com.doctoraak.doctoraakdoctor.ui

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakdoctor.Adapter.ShiftWorkingHourAdapter

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.FragmentShiftWorkingHoursDialogBinding
import com.doctoraak.doctoraakdoctor.model.ShiftWorkingHour
import com.doctoraak.doctoraakdoctor.utils.Constants
import com.doctoraak.doctoraakdoctor.utils.Utils
import com.doctoraak.doctoraakdoctor.utils.hide
import com.doctoraak.doctoraakdoctor.utils.show

class ShiftWorkingHoursDialog(context: Context, var shiftWorkingHoursList: List<ShiftWorkingHour>? = null, val isEditable: Boolean = true
                              , val listener: OnShiftWorkingHoursListener, val isDialogAfterChanged: Boolean = false)
    : Dialog(context, R.style.FullScreenDialogTheme)
{
    private lateinit var binding: FragmentShiftWorkingHoursDialogBinding
    private lateinit var adapter: ShiftWorkingHourAdapter

    companion object
    {
        @JvmStatic
        fun newInstance(context: Context, workingHoursList: List<ShiftWorkingHour>? = null, isEditable: Boolean = true
                        , listener: OnShiftWorkingHoursListener, isDialogAfterChanged: Boolean = false): ShiftWorkingHoursDialog =
            ShiftWorkingHoursDialog(context, workingHoursList, isEditable, listener, isDialogAfterChanged)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_shift_working_hours_dialog, null, false)
        setContentView(binding.root)
        binding.clickHandler = ShiftWorkingHoursClickHandler()

        init()

        if (isEditable)
            binding.btnSave.show()
        else
            binding.btnSave.hide()

        binding.btnSave.setOnClickListener {
            Log.d("saif", "setOnClickListener: adapter.workingHours= ${shiftWorkingHoursList}")

            if (validateWorkingDays())
            {
                if (isDialogAfterChanged)
                {
                    Utils.showWarning(context, context.getString(R.string.are_you_sure_you_want_to_change_your_working_hours__), okClick = {
                        listener.onWorkingHoursSelected(shiftWorkingHoursList!!)
                        dismiss()
                    })
                }
                else
                {
                    listener.onWorkingHoursSelected(shiftWorkingHoursList!!)
                    dismiss()
                }
            }
        }
    }

    private fun init()
    {
        if (shiftWorkingHoursList == null || shiftWorkingHoursList?.size == 0)
        {
            shiftWorkingHoursList = List(7) { ShiftWorkingHour().apply {
                active = 0; day = it+1;
                part1_from = Constants.TIME_DEFAULT; part2_from = Constants.TIME_DEFAULT;
                part1_to = Constants.TIME_DEFAULT; part2_to = Constants.TIME_DEFAULT; }
            }
        }

        initRecyclerShift()
    }

    private fun initRecyclerShift()
    {
        adapter = ShiftWorkingHourAdapter(context, 1, shiftWorkingHoursList!!, isEditable)

        with(binding.rvWorkingHours)
        {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ShiftWorkingHoursDialog.adapter
        }
    }

    private fun validateWorkingDays(): Boolean
    {
        val days = context.resources.getStringArray(R.array.days_of_week)
        shiftWorkingHoursList?.filter { it.active == 1 }?.forEach {

            if ((!it.part1_from.equals(Constants.TIME_DEFAULT) && !it.part1_to.equals(Constants.TIME_DEFAULT) )
                || (!it.part2_from.equals(Constants.TIME_DEFAULT) && !it.part2_to.equals(Constants.TIME_DEFAULT)))
                return true

            if (it.part1_from.equals(Constants.TIME_DEFAULT) || it.part2_from.equals(Constants.TIME_DEFAULT))
            {
                Toast.makeText(context, days[it.day-1]+" "
                        +context.getString(R.string.start_time_is_required_or_deactivate_day), Toast.LENGTH_LONG).show()
                return false
            }
            else if (it.part1_to.equals(Constants.TIME_DEFAULT) || it.part2_to.equals(Constants.TIME_DEFAULT))
            {
                Toast.makeText(context, days[it.day-1]+" "+context.getString(R.string.end_time_is_required_or_deactivate_day), Toast.LENGTH_LONG).show()
                return false
            }
            else
            {
                Toast.makeText(context, "Not valid", Toast.LENGTH_LONG).show()
                return false
            }

        }

        return true
    }



    inner class ShiftWorkingHoursClickHandler
    {
        fun onBackClick()
        {
            dismiss()
        }

        fun onShift1Click()
        {
            adapter.goToShift(1)

            ImageViewCompat.setImageTintList(binding.tvShift1Text, ColorStateList.valueOf(ContextCompat.getColor(context
                , R.color.accentTransparent)))
            ImageViewCompat.setImageTintList(binding.tvShift2Text, null)
        }

        fun onShift2Click()
        {
            adapter.goToShift(2)

            ImageViewCompat.setImageTintList(binding.tvShift2Text, ColorStateList.valueOf(ContextCompat.getColor(context
                , R.color.accentTransparent)))
            ImageViewCompat.setImageTintList(binding.tvShift1Text, null)
        }
    }

}

interface OnShiftWorkingHoursListener
{
    fun onWorkingHoursSelected(shiftWorkingHours: List<ShiftWorkingHour>)
}


