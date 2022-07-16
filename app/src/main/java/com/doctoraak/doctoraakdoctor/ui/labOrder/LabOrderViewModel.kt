package com.doctoraak.doctoraakdoctor.ui.labOrder

import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

internal class LabOrderViewModel: ViewModel()
{
    val acceptLabOrder by lazy { SingleLiveData<BaseResponse>() }
    val cancelLabOrder by lazy { SingleLiveData<BaseResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun acceptLabOrder(api_token: String, lab_id: Long, order_id: Long)
            = LabOrderRepository.acceptLabOrder(api_token, lab_id, order_id
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            acceptLabOrder.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

    fun cancelLabOrder(api_token: String, lab_id: Long, order_id: Long, msg: String)
            = LabOrderRepository.cancelLabOrder(api_token, lab_id, order_id, msg
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            cancelLabOrder.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})


}