package com.doctoraak.doctoraakdoctor.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.text.Spannable
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.model.ShiftWorkingHour
import com.doctoraak.doctoraakdoctor.model.WorkingHour
import com.doctoraak.doctoraakdoctor.ui.DialogType
import com.doctoraak.doctoraakdoctor.ui.SweetDialog
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder
import java.util.*

object Utils
{

    fun convertDpToPixel(context: Context, dp: Float): Float =
        dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

    fun convertPixelsToDp(context: Context, px: Float): Float =
        px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

    fun checkInternetConnection(context: Context): Boolean {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo != null && manager.activeNetworkInfo!!.isConnected
    }

    fun convertDrawableToBytes(drawable: Drawable): ByteArray? {
        if (drawable !is BitmapDrawable) return null

        val bitmap = drawable.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun convertBytesToDrawable(res: Resources, bytes: ByteArray, size: Int): Drawable {
        val options = BitmapFactory.Options()
        if (size != -1) {
            options.inJustDecodeBounds = false
            options.outWidth = size
            options.outHeight = size
        }
        return BitmapDrawable(res, BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options))
    }

    fun showError(context: Context, msg: String, cancelable: Boolean = true
                  , title: String = context.getString(R.string.error))
    {
        SweetDialog.newInstance(context, DialogType.ERROR).apply {
            show()
            setCancelable(cancelable)
            setMessage(msg)
            setTitle(title)
        }
    }

    fun showSuccess(context: Context, msg: String, cancelable: Boolean = true)
    {
        SweetDialog.newInstance(context, DialogType.SUCCESS).apply {
            show()
            setCancelable(cancelable)
            setMessage(msg)
        }
    }

    fun showWarning(context: Context, msg: String, cancelable: Boolean = true, okClick: ()->Unit)
    {
        SweetDialog.newInstance(context, DialogType.WARNING).apply {
            show()
            setCancelable(cancelable)
            setMessage(msg)
            setOkClickListener {
                okClick()
                it.dismiss()
            }
        }
    }

    fun getWorkingDays(context: Context, workingHours: List<WorkingHour>): String
    {
        if (workingHours.isEmpty())
            return context.getString(R.string.no_available_days)

        val daysOfWeek = context.resources.getStringArray(R.array.days_of_week)
        val workingDays = StringBuilder("")
        workingHours.filter { it.active == 1 }.forEach {
            when (it.day)
            {
                1-> workingDays.append(daysOfWeek[0]).append(" - ")
                2-> workingDays.append(daysOfWeek[1]).append(" - ")
                3-> workingDays.append(daysOfWeek[2]).append(" - ")
                4-> workingDays.append(daysOfWeek[3]).append(" - ")
                5-> workingDays.append(daysOfWeek[4]).append(" - ")
                6-> workingDays.append(daysOfWeek[5]).append(" - ")
                7-> workingDays.append(daysOfWeek[6]).append(" - ")
            }
        }

        return if (workingDays.isNotEmpty()) {
            workingDays.trim()
            workingDays.deleteCharAt(workingDays.length-2)
            workingDays.trim().toString()
        } else
            context.getString(R.string.no_available_days)
    }

    fun getShiftWorkingDays(context: Context, shiftWorkingHours: List<ShiftWorkingHour>): String
    {
        if (shiftWorkingHours.isEmpty())
            return context.getString(R.string.no_available_days)

        val daysOfWeek = context.resources.getStringArray(R.array.days_of_week)
        val workingDays = StringBuilder()
        shiftWorkingHours.filter { it.active == 1 }.forEach {
            when (it.day)
            {
                1-> workingDays.append(daysOfWeek[0]).append(" - ")
                2-> workingDays.append(daysOfWeek[1]).append(" - ")
                3-> workingDays.append(daysOfWeek[2]).append(" - ")
                4-> workingDays.append(daysOfWeek[3]).append(" - ")
                5-> workingDays.append(daysOfWeek[4]).append(" - ")
                6-> workingDays.append(daysOfWeek[5]).append(" - ")
                7-> workingDays.append(daysOfWeek[6]).append(" - ")
            }
        }

        return if (workingDays.isNotEmpty()) {
            workingDays.trim()
            workingDays.deleteCharAt(workingDays.length-2)
            workingDays.trim().toString()
        } else
            context.getString(R.string.no_available_days)
    }


    fun getTextForAppLanguage(enText: String?, arText: String?, frText: String? = null): String {
        val text = when (getAppLanguage())
        {
            "ar" -> arText
            "en" -> enText
            else -> enText
        }.toString()

        return if (text.isNullOrEmpty())
            enText ?: ""
        else
            text
    }

    fun getAppLanguage() = Locale.getDefault().language

    fun convertToHours12(calendar: Calendar, hourOfDay: Int, min: Int): String
    {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        val hour = if (calendar.get(Calendar.HOUR) == 0) "12" else calendar.get(Calendar.HOUR).toString()
        val am_pm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
        return "$hour:$min $am_pm"
    }


}
