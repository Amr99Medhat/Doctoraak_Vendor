package com.doctoraak.doctoraakdoctor.Repository.Local

import android.content.Context
import android.net.ConnectivityManager

class InternetConnection(context: Context)
{
    init {
        manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    companion object
    {
        private lateinit var manager: ConnectivityManager

        fun checkInternetConnection(): Boolean {
            return manager.activeNetworkInfo != null && manager.activeNetworkInfo!!.isConnected
        }



    }
}