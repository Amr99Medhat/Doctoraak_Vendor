package com.doctoraak.doctoraakdoctor.ui.radiologyOrder

import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

internal class RadiologyOrderViewModel: ViewModel()
{
    val acceptRadiologyOrder by lazy { SingleLiveData<BaseResponse>() }
    val cancelRadiologyOrder by lazy { SingleLiveData<BaseResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun acceptRadiologyOrder(api_token: String, lab_id: Long, order_id: Long)
            = RadiologyOrderRepository.acceptRadiologyOrder(api_token, lab_id, order_id
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            acceptRadiologyOrder.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

    fun cancelRadiologyOrder(api_token: String, lab_id: Long, order_id: Long, msg: String)
            = RadiologyOrderRepository.cancelRadiologyOrder(api_token, lab_id, order_id, msg
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            cancelRadiologyOrder.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}