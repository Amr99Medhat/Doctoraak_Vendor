package com.doctoraak.doctoraakdoctor.ui.mobileVerification

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiCredential
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.UserType

internal object MobileVerificationRepository
{

    fun <M> activeAccount(userType: UserType, user_id: Long, sms_code: Int, success: (M)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit) where M:BaseResponse
    {
        ApiCredential.activeAccount(userType, user_id, sms_code, object : BaseResponseListener<M>
        {
            override fun onSuccess(model: M) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun resendSms(userType: UserType, user_id: Long, success: (BaseResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiCredential.resendSms(userType, user_id, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


}