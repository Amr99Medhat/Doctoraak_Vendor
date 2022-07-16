package com.doctoraak.doctoraakdoctor.ui.doctorReservation

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiDoctor
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.BaseResponse

object DoctorReservationRepository
{
    fun rejectClinicOrder(api_token: String, doctor_id: Long, order_id: Long, success: (BaseResponse)->Unit
                       , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiDoctor.rejectClinicOrder(api_token, doctor_id, order_id, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }
}