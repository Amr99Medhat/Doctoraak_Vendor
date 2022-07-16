package com.doctoraak.doctoraakdoctor.ui.mobileVerification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.ui.mobileVerification.MobileVerificationRepository
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData
import com.doctoraak.doctoraakdoctor.utils.UserType

class MobileVerificationViewModel : ViewModel()
{
    val continueUserId by lazy { MutableLiveData<Long>(-1) }
    val isActive by lazy { SingleLiveData<BaseResponse>() }
    val isResendSms by lazy { SingleLiveData<BaseResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun active(userType: UserType, user_id: Long, sms_code: Int)
            = MobileVerificationRepository.activeAccount(userType, user_id, sms_code
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            isActive.setValue(model) }
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

    fun resendSms(userType: UserType, user_id: Long) = MobileVerificationRepository.resendSms(userType, user_id
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            isResendSms.setValue(model) }
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}