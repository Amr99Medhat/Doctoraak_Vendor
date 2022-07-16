package com.doctoraak.doctoraakdoctor.ui.doctorReservation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.ActivityDoctorReservationBinding
import com.doctoraak.doctoraakdoctor.model.Reservation
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.doctorHome.DoctorHomeActivity
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class DoctorReservationActivity : BaseActivity()
{

    companion object{
        internal const val RESERVATION_INTENT = "RESERVATION_INTENT"
        internal const val POSITION_INTENT = "POSITION_INTENT"
        internal const val SHOW_TOOL_BAR_INTENT = "SHOW_TOOL_BAR_INTENT"

        internal fun launch(activity: Activity, position: Int, reservation: Reservation)
                = with(Intent(activity, DoctorReservationActivity::class.java))
        {
            putExtra(RESERVATION_INTENT, Gson().toJson(reservation))
            putExtra(POSITION_INTENT, position)
            putExtra(SHOW_TOOL_BAR_INTENT, true)
            activity.startActivityForResult(this, DoctorHomeActivity.RESULT_ITEM_CLICK)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDoctorReservationBinding>(this, R.layout.activity_doctor_reservation)
    }


}
