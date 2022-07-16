package com.doctoraak.doctoraakdoctor.ui

import android.app.Application
import android.content.Context
import com.doctoraak.doctoraakdoctor.Repository.Local.InternetConnection
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager

class App: Application()
{

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        SessionManager(this)
        InternetConnection(this)
    }



}