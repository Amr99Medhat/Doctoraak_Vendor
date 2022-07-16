package com.doctoraak.doctoraakdoctor.ui

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.LanguageDialogBinding
import com.doctoraak.doctoraakdoctor.utils.Language
import java.util.*

class LanguageDialog(context: Context, val okClick: ()->Unit) : Dialog(context)
{
    companion object{
        @JvmStatic
        fun newInstance(context: Context, okClick: ()->Unit) =
            LanguageDialog(context, {okClick()})
    }

    private lateinit var binding: LanguageDialogBinding

    private var selectedLanguage = Language.ENGLISH

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context)
            , R.layout.language_dialog, null, false)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener { changeLanguageClick() }
        binding.tvArabic.setOnClickListener {
            selectArabic()
        }
        binding.tvEnglish.setOnClickListener {
            selectEnglish()
        }

        if (Language.valueOf(SessionManager.getLanguage()) == Language.ENGLISH)
            selectEnglish()
        else
            selectArabic()
    }

    private fun selectEnglish()
    {
        selectedLanguage = Language.ENGLISH
        binding.tvEnglish.setBackgroundResource(R.drawable.rect_back_gray_2)
        binding.tvArabic.setBackgroundResource(R.drawable.rect_corner_border)
    }

    private fun selectArabic()
    {
        selectedLanguage = Language.ARABIC
        binding.tvArabic.setBackgroundResource(R.drawable.rect_back_gray_2)
        binding.tvEnglish.setBackgroundResource(R.drawable.rect_corner_border)
    }


    override fun onStart()
    {
        super.onStart()
        window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setGravity(Gravity.CENTER)
            it.setBackgroundDrawableResource(R.color.transparent)
            it.setWindowAnimations(R.style.dialogAnimation)
        }
    }

    private fun changeLanguageClick()
    {
        val config = Configuration()

        if (selectedLanguage == Language.ARABIC)
        {
            val locale = Locale("ar")
            Locale.setDefault(locale)
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            SessionManager.saveLanguage(Language.ARABIC)
        }
        else if (selectedLanguage == Language.ENGLISH)
        {
            val locale = Locale.ENGLISH
            Locale.setDefault(locale)
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            SessionManager.saveLanguage(Language.ENGLISH)
        }

//        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        dismiss()
        okClick()
    }



}
