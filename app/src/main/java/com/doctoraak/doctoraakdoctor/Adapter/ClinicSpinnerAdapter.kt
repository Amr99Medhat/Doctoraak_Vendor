package com.doctoraak.doctoraakdoctor.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.imageUrl

class ClinicSpinnerAdapter(context: Context, val list: List<Clinic>)
    : ArrayAdapter<String>(context, R.layout.clinic_spinner_item
    , R.id.tv_name, List(list.size, {index -> context.getString(R.string.clinic)+" "+(index+1) }))
{

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup?): View
    {
        val view: View
        val holder: SpinnerHolder
        if (convertView == null)
        {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.clinic_spinner_item, parent, false)
            holder = SpinnerHolder()
            holder.tv_name = view.findViewById(R.id.tv_name)
            holder.iv_icon = view.findViewById(R.id.iv_icon)
            view.tag = holder
        }
        else
        {
            view = convertView
            holder = view.tag as SpinnerHolder
        }

        val item = list.get(position)
        holder.tv_name.text = context.getString(R.string.clinic)+" "+(position+1)

        holder.iv_icon.visibility = View.VISIBLE
        holder.tv_name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)

        Glide.with(view.context).load(item.photo).error(R.drawable.ic_clinic).into(holder.iv_icon)

        return view
    }


    class SpinnerHolder
    {
        internal lateinit var iv_icon: ImageView
        internal lateinit var tv_name: TextView
    }

}