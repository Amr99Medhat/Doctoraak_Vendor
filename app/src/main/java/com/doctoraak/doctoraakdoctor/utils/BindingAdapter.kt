package com.doctoraak.doctoraakdoctor.utils

import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakdoctor.ui.PlacePickerDialog
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException
import java.util.*

@BindingAdapter("imageUrl", "imagePlaceHolder", "imageError", requireAll = false)
fun imageUrl(view: ImageView, url: String?, placeHolder: Drawable?=null, error: Drawable? = null)
{
    Glide.with(view.context).load(url).placeholder(placeHolder).error(error).into(view)
}

@BindingAdapter("latt", "lang", requireAll = true)
fun convertLatLongToAddress(view: View, latt: String, lang: String)
{
    if (TextUtils.isEmpty(latt) || TextUtils.isEmpty(lang))
        return

    try {
        val geocoder = Geocoder(view.context, Locale.getDefault())
        val addressList = geocoder.getFromLocation(latt.toDouble(), lang.toDouble(), 1)
        val builder = StringBuilder("")
        if (addressList != null && addressList.size > 0)
        {
            val address = addressList[0]

            for (i in 0 until address.maxAddressLineIndex) {
                builder.append(
                    if (TextUtils.isEmpty(address.getAddressLine(i)))
                        ""
                    else address.getAddressLine(i)
                )
            }
            builder.append(if (TextUtils.isEmpty(address.locality)) "" else address.locality + ", ")
                .append(if (TextUtils.isEmpty(address.subAdminArea)) "" else address.subAdminArea + ", ")
                .append(if (TextUtils.isEmpty(address.adminArea)) "" else address.adminArea + ", ")
                .append(if (TextUtils.isEmpty(address.countryName)) "" else address.countryName)
        }

        if (view is TextView)
            view.text = builder.toString()
        else if (view is EditText)
            view.setText(builder.toString())
        else if (view is TextInputEditText)
            view.setText(builder.toString())
    }catch (e: IOException)
    {
        Log.w("saif", "convertLatLongToAddress: ", e)
    }
}
