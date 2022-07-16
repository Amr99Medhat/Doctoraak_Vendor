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
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.WorkingHoursItemBinding
import com.doctoraak.doctoraakdoctor.model.WorkingHour
import com.doctoraak.doctoraakdoctor.utils.Utils
import com.doctoraak.doctoraakdoctor.utils.convertToHours12
import java.util.*

class WorkingHourAdapter(val context: Context, val workingHoursList: List<WorkingHour>, val isEditable: Boolean = true)
    : RecyclerView.Adapter<WorkingHourAdapter.WorkingHourViewHolder>()
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
        val item = workingHoursList[position]
        holder.binding.rbDay.text = daysList[position]
        holder.binding.rbDay.isChecked = if (item.active == 0) false else true

        with(Calendar.getInstance())
        {
            holder.binding.tvFrom.text = item.part_from.convertToHours12(this)
            holder.binding.tvTo.text = item.part_to.convertToHours12(this)
        }

        //////////   listeners  //////////
        if (isEditable)
        {
            holder.binding.rbDay.setOnCheckedChangeListener { _, isChecked ->
                workingHoursList[position].active = if (isChecked) 1 else 0
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
        if (workingHoursList[position].active == 0)
        {
            Toast.makeText(context, context.getString(R.string.active_this_day_first), Toast.LENGTH_SHORT).show()
            return
        }

        val calendar = Calendar.getInstance()
        TimePickerDialog(context, { view: TimePicker?, hourOfDay: Int, minute: Int ->
            textView.text =  Utils.convertToHours12(calendar, hourOfDay, minute)

            val time = "$hourOfDay:$minute:00"
            if (isFrom)
                workingHoursList[position].part_from = time
            else
                workingHoursList[position].part_to = time

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


    class WorkingHourViewHolder(val binding: WorkingHoursItemBinding) : RecyclerView.ViewHolder(binding.root)
}