package com.doctoraak.doctoraakdoctor.ui.radiologyOrder

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.ActivityLabOrderBinding
import com.doctoraak.doctoraakdoctor.databinding.ActivityRadiologyOrderBinding
import com.doctoraak.doctoraakdoctor.model.LabOrder
import com.doctoraak.doctoraakdoctor.model.RadiologyOrder
import com.doctoraak.doctoraakdoctor.ui.BaseHome
import com.doctoraak.doctoraakdoctor.ui.labOrder.LabOrderActivity
import com.doctoraak.doctoraakdoctor.ui.radiologyHome.RadiologyHomeActivity
import com.google.gson.Gson

class RadiologyOrderActivity : BaseHome()
{
    companion object
    {
        internal const val ORDER_INTENT = "ORDER_INTENT"
        internal const val ACTION_INTENT = "ACTION_INTENT"
        internal const val SHOW_TOOL_BAR_INTENT = "SHOW_TOOL_BAR_INTENT"

        internal fun launch(activity: Activity, radiologyOrder: RadiologyOrder, action: Int = 0)
                = with(Intent(activity, RadiologyOrderActivity::class.java))
        {
            putExtra(ORDER_INTENT, Gson().toJson(radiologyOrder))
            putExtra(ACTION_INTENT, action)
            putExtra(SHOW_TOOL_BAR_INTENT, true)
            activity.startActivityForResult(this, RadiologyHomeActivity.RESULT_ITEM_CLICK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
            .setContentView<ActivityRadiologyOrderBinding>(this, R.layout.activity_radiology_order)
    }


}
