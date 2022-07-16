package com.doctoraak.doctoraakdoctor.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.ui.notification.NotificationActivity
import com.doctoraak.doctoraakdoctor.utils.Constants
import com.doctoraak.doctoraakdoctor.utils.Language
import com.doctoraak.doctoraakdoctor.utils.UserType
import java.lang.StringBuilder
import java.util.*

open class BaseActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        handleLanguage()
    }

    protected fun handleLanguage()
    {
        val config = Configuration()

        val locale = if (SessionManager.getLanguage() == Language.ARABIC.name)
            Locale("ar")
        else
            Locale.ENGLISH

        Locale.setDefault(locale)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }

    protected fun getLabInsuranceNames(list: List<InsuranceLab>): String
    {
        if (list.isEmpty()) return ""

        val insurances = SessionManager.getInsurance()
        val names = StringBuilder()
        list.forEach { item->
            insurances.find { it.id == item.insurance_id}?.let {
                names.append(it.name)
                    .append(" - ")
            }
        }

        if (names.isNotEmpty())
        {
            names.trim()
            names.deleteCharAt(names.length-2)
        }

        return names.toString()
    }

    protected fun getLabInsuranceIds(list: List<InsuranceLab>): String
    {
        if (list.isEmpty()) return ""

        val insurances = SessionManager.getInsurance()
        val ids = StringBuilder("[")
        list.forEach { item->
            insurances.find { it.id == item.insurance_id}?.let {
                ids.append(it.id)
                    .append(",")
            }
        }

        if (ids.isNotEmpty())
        {
            ids.trim()
            ids.deleteCharAt(ids.length-1)
            ids.append("]")
        }

        return ids.toString()
    }

    protected fun getRadiologyInsuranceNames(list: List<InsuranceRadiology>): String
    {
        if (list.isEmpty()) return ""

        val insurances = SessionManager.getInsurance()
        val names = StringBuilder()
        list.forEach { item->
            insurances.find { it.id == item.insurance_id}?.let {
                names.append(it.name)
                    .append(" - ")
            }
        }

        if (names.isNotEmpty())
        {
            names.trim()
            names.deleteCharAt(names.length-2)
        }

        return names.toString()
    }

    protected fun getRadiologyInsuranceIds(list: List<InsuranceRadiology>): String
    {
        if (list.isEmpty()) return ""

        val insurances = SessionManager.getInsurance()
        val ids = StringBuilder()
        ids.append("[")

        list.forEach { item->
            insurances.find { it.id == item.insurance_id}?.let {
                ids.append(it.id)
                    .append(",")
            }
        }

        if (ids.isNotEmpty())
        {
            ids.trim()
            ids.deleteCharAt(ids.length-1)
            ids.append("]")
        }

        return ids.toString()
    }

    protected fun getDoctorInsuranceNames(list: List<InsuranceDoctor>): String
    {
        if (list.isEmpty()) return ""

        val insurances = SessionManager.getInsurance()
        val names = StringBuilder()
        list.forEach { item->
            insurances.find { it.id == item.insurance_id}?.let {
                names.append(it.name)
                    .append(" - ")
            }
        }

        if (names.isNotEmpty())
        {
            names.trim()
            names.deleteCharAt(names.length-2)
        }

        return names.toString()
    }

    protected fun getDoctorInsuranceIds(list: List<InsuranceDoctor>): String
    {
        if (list.isEmpty()) return ""

        val insurances = SessionManager.getInsurance()
        val ids = StringBuilder("[")
        list.forEach { item->
            insurances.find { it.id == item.insurance_id}?.let {
                ids.append(it.id)
                    .append(",")
            }
        }

        if (ids.isNotEmpty())
        {
            ids.trim()
            ids.deleteCharAt(ids.length-1)
            ids.append("]")
        }

        return ids.toString()
    }

    protected fun getPharmacyInsuranceNames(list: List<InsurancePharmacy>): String
    {
        if (list.isEmpty()) return ""

        val insurances = SessionManager.getInsurance()
        val names = StringBuilder("")
        list.forEach { item->
            insurances.find { it.id == item.insurance_id}?.let {
                names.append(it.name)
                    .append(" - ")
            }
        }

        if (names.isNotEmpty())
        {
            names.trim()
            names.deleteCharAt(names.length-2)
        }

        return names.toString().trim()
    }

    protected fun getPharmacyInsuranceIds(list: List<InsurancePharmacy>): String
    {
        if (list.isEmpty()) return ""

        val insurances = SessionManager.getInsurance()
        val ids = StringBuilder("[")
        list.forEach { item->
            insurances.find { it.id == item.insurance_id}?.let {
                ids.append(it.id)
                    .append(",")
            }
        }

        if (ids.isNotEmpty())
        {
            ids.trim()
            ids.deleteCharAt(ids.length-1)
            ids.append("]")
        }

        return ids.toString().trim()
    }

    protected fun setToolBar(toolbar: Toolbar?) =
        Companion.setToolBar(this, toolbar)

    companion object {
        fun setToolBar(activity: AppCompatActivity, toolbar: Toolbar?)
        {
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.let {
                it.setDisplayShowTitleEnabled(false)
                it.setDisplayHomeAsUpEnabled(true)
            }

            toolBarInsuranceImage(toolbar)
            toolBarClicks(activity, toolbar)
        }

        fun toolBarInsuranceImage(toolbar: Toolbar?)
        {
            toolbar?.findViewById<ImageView>(R.id.iv_insurance)?.let { image->
                Log.d("saif", "in the image iv_insurance "+
                        SessionManager.getDoctorData()?.insuranceCompany.toString())

                Glide.with(image.context).load(R.drawable.ic_one_care_logo).override(132).into(image)

                // TODO this code is commented temporary until know what to do with how to handle insurance company;
//                when (SessionManager.getUserType())
//                {
//                    UserType.DOCTOR ->
//                    {
//                        SessionManager.getDoctorData()?.insuranceCompany?.let { list ->
//                            list.find { it.id == Constants.ONE_CARE_MEDICAL_INSURANCE_ID }?.let {
//                                Glide.with(image.context).load(it.url).override(132).into(image)
//                            } ?: run {
//                                Glide.with(image.context).load(list.getOrNull(0)?.url).override(132).into(image)
//                            }
//                        }
//                    }
//                    UserType.PHARMACY ->
//                    {
//                        SessionManager.getPharmacyData()?.insuranceCompany?.let { list ->
//                            list.find { it.id == Constants.ONE_CARE_MEDICAL_INSURANCE_ID }?.let {
//                                Glide.with(image.context).load(it.url).override(132).into(image)
//                            } ?: run {
//                                Glide.with(image.context).load(list.getOrNull(0)?.url).override(132).into(image)
//                            }
//                        }
//                    }
//                    UserType.LAB ->
//                    {
//                        SessionManager.getLabData()?.insuranceCompany?.let { list ->
//                            list.find { it.id == Constants.ONE_CARE_MEDICAL_INSURANCE_ID }?.let {
//                                Glide.with(image.context).load(it.url).override(132).into(image)
//                            } ?: run {
//                                Glide.with(image.context).load(list.getOrNull(0)?.url).override(132).into(image)
//                            }
//                        }
//                    }
//                    UserType.RADIOLOGY ->
//                    {
//                        SessionManager.getRadiologyData()?.insuranceCompany?.let { list ->
//                            list.find { it.id == Constants.ONE_CARE_MEDICAL_INSURANCE_ID }?.let {
//                                Glide.with(image.context).load(it.url).override(132).into(image)
//                            } ?: run {
//                                Glide.with(image.context).load(list.getOrNull(0)?.url).override(132).into(image)
//                            }
//                        }
//                    }
//                    else -> { }
//                }
            }
        }

        fun toolBarClicks(activity: Activity, toolbar: Toolbar?)
        {
            toolbar?.findViewById<View>(R.id.view_group_notification)?.let {
                it.setOnClickListener {
                    activity.startActivity(Intent(activity, NotificationActivity::class.java))
                }
            }
        }
    }



    protected fun toast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_SHORT)
            .run {
                show()
            }

    protected fun toast(textId: Int) = toast(getString(textId))

    protected fun alertDialog(title: String, msg: String, positiveText: String? = null
                                  , positiveClickListener: ((dialog: DialogInterface?, which: Int) -> Unit)? = null
                                  , negativeText: String? = null
                                  , negativeClickListener: ((dialog: DialogInterface?, which: Int) -> Unit)? = null
                                  , cancable: Boolean = true) = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(msg)
        .setCancelable(cancable)
        .setPositiveButton(positiveText, positiveClickListener)
        .setNegativeButton(negativeText, negativeClickListener)
        .show()

    protected fun alertDialogMultiChoice(title: String, list: Array<String>
                                         , itemsSelected: BooleanArray = BooleanArray(list.size){i-> false}
                                         , choiceListener: (dialog: DialogInterface?, which: Int, isChecked: Boolean)->Unit
                                         , positiveText: String? = null
                                  , positiveClickListener: ((dialog: DialogInterface?, which: Int) -> Unit)? = null
                                  , negativeText: String? = null
                                  , negativeClickListener: ((dialog: DialogInterface?, which: Int) -> Unit)? = null
                                  , cancable: Boolean = true)
            = AlertDialog.Builder(this)
        .setTitle(title)
        .setCancelable(cancable)
        .setMultiChoiceItems(list, itemsSelected, choiceListener)
        .setPositiveButton(positiveText, positiveClickListener)
        .setNegativeButton(negativeText, negativeClickListener)
        .show()

    protected fun getInsuranceListIds(selected: String?): List<String>?
    {
        if (selected.isNullOrBlank() || selected.isBlank())
            return null

        return StringBuilder(selected)
            .deleteCharAt(selected.length - 1)
            .deleteCharAt(0)
            .toString()
            .split(",")
    }

    protected fun getSelectedInsuranceItems(selected: String?, selectedList: SparseIntArray
                                          , listSize: Int, insurance: List<Insurance>): BooleanArray
    {
        var itemSelected = BooleanArray(listSize){ false }

        with (getInsuranceListIds(selected))
        {
            if (this != null)
            {
                itemSelected = BooleanArray(listSize){ i ->
                    val isSelected = this.find { it.trim() == insurance[i].id.toString() } != null

                    if (isSelected)
                        selectedList.put(i, insurance[i].id)

                    isSelected
                }
            }
        }

        return itemSelected
    }

    protected fun requestReadExternalPermission(functionAfterPermission: ()->Unit, PERMISSION_READ_EXTERNAL_STORAGE_CODE: Int)
    {
        val requestReadExternalPermission = {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE )
                ,  PERMISSION_READ_EXTERNAL_STORAGE_CODE)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE))
                alertDialog(getString(R.string.request_permission), getString(R.string.read_external_storage_permission_msg)
                    , getString(R.string.ok), { dialog, _ ->  dialog?.dismiss(); requestReadExternalPermission()}
                    , getString(R.string.cancel))
            else
                requestReadExternalPermission()
        else
            functionAfterPermission()
    }

    @SuppressLint("Range")
    protected fun getPathFromUri(uri: Uri, projection: Array<String>): String?
    {
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            cursor.moveToFirst()
            val path = cursor.getString(cursor.getColumnIndex(projection[0]))
            cursor.close()
            return path
        }
        return null
    }

    protected fun showError(msg: String)
    {
        SweetDialog.newInstance(this, DialogType.ERROR).apply {
            show()
            setMessage(msg)
        }
    }

    fun showWarning(context: Context, msg: String, cancelable: Boolean = true, okClick: ()->Unit) =
        SweetDialog.newInstance(context, DialogType.WARNING).apply {
            show()
            setCancelable(cancelable)
            setMessage(msg)
            setOkClickListener {
                okClick()
                it.dismiss()
            }
        }


    protected fun finishAndBack()
    {
        finish()
        onBackPressed()
    }

    fun handleRefreshWithMotionLayouts(refreshLayout: SwipeRefreshLayout, motion_layout: MotionLayout)
    {
        val attributes = obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val dimension = attributes.getDimensionPixelSize(0, 0)
        attributes.recycle()
        refreshLayout.setProgressViewOffset(true, dimension, dimension * 2)

        motion_layout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, i: Int, i1: Int) {}
            override fun onTransitionChange(motionLayout: MotionLayout, i: Int, i1: Int, v: Float) {
                if (v == 0f) {
                    refreshLayout.setEnabled(true)
                } else {
                    refreshLayout.setEnabled(false)
                    refreshLayout.setRefreshing(false)
                }
            }
            override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {}
            override fun onTransitionTrigger(motionLayout: MotionLayout, i: Int, b: Boolean, v: Float) {}
        })
    }



}