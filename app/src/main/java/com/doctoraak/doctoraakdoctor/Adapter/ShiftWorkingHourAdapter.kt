package com.doctoraak.doctoraakdoctor.Adapter

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.WorkingHoursItemBinding
import com.doctoraak.doctoraakdoctor.model.ShiftWorkingHour
import com.doctoraak.doctoraakdoctor.utils.Constants
import com.doctoraak.doctoraakdoctor.utils.Utils
import com.doctoraak.doctoraakdoctor.utils.convertToHours12
import java.util.*

/** shift 1 == index is 1    ,    shift 2 == index is 2  */
class ShiftWorkingHourAdapter(val context: Context, var shift: Int, val shiftWorkingHoursList: List<ShiftWorkingHour>
                              , val isEditable: Boolean = true)
    : RecyclerView.Adapter<ShiftWorkingHourAdapter.WorkingHourViewHolder>()
{
    private val daysList: Array<String> by lazy { context.resources.getStringArray(R.array.days_of_week) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkingHourViewHolder =
        WorkingHourViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater
                    .from(context), R.layout.working_hours_item, parent, false
            )
        )

    override fun getItemCount(): Int  = 7 // for one Week = 7 days;

    override fun onBindViewHolder(holder: WorkingHourViewHolder, position: Int)
    {
        val item = shiftWorkingHoursList[position]
        holder.binding.rbDay.text = daysList[position]
        holder.binding.rbDay.isChecked = if (item.active == 0) false else true

        val calendar = Calendar.getInstance()
        if (shift == 1)
        {
            Log.d("saif", "$1_from= ${item.part1_from}      , 1_to= ${item.part1_to}")
            holder.binding.tvFrom.text = item.part1_from?.convertToHours12(calendar)
            holder.binding.tvTo.text = item.part1_to?.convertToHours12(calendar)
        }
        else
        {
            holder.binding.tvFrom.text = item.part2_from?.convertToHours12(calendar)
            holder.binding.tvTo.text = item.part2_to?.convertToHours12(calendar)
        }

        //////////   listeners  //////////
        if (isEditable)
        {
            holder.binding.rbDay.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked)
                {
                    if (shift == 1)
                    {
                        if (Constants.TIME_DEFAULT.equals(shiftWorkingHoursList[position].part2_from)
                            && Constants.TIME_DEFAULT.equals(shiftWorkingHoursList[position].part2_to))
                        {
                            shiftWorkingHoursList[position].active = 0
                            Log.d("saif", "onBindViewHolder:  active = 0 in shift 1")
                        }
                    }
                    else
                        if (Constants.TIME_DEFAULT.equals(shiftWorkingHoursList[position].part1_from)
                            || Constants.TIME_DEFAULT.equals(shiftWorkingHoursList[position].part1_to))
                        {
                            shiftWorkingHoursList[position].active = 0
                            Log.d("saif", "onBindViewHolder:  active = 0 in shift 2")
                        }
                }
                else
                    shiftWorkingHoursList[position].active = 1
            }
            holder.binding.tvFrom.setOnClickListener {showTimePickerDialog(position, true, it as TextView)}
            holder.binding.tvTo.setOnClickListener {showTimePickerDialog(position, false, it as TextView)}
        }
        else
        {
            holder.binding.rbDay.isEnabled = false
        }
    }

    private fun showTimePickerDialog(position: Int, isFrom: Boolean, textView: TextView)
    {
        if (shiftWorkingHoursList[position].active == 0)
        {
            Toast.makeText(context, context.getString(R.string.active_this_day_first), Toast.LENGTH_SHORT).show()
            return
        }

        val calendar = Calendar.getInstance()
        TimePickerDialog(context, { view: TimePicker?, hourOfDay: Int, minute: Int ->
            textView.text = Utils.convertToHours12(calendar, hourOfDay, minute)
            val time = "$hourOfDay:$minute:00"
            if (shift == 1)
                if (isFrom)
                    shiftWorkingHoursList[position].part1_from = time
                else
                    shiftWorkingHoursList[position].part1_to = time
            else
                if (isFrom)
                    shiftWorkingHoursList[position].part2_from = time
                else
                    shiftWorkingHoursList[position].part2_to = time
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).apply {
                setOnShowListener {
                    handleBtn(getButton(Dialog.BUTTON_POSITIVE)
                        , getButton(Dialog.BUTTON_NEGATIVE))
                }
            }.show()
    }

    private fun handleBtn(positiveBtn: Button, negativeBtn: Button)
    {
        val handleBtn: (Button)->Unit = {
            with(it)
            {
                setBackgroundResource(R.color.transparent)
                setTextColor(ContextCompat.getColor(context, R.color.gray_3))
            }
        }

        handleBtn(positiveBtn)
        handleBtn(negativeBtn)
    }

    fun goToShift(shift: Int)
    {
        this.shift = shift
        notifyDataSetChanged()
    }


    class WorkingHourViewHolder(val binding: WorkingHoursItemBinding) : RecyclerView.ViewHolder(binding.root)
}