package com.doctoraak.doctoraakdoctor.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.DoctorReservationItemBinding
import com.doctoraak.doctoraakdoctor.databinding.LabOrderItemBinding
import com.doctoraak.doctoraakdoctor.databinding.RadiologyOrderItemBinding
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.Gender
import com.doctoraak.doctoraakdoctor.utils.imageUrl

class RadiologyAnalysisAdapter(val context: Context, var list: List<RadiologyDetail>?)
    : RecyclerView.Adapter<RadiologyAnalysisAdapter.RadiologyOrderViewHolder>()
{
    private val anim_right by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right)}
    private val anim_left by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)}
    private var is_left_anim = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadiologyOrderViewHolder =
        RadiologyOrderViewHolder(DataBindingUtil.inflate(LayoutInflater
                .from(parent.context), R.layout.radiology_order_item, parent, false))

    override fun getItemCount(): Int = list.let { it?.size } ?: 0

    // todo i ignored language;
    override fun onBindViewHolder(holder: RadiologyOrderViewHolder, position: Int)
    {
        val item = list!![position]
        holder.binding.tvName.text = item.rays.name
        
        animateItem(holder.binding.root)
    }

    private fun animateItem(view: View)
    {
        view.startAnimation(if (is_left_anim) anim_left else anim_right)
        is_left_anim = !is_left_anim
    }

    inner class RadiologyOrderViewHolder(val binding: RadiologyOrderItemBinding)
        : RecyclerView.ViewHolder(binding.root)
}