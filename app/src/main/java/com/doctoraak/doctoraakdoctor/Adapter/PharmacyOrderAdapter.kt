package com.doctoraak.doctoraakdoctor.Adapter

import android.content.Context
import android.util.Log
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
import com.doctoraak.doctoraakdoctor.databinding.PharmacyReservationItemBinding
import com.doctoraak.doctoraakdoctor.model.PharmacyOrder
import com.doctoraak.doctoraakdoctor.model.Reservation
import com.doctoraak.doctoraakdoctor.utils.Gender
import com.doctoraak.doctoraakdoctor.utils.Utils
import com.doctoraak.doctoraakdoctor.utils.imageUrl

class PharmacyOrderAdapter(val context: Context, var list: List<PharmacyOrder>?, var itemClick: (PharmacyOrder)->Unit)
    : RecyclerView.Adapter<PharmacyOrderAdapter.PharmacyOrderViewHolder>()
{
    private val anim_right by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right)}
    private val anim_left by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)}
    private var is_left_anim = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyOrderViewHolder =
        PharmacyOrderViewHolder(DataBindingUtil.inflate(LayoutInflater
                .from(parent.context), R.layout.pharmacy_reservation_item, parent, false))

    override fun getItemCount(): Int = list.let { it?.size } ?: 0

    override fun onBindViewHolder(holder: PharmacyOrderViewHolder, position: Int)
    {
        val item = list!![position]
        holder.binding.run {
            tvPatientName.text = item.patient.name
            tvDate.text = item.createdAt
            val address = Utils.getTextForAppLanguage(item.patient.address, item.patient.addressAr, item.patient.addressFr)
            tvAddress.text = if (address.isNullOrEmpty()) address else context.getString(R.string.no_address_specified)

            val image = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_default_user_icon, null)
            imageUrl(ivPatientImage, item.patient.photo, image, image)
        }

        animateItem(holder.binding.root)
    }

    private fun animateItem(view: View)
    {
        view.startAnimation(if (is_left_anim) anim_left else anim_right)
        is_left_anim = !is_left_anim
    }

    inner class PharmacyOrderViewHolder(val binding: PharmacyReservationItemBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.setOnClickListener { itemClick(list!![adapterPosition]) }
        }
    }
}