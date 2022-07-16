package com.doctoraak.doctoraakdoctor.ui.profilePharmacy

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiPharmacy
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

internal object PharmacyProfileRepository
{

    fun updateProfile(pharmacy: Pharmacy, success: (PharmacyResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiPharmacy.updateProfile(pharmacy, object : BaseResponseListener<PharmacyResponse>
        {
            override fun onSuccess(model: PharmacyResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}