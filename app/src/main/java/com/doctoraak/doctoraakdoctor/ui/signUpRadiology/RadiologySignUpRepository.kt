package com.doctoraak.doctoraakdoctor.ui.signUpRadiology

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiCredential
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

internal object RadiologySignUpRepository
{

    fun registerRadiology(radiology: Radiology, success: (RadiologyResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiCredential.registerRadiology(radiology, object : BaseResponseListener<RadiologyResponse>
        {
            override fun onSuccess(model: RadiologyResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}