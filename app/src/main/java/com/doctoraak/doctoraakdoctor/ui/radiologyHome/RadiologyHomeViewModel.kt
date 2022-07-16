package com.doctoraak.doctoraakdoctor.ui.radiologyHome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class RadiologyHomeViewModel : ViewModel()
{
    val radiologyOrders by lazy { MutableLiveData<RadiologyOrderResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }

    fun getRadiologyOrders(api_token: String, radiology_id: Long)
            = RadiologyHomeRepository.getRadiologyOrders(api_token, radiology_id
        , success = { model: RadiologyOrderResponse ->
            isLoading.setValue(false)
            radiologyOrders.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}