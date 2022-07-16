package com.doctoraak.doctoraakdoctor.ui.labOrder

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.ActivityLabOrderBinding
import com.doctoraak.doctoraakdoctor.databinding.ActivityPharmacyPrescriptionBinding
import com.doctoraak.doctoraakdoctor.model.LabOrder
import com.doctoraak.doctoraakdoctor.model.PharmacyOrder
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.labHome.LabHomeActivity
import com.doctoraak.doctoraakdoctor.ui.pharmacyOrder.PharmacyPrescriptionActivity
import com.google.gson.Gson

class LabOrderActivity : BaseActivity()
{
    companion object{
        internal const val ORDER_INTENT = "ORDER_INTENT"
        internal const val ACTION_INTENT = "ACTION_INTENT"
        internal const val SHOW_TOOL_BAR_INTENT = "SHOW_TOOL_BAR_INTENT"

        internal fun launch(activity: Activity, labOrder: LabOrder, action: Int = 0)
                = with(Intent(activity, LabOrderActivity::class.java))
        {
            putExtra(ORDER_INTENT, Gson().toJson(labOrder))
            putExtra(ACTION_INTENT, action)
            putExtra(SHOW_TOOL_BAR_INTENT, true)
            activity.startActivityForResult(this, LabHomeActivity.RESULT_ITEM_CLICK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
            .setContentView<ActivityLabOrderBinding>(this, R.layout.activity_lab_order)

    }



}
