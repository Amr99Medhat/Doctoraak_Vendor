package com.doctoraak.doctoraakdoctor.ui.forgetPassword

import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiCredential
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.BaseResponse

internal object ForgetPasswordRepository
{

    fun forgetPassword(userType: UserType, phone: String, success: (BaseResponse)->Unit
                       , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiCredential.forgetPassword(userType, phone, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse)
            {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun updatePassword(userType: UserType, phone: String, password: String, newPassword: String, success: (BaseResponse)->Unit
                       , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiCredential.updatePassword(userType, phone, password, newPassword, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse)
            {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun resendSms(userType: UserType, phone: String, success: (BaseResponse)->Unit
                        , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiCredential.resendSms(userType, phone, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}