package com.doctoraak.doctoraakdoctor.ui.pharmacyOrder

import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.ui.pharmacyHome.PharmacyHomeRepository
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

internal class PharmacyOrderViewModel: ViewModel()
{
    val acceptPharmacyOrder by lazy { SingleLiveData<BaseResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun acceptPharmacyOrder(api_token: String, pharmacy_id: Long, order_id: Long)
            = PharmacyOrderRepository.acceptPharmacyOrder(api_token, pharmacy_id, order_id
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            acceptPharmacyOrder.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})


}