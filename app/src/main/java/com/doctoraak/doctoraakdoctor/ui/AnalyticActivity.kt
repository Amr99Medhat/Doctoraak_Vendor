package com.doctoraak.doctoraakdoctor.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityAnalyticBinding
import com.doctoraak.doctoraakdoctor.utils.Language
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.utils.hide
import com.doctoraak.doctoraakdoctor.utils.show
import android.content.res.Configuration
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure


class AnalyticActivity : BaseActivity()
{
    private lateinit var binding: ActivityAnalyticBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_analytic)
        setToolBar(binding.toolBarLayout.toolBar)
        binding.toolBarLayout.toolBar.setBackgroundResource(R.color.ColorMainAccent_0)

        webView = WebView(createConfigurationContext(Configuration()))
        binding.frameWebView.addView(webView)

        handleWebView()
    }

    private fun getUrl(): String
    {
        val id = SessionManager.getUserId()
        val lang = if (SessionManager.getLanguage() == Language.ENGLISH.name)
            "en" else "ar"
        return when (SessionManager.getUserType())
        {
            UserType.DOCTOR -> "${ApiConfigure.BASE_URL_EXCEPT_API}mobile/doctor?doctor=$id&lang=$lang"
            UserType.PHARMACY -> "${ApiConfigure.BASE_URL_EXCEPT_API}mobile/pharmacy?pharmacy=$id&lang=$lang"
            UserType.LAB -> "${ApiConfigure.BASE_URL_EXCEPT_API}mobile/lab?lab=$id&lang=$lang"
            UserType.RADIOLOGY -> "${ApiConfigure.BASE_URL_EXCEPT_API}mobile/radiology?radiology=$id&lang=$lang"
            else -> ""
        }
    }

    private fun handleWebView()
    {
        with(webView)
        {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.loading.loadingView.show()
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.loading.loadingView.hide()
                }
            }
            loadUrl(this@AnalyticActivity.getUrl())
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item?.itemId)
        {
            android.R.id.home -> {
                finishAndBack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
