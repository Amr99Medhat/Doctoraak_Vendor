package com.doctoraak.doctoraakdoctor.ui.signIn

import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiCredential
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.BaseResponse

internal object SignInRepository
{
    fun <M> logIn(userType: UserType, phone: String, password: String, success: (M)->Unit
                  , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit) where M: BaseResponse
    {
        ApiCredential.logIn(userType, phone, password, object : BaseResponseListener<M>
        {
            override fun onSuccess(model: M)
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


}