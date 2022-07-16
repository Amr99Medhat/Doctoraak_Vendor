package com.doctoraak.doctoraakdoctor.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.DoctorReservationItemBinding
import com.doctoraak.doctoraakdoctor.databinding.LabItemBinding
import com.doctoraak.doctoraakdoctor.databinding.PharmacyReservationItemBinding
import com.doctoraak.doctoraakdoctor.databinding.RadiologyItemBinding
import com.doctoraak.doctoraakdoctor.model.RadiologyOrder
import com.doctoraak.doctoraakdoctor.model.PharmacyOrder
import com.doctoraak.doctoraakdoctor.model.Reservation
import com.doctoraak.doctoraakdoctor.utils.Gender
import com.doctoraak.doctoraakdoctor.utils.imageUrl

class RadiologyOrderAdapter(val context: Context, var list: ArrayList<RadiologyOrder>?
                            , var itemClick: (Int, RadiologyOrder)->Unit)
    : RecyclerView.Adapter<RadiologyOrderAdapter.RadiologyOrderViewHolder>()
{
    private val anim_right by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right)}
    private val anim_left by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)}
    private var is_left_anim = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadiologyOrderViewHolder =
        RadiologyOrderViewHolder(DataBindingUtil.inflate(LayoutInflater
                .from(parent.context), R.layout.radiology_item, parent, false))

    override fun getItemCount(): Int = list.let { it?.size } ?: 0

    override fun onBindViewHolder(holder: RadiologyOrderViewHolder, position: Int)
    {
        val item = list!![position]

        holder.binding.order = item
        holder.binding.tvAnalysis.text = "${item.details.size} "+context.getString(R.string.analysis)

        animateItem(holder.binding.root)
    }

    private fun animateItem(view: View)
    {
        view.startAnimation(if (is_left_anim) anim_left else anim_right)
        is_left_anim = !is_left_anim
    }

    fun removeItem(position: Int)
    {
        if (position == -1)
            return

        list?.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class RadiologyOrderViewHolder(val binding: RadiologyItemBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.setOnClickListener { itemClick(adapterPosition, list!![adapterPosition]) }
        }
    }
}