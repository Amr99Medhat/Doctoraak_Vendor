package com.doctoraak.doctoraakdoctor.ui.signUpDoctor

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiCredential
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

internal object DoctorSignUpRepository
{

    fun registerDoctor(doctor: Doctor, success: (DoctorResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiCredential.registerDoctor(doctor, object : BaseResponseListener<DoctorResponse>
        {
            override fun onSuccess(model: DoctorResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    
}
