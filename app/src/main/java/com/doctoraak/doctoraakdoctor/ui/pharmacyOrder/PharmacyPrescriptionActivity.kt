package com.doctoraak.doctoraakdoctor.ui.pharmacyOrder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.ActivityDoctorReservationBinding
import com.doctoraak.doctoraakdoctor.databinding.ActivityPharmacyPrescriptionBinding
import com.doctoraak.doctoraakdoctor.model.PharmacyOrder
import com.doctoraak.doctoraakdoctor.model.Reservation
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.doctorReservation.DoctorReservationActivity
import com.google.gson.Gson

class PharmacyPrescriptionActivity : BaseActivity()
{
    companion object{
        internal const val ORDER_INTENT = "ORDER_INTENT"
        internal const val ACTION_INTENT = "ACTION_INTENT"
        internal const val SHOW_TOOL_BAR_INTENT = "SHOW_TOOL_BAR_INTENT"

        internal fun launch(context: Context, pharmacyOrder: PharmacyOrder, action: Int = 0)
                = with(Intent(context, PharmacyPrescriptionActivity::class.java))
        {
            putExtra(ORDER_INTENT, Gson().toJson(pharmacyOrder))
            putExtra(ACTION_INTENT, action)
            putExtra(SHOW_TOOL_BAR_INTENT, true)
            context.startActivity(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
            .setContentView<ActivityPharmacyPrescriptionBinding>(this, R.layout.activity_pharmacy_prescription)
    }


}
