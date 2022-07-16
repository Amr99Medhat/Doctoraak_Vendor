package com.doctoraak.doctoraakdoctor.ui.profileRadiology

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiRadiology
import com.doctoraak.doctoraakdoctor.Repository.Remote.*
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.model.*

internal object RadiologyProfileRepository
{

    fun updateProfileRadiology(radiology: Radiology, success: (RadiologyResponse)->Unit
                         , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiRadiology.updateProfileRadiology(radiology, object : BaseResponseListener<RadiologyResponse>
        {
            override fun onSuccess(model: RadiologyResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}