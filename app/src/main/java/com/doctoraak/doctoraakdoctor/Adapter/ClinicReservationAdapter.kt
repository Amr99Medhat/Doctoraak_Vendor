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
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.DoctorReservationItemBinding
import com.doctoraak.doctoraakdoctor.model.Reservation
import com.doctoraak.doctoraakdoctor.utils.DoctorReservationType
import com.doctoraak.doctoraakdoctor.utils.Gender
import com.doctoraak.doctoraakdoctor.utils.imageUrl

class ClinicReservationAdapter(val context: Context, var list: ArrayList<Reservation>?, var itemClick: (Int, Reservation)->Unit)
    : RecyclerView.Adapter<ClinicReservationAdapter.ClinicReservationViewHolder>()
{
    private val anim_right by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right)}
    private val anim_left by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)}
    private var is_left_anim = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicReservationViewHolder =
        ClinicReservationViewHolder(DataBindingUtil.inflate(LayoutInflater
                .from(parent.context), R.layout.doctor_reservation_item, parent, false))

    override fun getItemCount(): Int = list.let { it?.size } ?: 0

    override fun onBindViewHolder(holder: ClinicReservationViewHolder, position: Int)
    {
        val item = list!![position]
        holder.binding.run {
            tvPatientName.text = item.patient.name
            tvDate.text = item.date
            tvPhone.text = item.patient.phone
            tvAgeGenderName.text = if (item.patient.gender == Gender.MALE.value)
                context.getString(R.string.male)
            else context.getString(R.string.female)+", "+item.patient.birthdate

            Log.d("saif", "photo_--= ${item.patient.photo}")
            tvReservationNumber.text = "${context.getString(R.string.reservation_number)} : ${item.reservationNumber}"
            tvReservationType.text = DoctorReservationType.getType(item.type).name
            tvForMe.text = if (item.notes.isNullOrEmpty()) context.getString(R.string.for_me) else context.getString(R.string.for_other)

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

    fun setData(arrayList: ArrayList<Reservation>?)
    {
        this.list = arrayList
        notifyDataSetChanged()
    }

    fun removeItem(position: Int)
    {
        if (position == -1)
            return

        list?.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ClinicReservationViewHolder(val binding: DoctorReservationItemBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.root.setOnClickListener { itemClick(adapterPosition, list!![adapterPosition]) }
        }
    }
}