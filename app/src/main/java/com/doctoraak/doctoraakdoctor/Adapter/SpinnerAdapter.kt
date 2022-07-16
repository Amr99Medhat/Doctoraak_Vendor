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
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.model.Degree
import com.doctoraak.doctoraakdoctor.model.Insurance
import com.doctoraak.doctoraakdoctor.model.Main
import com.doctoraak.doctoraakdoctor.model.Specialization
import com.doctoraak.doctoraakdoctor.utils.Language
import com.doctoraak.doctoraakdoctor.utils.imageUrl
import com.google.gson.Gson

class SpinnerAdapter(context: Context, var list: List<Main> = arrayListOf()
)
    : ArrayAdapter<String>(context, R.layout.spinner_item, R.id.tv_name
    , List(list.size, { index -> if (SessionManager.getLanguage() == Language.ENGLISH.name)
        list[index].name
    else
        list[index].name_ar
    }))
{

    private val currentLanguage by lazy { if (SessionManager.getLanguage() == Language.ENGLISH.name)
        Language.ENGLISH
    else
        Language.ARABIC
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup?): View
    {
        val view: View
        val holder: SpinnerHolder
        if (convertView == null)
        {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.spinner_item, parent, false)
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

        if (list.isEmpty())
        {
            holder.tv_name.text = context.getString(R.string.no_available_areas_for_that_city)
            return view
        }

        val item = list.get(position)
        holder.tv_name.text = if (currentLanguage == Language.ENGLISH )
            item.name
        else
            item.name_ar

        if (item is Specialization)
            Glide.with(view.context).load(item.icon).into(holder.iv_icon)
        else if (item is Insurance)
            Glide.with(view.context).load(item.photo).into(holder.iv_icon)

        return view
    }

    override fun getCount() = if (list != null && list.isNotEmpty())
        list.size else 1

    override fun getItem(position: Int) = list.getOrNull(position)?.name ?: ""



    class SpinnerHolder
    {
        internal lateinit var iv_icon: ImageView
        internal lateinit var tv_name: TextView
    }

}