package com.doctoraak.doctoraakdoctor.ui.pharmacyHome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class PharmacyHomeViewModel : ViewModel()
{
    val pharmacyOrders by lazy { MutableLiveData<PharmacyOrderResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun getPharmacyOrders(api_token: String, pharmacy_id: Long)
            = PharmacyHomeRepository.getPharmacyOrders(api_token, pharmacy_id
        , success = { model: PharmacyOrderResponse ->
            isLoading.setValue(false)
            pharmacyOrders.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}