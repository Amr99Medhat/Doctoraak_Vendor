package com.doctoraak.doctoraakdoctor.ui.signUpClinic

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiDoctor
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

internal object ClinicSignUpRepository
{

    fun createClinic(api_token: String, clinic: Clinic, success: (ClinicResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiDoctor.createClinic(api_token, clinic, object : BaseResponseListener<ClinicResponse>
        {
            override fun onSuccess(model: ClinicResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


    
}