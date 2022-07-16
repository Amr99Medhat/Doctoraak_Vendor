package com.doctoraak.doctoraakdoctor.ui.signUpRadiology

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class RadiologySignUpViewModel : ViewModel()
{
    val radiology by lazy { MutableLiveData<Radiology>(Radiology()) }
    val isRegister by lazy { SingleLiveData<RadiologyResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun registerRadiology(lab: Radiology) = RadiologySignUpRepository.registerRadiology(lab
        , success = { model: RadiologyResponse ->
            isLoading.setValue(false)
            isRegister.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})


}