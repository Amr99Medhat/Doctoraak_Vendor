package com.doctoraak.doctoraakdoctor.ui.signUpPharmacy

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiCredential
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

internal object PharmacySignUpRepository
{

    fun registerPharmacy(pharmacy: Pharmacy, success: (PharmacyResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiCredential.registerPharmacy(pharmacy, object : BaseResponseListener<PharmacyResponse>
        {
            override fun onSuccess(model: PharmacyResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}