package com.doctoraak.doctoraakdoctor.ui.labHome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class LabHomeViewModel : ViewModel()
{
    val labOrders by lazy { MutableLiveData<LabOrderResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun getLabOrders(api_token: String, lab_id: Long)
            = LabHomeRepository.getLabOrders(api_token, lab_id
        , success = { model: LabOrderResponse ->
            isLoading.setValue(false)
            labOrders.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})


}