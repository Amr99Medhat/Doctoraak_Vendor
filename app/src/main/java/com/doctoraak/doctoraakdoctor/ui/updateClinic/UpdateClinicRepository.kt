package com.doctoraak.doctoraakdoctor.ui.updateClinic

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiDoctor
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

internal object UpdateClinicRepository
{

    fun updateClinic(api_token: String, clinic: Clinic, success: (ClinicResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiDoctor.updateClinic(api_token, clinic, object : BaseResponseListener<ClinicResponse>
        {
            override fun onSuccess(model: ClinicResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun changeClinicAvailability(api_token: String, clinic_id: Long, doctor_id: Long, success: (BaseResponse)->Unit
                                 , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiDoctor.changeClinicAvailability(api_token, clinic_id, doctor_id, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    
}