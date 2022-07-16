package com.doctoraak.doctoraakdoctor.ui.profileLab

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiLab
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

internal object LabProfileRepository
{

    fun updateProfileLab(lab: Lab, success: (LabResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiLab.updateProfileLab(lab, object : BaseResponseListener<LabResponse>
        {
            override fun onSuccess(model: LabResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}