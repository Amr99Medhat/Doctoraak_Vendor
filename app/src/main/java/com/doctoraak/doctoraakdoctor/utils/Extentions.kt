package com.doctoraak.doctoraakdoctor.utils

import android.animation.Animator
import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.transition.*
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.InternetConnection
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseCallBack
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.Degree
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import java.io.File
import java.util.*

fun Int?.valueOfDoctorReservationType(): String {
    if (this == null)
        return ""

    val values = DoctorReservationType.values()
    for (i in values.indices.reversed()) {
        val value = values[i]
        if (this == value.value)
            return value.name.toLowerCase()
    }

    return ""
}

fun Pair<Int, String?>.showError(context: Context) {
    if (second.isNullOrEmpty())
        Utils.showError(context, msg = context.getString(first))
    else
        Utils.showError(context, msg = second!!, title = context.getString(first))
}

fun String?.convertToHours12(calendar: Calendar): String =
    if (this == null || this == Constants.TIME_DEFAULT)
        "00:00"
    else {
        val list = this.split(":")
        Utils.convertToHours12(calendar, list[0].toInt(), list[1].toInt())
    }

fun View.animateOutInX(view_in: View, duration: Long = 400, translate_x: Float = 350f) {
    animate()
        .setDuration(duration)
        .translationX(-translate_x)
        .alpha(0f)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                visibility = GONE
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
        .start()

    with(view_in)
    {
        translationX = translate_x
        visibility = VISIBLE
        alpha = 0f
        animate()
            .setDuration(duration)
            .setStartDelay(100)
            .translationX(0f)
            .alpha(1f)
            .start()
    }
}

fun ViewGroup.animateExplodeOutChildren(startDelay: Long = 0) {
    val right_top = AnimationUtils.loadAnimation(context, R.anim.explode_out_right_top)
        .apply { startOffset = startDelay }
    val right_bottom = AnimationUtils.loadAnimation(context, R.anim.explode_out_right_bottom)
        .apply { startOffset = startDelay }
    val left_top = AnimationUtils.loadAnimation(context, R.anim.explode_out_left_top)
        .apply { startOffset = startDelay }
    val left_bottom = AnimationUtils.loadAnimation(context, R.anim.explode_out_left_bottom)
        .apply { startOffset = startDelay }
    val bottom = AnimationUtils.loadAnimation(context, R.anim.explode_out_bottom)
        .apply { startOffset = startDelay }
    val top = AnimationUtils.loadAnimation(context, R.anim.explode_out_top)
        .apply { startOffset = startDelay }

    children.forEach {
        if (it.x == width / 2f && it.y < height / 2)
            it.startAnimation(top)
        else if (it.x == width / 2f && it.y > height / 2)
            it.startAnimation(bottom)
        else if (it.x < width / 2 && it.y < height / 2)
            it.startAnimation(left_top)
        else if (it.x > width / 2 && it.y < height / 2)
            it.startAnimation(right_top)
        else if (it.x < width / 2 && it.y >= height / 2)
            it.startAnimation(left_bottom)
        else if (it.x > width / 2 && it.y >= height / 2)
            it.startAnimation(right_bottom)
    }
}

fun ViewGroup.animateExplodeInChildren(startDelay: Long = 0) {
    val right_top = AnimationUtils.loadAnimation(context, R.anim.explode_in_right_top)
        .apply { startOffset = startDelay }
    val right_bottom = AnimationUtils.loadAnimation(context, R.anim.explode_in_right_bottom)
        .apply { startOffset = startDelay }
    val left_top = AnimationUtils.loadAnimation(context, R.anim.explode_in_left_top)
        .apply { startOffset = startDelay }
    val left_bottom = AnimationUtils.loadAnimation(context, R.anim.explode_in_left_bottom)
        .apply { startOffset = startDelay }
    val bottom = AnimationUtils.loadAnimation(context, R.anim.explode_in_bottom)
        .apply { startOffset = startDelay }
    val top = AnimationUtils.loadAnimation(context, R.anim.explode_in_top)
        .apply { startOffset = startDelay }

    children.forEach {
        if (it.x == width / 2f && it.y < height / 2)
            it.startAnimation(top)
        else if (it.x == width / 2f && it.y > height / 2)
            it.startAnimation(bottom)
        else if (it.x < width / 2 && it.y < height / 2)
            it.startAnimation(left_top)
        else if (it.x > width / 2 && it.y < height / 2)
            it.startAnimation(right_top)
        else if (it.x < width / 2 && it.y >= height / 2)
            it.startAnimation(left_bottom)
        else if (it.x > width / 2 && it.y >= height / 2)
            it.startAnimation(right_bottom)
    }
}

fun ViewGroup.animateSlideOutInWithOrder(
    viewGroupIn: ViewGroup,
    startDelay: Long = 200,
    duration: Long = 400
) {
    animateSlideOutWithOrder(_duration = duration)
    viewGroupIn.animateSlideInWithOrder(startDelay, duration)
}

fun ViewGroup.animateSlideOutInWithOrderReverse(
    viewGroupIn: ViewGroup,
    startDelay: Long = 200,
    duration: Long = 400
) {
    animateSlideOutWithOrderReverse(_duration = duration)
    viewGroupIn.animateSlideInWithOrderReverse(startDelay, duration)
}

fun ViewGroup.animateSlideInWithOrderReverse(startDelay: Long = 0, _duration: Long = 400) {
    var delay: Long = startDelay
    show()
    children.forEach {
        with(AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left))
        {
            duration = _duration
            startOffset = delay
            delay += 100

            it.startAnimation(this)
        }
    }
}

fun ViewGroup.animateScaleInWithOrder(startDelay: Long = 0, _duration: Long = 400) {
    var delay: Long = startDelay
    show()
    children.forEach {
        with(AnimationUtils.loadAnimation(context, R.anim.scale_in_from_left))
        {
            duration = _duration
            startOffset = delay
            delay += 100

            it.startAnimation(this)
        }
    }
}

fun ViewGroup.animateSlideOutWithOrderReverse(startDelay: Long = 0, _duration: Long = 400) {
    var delay: Long = startDelay

    children.forEachIndexed { index, view ->
        with(AnimationUtils.loadAnimation(context, R.anim.slide_out_to_right))
        {
            duration = _duration
            startOffset = delay
            delay += 100

            view.startAnimation(this)
            if (index == childCount - 1)
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        hide()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationStart(animation: Animation?) {}
                })
        }
    }
}

fun ViewGroup.animateSlideInWithOrder(startDelay: Long = 0, _duration: Long = 400) {
    var delay: Long = startDelay
    show()
    children.forEach {
        with(AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right))
        {
            duration = _duration
            startOffset = delay
            delay += 100

            it.startAnimation(this)
        }
    }
}

fun ViewGroup.animateSlideOutWithOrder(startDelay: Long = 0, _duration: Long = 400) {
    var delay: Long = startDelay

    children.forEachIndexed { index, view ->
        with(AnimationUtils.loadAnimation(context, R.anim.slide_out_to_left))
        {
            duration = _duration
            startOffset = delay
            delay += 100

            view.startAnimation(this)
            if (index == childCount - 1)
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        hide()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationStart(animation: Animation?) {}
                })
        }
    }
}

fun String.validatePhone(textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this))
        textInputLayout.error = textInputLayout.context.getString(R.string.enter_your_) + " " +
                textInputLayout.context.getString(R.string.phone_number)
//    else if (length != 11)
//        textInputLayout.error = textInputLayout.context.getString(R.string.phone_must_be_equal_11)
    else {
        textInputLayout.error = null
        return true
    }

    return false
}

fun String.validateEmpty(
    secondVal: String,
    textInputLayout: TextInputLayout,
    errorText: String
): Boolean {
    if (TextUtils.isEmpty(this) || TextUtils.isEmpty(secondVal))
        textInputLayout.error = errorText
    else {
        textInputLayout.error = null
        return true
    }

    return false
}

fun String.validateFilePath(
    textInputLayout: TextInputLayout
    , empty_string_error: String = textInputLayout.context.getString(R.string.pick_a_file)
): Boolean {
    if (TextUtils.isEmpty(this))
        textInputLayout.error = empty_string_error
    else if (!File(this).exists())
        textInputLayout.error = textInputLayout.context.getString(R.string.file_not_found)
    else {
        textInputLayout.error = null
        return true
    }

    return false
}

fun Int.validateId(
    textInputLayout: TextInputLayout,
    empty_string_error: String,
    nullValue: Int = -1
): Boolean {
    if (this == nullValue)
        textInputLayout.error = empty_string_error
    else {
        textInputLayout.error = null
        return true
    }

    return false
}

fun String.validateImagePath(textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this))
        textInputLayout.error = textInputLayout.context.getString(R.string.pick_an_image)
    else if (!File(this).exists())
        textInputLayout.error = textInputLayout.context.getString(R.string.image_not_found)
    else {
        textInputLayout.error = null
        return true
    }

    return false
}

fun String.validateImagePath(context: Context): String? {
    if (!File(this).exists())
        return context.getString(R.string.image_not_found)
    else
        return null
}

fun String.validatePassword(textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this))
        textInputLayout.error = textInputLayout.context.getString(R.string.enter_your_) + " " +
                textInputLayout.context.getString(R.string.password)
    else if (length < 6)
        textInputLayout.error =
            textInputLayout.context.getString(R.string.password_must_be_larger_7)
    else {
        textInputLayout.error = null
        return true
    }

    return false
}

fun String.validatePasswordAndConfirmPassword(
    textInputLayout: TextInputLayout
    , confirm_password: String, textInputLayoutConfirm: TextInputLayout
): Boolean {
    if (TextUtils.isEmpty(this))
        textInputLayout.error = textInputLayout.context.getString(R.string.enter_your_) + " " +
                textInputLayout.context.getString(R.string.password)
    else if (length < 6)
        textInputLayout.error =
            textInputLayout.context.getString(R.string.password_must_be_larger_7)
    else {
        textInputLayout.error = null

        if (TextUtils.isEmpty(confirm_password))
            textInputLayoutConfirm.error =
                textInputLayout.context.getString(R.string.enter_your_) + " " +
                        textInputLayout.context.getString(R.string.confirm_password)
        if (!TextUtils.equals(this, confirm_password))
            textInputLayoutConfirm.error =
                textInputLayout.context.getString(R.string.password_fields_didt_match)
        else {
            textInputLayoutConfirm.error = null
            return true
        }
    }

    return false
}

fun String.validateVerificationCode(textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = textInputLayout.context.getString(R.string.enter_your_) + " " +
                textInputLayout.context.getString(R.string.verification_code)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateEmpty(
    textInputLayout: TextInputLayout
    , errorText: String = textInputLayout.context.getString(R.string.empty_field)
): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = errorText
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateEmpty(showingInView: View, errorText: Int = R.string.empty_field
): Boolean {
    if (TextUtils.isEmpty(this)) {
        Snackbar.make(showingInView, errorText, Snackbar.LENGTH_SHORT).show()
        return false
    } else
        return true
}

fun String.validateEmptyAndNotZero(
    textInputLayout: TextInputLayout
    , errorText: String = textInputLayout.context.getString(R.string.empty_field)
): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = errorText
        return false
    } else if (this.toInt() == 0) {
        textInputLayout.error = textInputLayout.context.getString(R.string.value_equal_zero)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateEmptyAndNotZeroFloat(
    textInputLayout: TextInputLayout
    , errorText: String = textInputLayout.context.getString(R.string.empty_field)
): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = errorText
        return false
    } else if (this.toFloat() == 0f) {
        textInputLayout.error = textInputLayout.context.getString(R.string.value_equal_zero)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun CheckBox.validateChecked(): Boolean {
    if (!isChecked) {
        setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))
        return false
    } else {
        setTextColor(ResourcesCompat.getColor(resources, R.color.gray_3, null))
        return true
    }
}

fun String.validateUsername(textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = textInputLayout.context.getString(R.string.enter_your_) +
                " " + textInputLayout.context.getString(R.string.full_name)
        return false
    } else {
        textInputLayout.error = null
        return true
    }
}

fun String.validateEmail(textInputLayout: TextInputLayout): Boolean {
    if (TextUtils.isEmpty(this)) {
        textInputLayout.error = textInputLayout.context.getString(R.string.enter_your_) +
                " " + textInputLayout.context.getString(R.string.email)
        return false
    } else
        return true
//        if (!Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
//        textInputLayout.error = textInputLayout.context.getString(R.string.email) + " " +
//                textInputLayout.context.getString(R.string.is_not_valid)
//        return false
//    } else {
//        textInputLayout.error = null
//        return true
//    }
}

fun View.show() {
    this.visibility = VISIBLE
}

fun View.hide() {
    this.visibility = GONE
}

fun View.invisible() {
    this.visibility = INVISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

fun <T> Call<T>.start(listener: BaseResponseListener<T>) {
    listener.onLoading()
    if (InternetConnection.checkInternetConnection())
        this.enqueue(BaseCallBack(listener))
    else
        listener.onError(R.string.no_internet_connection, null)
}

fun checkInternetConnection(context: Context): Boolean {
    val manager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo != null && manager.activeNetworkInfo!!.isConnected
}
