package com.doctoraak.doctoraakdoctor.ui.radiologyHome

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiRadiology
import com.doctoraak.doctoraakdoctor.Repository.Remote.*
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.model.*

internal object RadiologyHomeRepository
{

    fun getRadiologyOrders(api_token: String, radiology_id: Long, success: (RadiologyOrderResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiRadiology.getRadiologyOrders(api_token, radiology_id, object : BaseResponseListener<RadiologyOrderResponse>
        {
            override fun onSuccess(model: RadiologyOrderResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


}