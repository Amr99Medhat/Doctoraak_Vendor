package com.doctoraak.doctoraakdoctor.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.DoctorReservationItemBinding
import com.doctoraak.doctoraakdoctor.databinding.LabOrderItemBinding
import com.doctoraak.doctoraakdoctor.databinding.PharmacyReservationItemBinding
import com.doctoraak.doctoraakdoctor.model.LabDetail
import com.doctoraak.doctoraakdoctor.model.LabOrder
import com.doctoraak.doctoraakdoctor.model.PharmacyOrder
import com.doctoraak.doctoraakdoctor.model.Reservation
import com.doctoraak.doctoraakdoctor.utils.Gender
import com.doctoraak.doctoraakdoctor.utils.Language
import com.doctoraak.doctoraakdoctor.utils.imageUrl

class LabAnalysisAdapter(val context: Context, var list: List<LabDetail>?)
    : RecyclerView.Adapter<LabAnalysisAdapter.LabOrderViewHolder>()
{
    private val anim_right by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right)}
    private val anim_left by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)}
    private var is_left_anim = false

    private val currentLanguage by lazy { if (SessionManager.getLanguage() == Language.ENGLISH.name)
        Language.ENGLISH
    else
        Language.ARABIC
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabOrderViewHolder =
        LabOrderViewHolder(DataBindingUtil.inflate(LayoutInflater
                .from(parent.context), R.layout.lab_order_item, parent, false))

    override fun getItemCount(): Int = list.let { it?.size } ?: 0

    override fun onBindViewHolder(holder: LabOrderViewHolder, position: Int)
    {
        val item = list!![position]
        holder.binding.tvName.text = if (currentLanguage == Language.ENGLISH)
            item.analysis.name
        else
            item.analysis.nameAr

        animateItem(holder.binding.root)
    }

    private fun animateItem(view: View)
    {
        view.startAnimation(if (is_left_anim) anim_left else anim_right)
        is_left_anim = !is_left_anim
    }

    inner class LabOrderViewHolder(val binding: LabOrderItemBinding)
        : RecyclerView.ViewHolder(binding.root)
}