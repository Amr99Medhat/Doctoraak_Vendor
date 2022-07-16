package com.doctoraak.doctoraakdoctor.ui.forgetPassword

import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class ForgetPasswordViewModel : ViewModel()
{
    val isForgetPassStep by lazy { SingleLiveData<BaseResponse>() }
    val isUpdatePassStep by lazy { SingleLiveData<BaseResponse>() }
    val isResendSms by lazy { SingleLiveData<BaseResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }

    fun forgetPassword(userType: UserType, phone: String) =
        ForgetPasswordRepository.forgetPassword(userType, phone, success = { model: BaseResponse ->
            isLoading.setValue(false)
            isForgetPassStep.setValue(model)
        }
            , loading = { isLoading.setValue(true) }
            , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
            , error = {i, s ->
                errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

    fun updatePassword(userType: UserType, phone: String, password: String, newPassword: String) =
        ForgetPasswordRepository.updatePassword(userType, phone, password, newPassword, success = { model: BaseResponse ->
            isLoading.setValue(false)
            isUpdatePassStep.setValue(model) }
            , loading = { isLoading.setValue(true) }
            , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false)}
            , error = {i, s ->
                errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

    fun resendSms(userType: UserType, phone: String) = ForgetPasswordRepository.resendSms(userType, phone
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            isResendSms.setValue(model) }
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}