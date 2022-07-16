package com.doctoraak.doctoraakdoctor.ui.labHome

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiLab
import com.doctoraak.doctoraakdoctor.Repository.Remote.*
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.model.*

internal object LabHomeRepository
{

    fun getLabOrders(api_token: String, lab_id: Long, success: (LabOrderResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiLab.getLabOrders(api_token, lab_id, object : BaseResponseListener<LabOrderResponse>
        {
            override fun onSuccess(model: LabOrderResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


}