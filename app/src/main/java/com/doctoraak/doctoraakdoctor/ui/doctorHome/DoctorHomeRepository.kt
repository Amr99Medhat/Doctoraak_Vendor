package com.doctoraak.doctoraakdoctor.ui.doctorHome

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiDoctor
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

internal object DoctorHomeRepository
{

    fun getClinicOrders(api_token: String, doctor_id: Long, clinic_id: Long, success: (ReservationResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiDoctor.getClinicOrders(api_token, doctor_id, clinic_id, object : BaseResponseListener<ReservationResponse>
        {
            override fun onSuccess(model: ReservationResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun getAllClinics(api_token: String, doctor_id: Long, success: (AllClinicsResponse)->Unit
                    , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiDoctor.getAllClinics(api_token, doctor_id, object : BaseResponseListener<AllClinicsResponse>
        {
            override fun onSuccess(model: AllClinicsResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun updateClinicWorkingHours(api_token: String, clinic_id: Long, doctor_id: Long, shiftWorkingHours: List<ShiftWorkingHour>, success: (BaseResponse)->Unit
                      , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiDoctor.updateClinicWorkingHours(api_token, clinic_id, doctor_id, shiftWorkingHours, object : BaseResponseListener<BaseResponse>
        {
            override fun onSuccess(model: BaseResponse) = success(model)
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


    
}