package com.doctoraak.doctoraakdoctor.ui.labOrder

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiLab
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.BaseResponse

object LabOrderRepository
{
    fun acceptLabOrder(api_token: String, lab_id: Long, order_id: Long, success: (BaseResponse)->Unit
                            , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiLab.acceptLabOrder(api_token, lab_id, order_id, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }
    
    fun cancelLabOrder(api_token: String, lab_id: Long, order_id: Long, msg: String, success: (BaseResponse)->Unit
                            , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiLab.cancelLabOrder(api_token, lab_id, order_id, msg, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


}