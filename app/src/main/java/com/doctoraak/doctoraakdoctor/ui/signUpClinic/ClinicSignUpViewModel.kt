package com.doctoraak.doctoraakdoctor.ui.signUpClinic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class ClinicSignUpViewModel : ViewModel()
{
    val clinic by lazy { MutableLiveData(Clinic()) }
    val isCreateClinic by lazy { SingleLiveData<ClinicResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun createClinic(api_token: String, clinic: Clinic) = ClinicSignUpRepository.createClinic(api_token, clinic
        , success = { model: ClinicResponse ->
            isLoading.setValue(false)
            isCreateClinic.setValue(model) }
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}