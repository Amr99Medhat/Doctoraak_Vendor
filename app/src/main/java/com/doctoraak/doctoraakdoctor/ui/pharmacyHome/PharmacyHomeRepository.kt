package com.doctoraak.doctoraakdoctor.ui.pharmacyHome

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiPharmacy
import com.doctoraak.doctoraakdoctor.Repository.Remote.*
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.model.*

internal object PharmacyHomeRepository
{

    fun getPharmacyOrders(api_token: String, pharmacy_id: Long, success: (PharmacyOrderResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiPharmacy.getPharmacyOrders(api_token, pharmacy_id, object : BaseResponseListener<PharmacyOrderResponse>
        {
            override fun onSuccess(model: PharmacyOrderResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


}