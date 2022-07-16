package com.doctoraak.doctoraakdoctor.ui.updateClinic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class UpdateClinicViewModel : ViewModel()
{
    val clinic by lazy { MutableLiveData(Clinic()) }
    val isUpdateClinic by lazy { SingleLiveData<ClinicResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }
    val clinicAvailability by lazy { SingleLiveData<BaseResponse>() }


    fun updateClinic(api_token: String, clinic: Clinic) = UpdateClinicRepository.updateClinic(api_token, clinic
        , success = { model: ClinicResponse ->
            isLoading.setValue(false)
            isUpdateClinic.setValue(model) }
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

    fun changeClinicAvailability(api_token: String, clinic_id: Long, doctor_id: Long)
            = UpdateClinicRepository.changeClinicAvailability(api_token, clinic_id, doctor_id
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            clinicAvailability.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})


}