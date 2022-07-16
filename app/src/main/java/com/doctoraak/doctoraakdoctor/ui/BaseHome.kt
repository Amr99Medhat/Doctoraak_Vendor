package com.doctoraak.doctoraakdoctor.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiCredential
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.FirebaseTokenResponse
import com.doctoraak.doctoraakdoctor.ui.splash.SplashActivity
import com.google.firebase.iid.FirebaseInstanceId

open class BaseHome : BaseActivity()
{
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout


    protected fun logOut()
    {
        showWarning(this, getString(R.string.are_you_sure_you_want_to_log_out), okClick = {
            SessionManager.logOut()
            startActivity(Intent(this, UserTypeActivity::class.java))
            finish()
        }).positiveTextButton(getString(R.string.log_out))
    }

    protected fun changeLanguage()
    {
        LanguageDialog.newInstance(this) {
            finishAffinity()
            startActivity(Intent(this, SplashActivity::class.java))
        }.show()
    }

    protected fun contactUs()
    {
        ContactDialog.newInstance(this, SessionManager.getContact())
            .show()
    }

    protected fun setToolBarNav(toolbar: Toolbar, drawerLayout: DrawerLayout)
    {
        this.drawerLayout = drawerLayout

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowHomeEnabled(true)
        }

        toolBarInsuranceImage(toolbar)
        toolBarClicks(this, toolbar)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerToggle.syncState()
    }


    protected fun sendFCMToken()
    {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener({
            if (it.isSuccessful)
            {
                Log.d("saif", "token= ${it.result?.token}")
                ApiCredential.updateFCMToken(it.result?.token?:"", SessionManager.getUserId()
                    , SessionManager.getUserType()!!, SessionManager.getApiToken(), object :
                        BaseResponseListener<FirebaseTokenResponse>
                    {
                        override fun onSuccess(model: FirebaseTokenResponse) {}
                        override fun onLoading() {}
                        override fun onErrorMsg(errorMsg: String) {}
                        override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) {}
                    })
            }
        })

    }




    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        if (::drawerLayout.isInitialized)
            drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (::drawerLayout.isInitialized && drawerToggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed()
    {
        if (::drawerLayout.isInitialized && drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()

    }

}