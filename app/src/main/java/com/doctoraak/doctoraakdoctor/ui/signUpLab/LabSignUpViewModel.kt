package com.doctoraak.doctoraakdoctor.ui.signUpLab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class LabSignUpViewModel : ViewModel()
{
    val lab by lazy { MutableLiveData<Lab>(Lab()) }
    val isRegister by lazy { SingleLiveData<LabResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun registerLab(lab: Lab) = LabSignUpRepository.registerLab(lab
        , success = { model: LabResponse ->
            isLoading.setValue(false)
            isRegister.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})


}