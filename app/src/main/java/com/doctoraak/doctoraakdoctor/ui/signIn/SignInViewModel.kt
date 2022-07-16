package com.doctoraak.doctoraakdoctor.ui.signIn

import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

internal class SignInViewModel<T>: ViewModel()
        where T: BaseResponse
{
    val userResponse by lazy { SingleLiveData<T>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun logIn(userType: UserType, phone: String, password: String) =
        SignInRepository.logIn(userType, phone, password
            , success = { model: T ->
                isLoading.setValue(false)
                userResponse.setValue(model)
            }
            , loading = { isLoading.setValue(true) }
            , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
            , error = {i, s ->
                errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}