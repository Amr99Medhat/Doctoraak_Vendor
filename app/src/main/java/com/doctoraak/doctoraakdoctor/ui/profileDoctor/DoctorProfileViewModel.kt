package com.doctoraak.doctoraakdoctor.ui.profileDoctor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class DoctorProfileViewModel : ViewModel()
{
    val updateProfile by lazy { SingleLiveData<DoctorResponse>() }
    val doctor by lazy { MutableLiveData<Doctor>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }

    fun updateDoctorProfile(doctor: Doctor)
            = DoctorProfileRepository.updateDoctorProfile(doctor
        , success = { model: DoctorResponse ->
            isLoading.setValue(false)
            updateProfile.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}