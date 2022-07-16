package com.doctoraak.doctoraakdoctor.ui.profileDoctor

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiDoctor
import com.doctoraak.doctoraakdoctor.Repository.Remote.*
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.model.*

internal object DoctorProfileRepository
{

    fun updateDoctorProfile(doctor: Doctor, success: (DoctorResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiDoctor.updateDoctorProfile(doctor
            , object: BaseResponseListener<DoctorResponse>
        {
            override fun onSuccess(model: DoctorResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


}