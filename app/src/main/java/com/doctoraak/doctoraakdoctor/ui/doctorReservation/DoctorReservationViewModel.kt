package com.doctoraak.doctoraakdoctor.ui.doctorReservation

import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

internal class DoctorReservationViewModel: ViewModel()
{
    val cancelReservation by lazy { SingleLiveData<BaseResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }

    fun cancelReservation(api_token: String, doctor_id: Long, order_id: Long) = DoctorReservationRepository
        .rejectClinicOrder(api_token, doctor_id, order_id
            , success = { model: BaseResponse ->
                isLoading.setValue(false)
                cancelReservation.setValue(model)}
            , loading = { isLoading.setValue(true) }
            , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
            , error = {i, s ->
                errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}