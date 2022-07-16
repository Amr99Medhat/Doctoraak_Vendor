package com.doctoraak.doctoraakdoctor.ui.signUpPharmacy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class PharmacySignUpViewModel : ViewModel()
{
    val pharmacy by lazy { MutableLiveData<Pharmacy>(Pharmacy()) }
    val isRegister by lazy { SingleLiveData<PharmacyResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }

    fun registerPharmacy(pharmacy: Pharmacy) = PharmacySignUpRepository.registerPharmacy(pharmacy
        , success = { model: PharmacyResponse ->
            isLoading.setValue(false)
            isRegister.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}