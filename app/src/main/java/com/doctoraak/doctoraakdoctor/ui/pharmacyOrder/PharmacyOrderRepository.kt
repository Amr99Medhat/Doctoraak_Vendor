package com.doctoraak.doctoraakdoctor.ui.pharmacyOrder

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiPharmacy
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.BaseResponse

object PharmacyOrderRepository
{
    fun acceptPharmacyOrder(api_token: String, pharmacy_id: Long, order_id: Long, success: (BaseResponse)->Unit
                            , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiPharmacy.acceptPharmacyOrder(api_token, pharmacy_id, order_id, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}