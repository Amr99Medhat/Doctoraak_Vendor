package com.doctoraak.doctoraakdoctor.ui.profilePharmacy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class PharmacyProfileViewModel : ViewModel()
{
    val pharmacy by lazy { MutableLiveData<Pharmacy>(Pharmacy()) }
    val updateProfile by lazy { SingleLiveData<PharmacyResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }

    fun updateProfile(pharmacy: Pharmacy) = PharmacyProfileRepository.updateProfile(pharmacy
        , success = { model: PharmacyResponse ->
            isLoading.setValue(false)
            updateProfile.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}