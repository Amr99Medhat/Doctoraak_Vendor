package com.doctoraak.doctoraakdoctor.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.PharmacyPrescriptionItemBinding
import com.doctoraak.doctoraakdoctor.model.MedicineDetail

class PharmacyMedicineAdapter(val context: Context, var list: List<MedicineDetail>?)
    : RecyclerView.Adapter<PharmacyMedicineAdapter.PharmacyMedicineViewHolder>()
{
    private val anim_right by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right)}
    private val anim_left by lazy { AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left)}
    private var is_left_anim = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyMedicineViewHolder =
        PharmacyMedicineViewHolder(DataBindingUtil.inflate(LayoutInflater
                .from(parent.context), R.layout.pharmacy_prescription_item, parent, false))

    override fun getItemCount(): Int = list?.size ?: 0

    // todo i ignored language;
    override fun onBindViewHolder(holder: PharmacyMedicineViewHolder, position: Int)
    {
        val item = list!![position]
        holder.binding.medicine = item
        Log.d("saif", "item__= $item")

        animateItem(holder.binding.root)
    }

    private fun animateItem(view: View)
    {
        view.startAnimation(if (is_left_anim) anim_left else anim_right)
        is_left_anim = !is_left_anim
    }

    inner class PharmacyMedicineViewHolder(val binding: PharmacyPrescriptionItemBinding)
        : RecyclerView.ViewHolder(binding.root)
}