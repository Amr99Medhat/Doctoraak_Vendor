package com.doctoraak.doctoraakdoctor.ui.radiologyOrder

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiRadiology
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.BaseResponse

object RadiologyOrderRepository
{
    fun acceptRadiologyOrder(api_token: String, lab_id: Long, order_id: Long, success: (BaseResponse)->Unit
                       , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiRadiology.acceptRadiologyOrder(api_token, lab_id, order_id, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun cancelRadiologyOrder(api_token: String, lab_id: Long, order_id: Long, msg: String, success: (BaseResponse)->Unit
                       , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiRadiology.cancelRadiologyOrder(api_token, lab_id, order_id, msg, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}