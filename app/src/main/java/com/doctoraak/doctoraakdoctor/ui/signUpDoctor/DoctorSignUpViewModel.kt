package com.doctoraak.doctoraakdoctor.ui.signUpDoctor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class DoctorSignUpViewModel : ViewModel()
{
    val doctor by lazy { MutableLiveData<Doctor>(Doctor()) }
    val isRegister by lazy { SingleLiveData<DoctorResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun registerDoctor(doctor: Doctor) = DoctorSignUpRepository.registerDoctor(doctor
        , success = { model: DoctorResponse ->
            isLoading.setValue(false)
            isRegister.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}