package com.doctoraak.doctoraakdoctor.Repository.Remote

import android.util.Log
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBodyImage
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBody
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.start
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File

object ApiDoctor
{
    fun createClinic(api_token: String, clinic: Clinic, callback: BaseResponseListener<ClinicResponse>)
    {
        Log.d("saif", "clinic= ${Gson().toJson(clinic)}")

        var imageData: MultipartBody.Part? = null
        if (clinic.photo != "")
        {
            val image_file = File(clinic.photo)
            imageData =
                MultipartBody.Part.createFormData("photo", image_file.name, createRequestBodyImage(image_file))
        }

        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<ClinicResponse> = service
            .createClinic(phone = createRequestBody(clinic.phone)
                , area_id = createRequestBody(clinic.area_id.toString())
                , fees = createRequestBody(clinic.fees.toString())
                , fees2 = createRequestBody(clinic.fees2.toString())
                , city_id = createRequestBody(clinic.city_id.toString())
                , lat = createRequestBody(clinic.latt)
                , lng = createRequestBody(clinic.lang)
                , doctor_id = createRequestBody(clinic.doctorId.toString())
                , api_token = createRequestBody(api_token)
                , waiting_time = createRequestBody(clinic.waitingTime)
                , working_hours = createRequestBody(Gson().toJson(clinic.shiftWorkingHours))
                , image = imageData)

        call.start(callback)
    }

    fun updateClinic(api_token: String, clinic: Clinic, callback: BaseResponseListener<ClinicResponse>)
    {
        Log.d("saif", "clinic= ${Gson().toJson(clinic)}")

        var imageData: MultipartBody.Part? = null
        if (clinic.photo != "")
        {
            val image_file = File(clinic.photo)
            if (image_file.exists())
                imageData = MultipartBody.Part
                    .createFormData("photo", image_file.name, createRequestBodyImage(image_file))
        }

        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<ClinicResponse> = service
            .updateClinic(phone = createRequestBody(clinic.phone)
                , area_id = createRequestBody(clinic.area_id.toString())
                , fees = createRequestBody(clinic.fees.toString())
                , fees2 = createRequestBody(clinic.fees2.toString())
                , city_id = createRequestBody(clinic.city_id.toString())
                , lat = createRequestBody(clinic.latt)
                , lng = createRequestBody(clinic.lang)
                , doctor_id = createRequestBody(clinic.doctorId.toString())
                , api_token = createRequestBody(api_token)
                , waiting_time = createRequestBody(clinic.waitingTime)
                , clinic_id = createRequestBody(clinic.id.toString())
                , image = imageData)

        call.start(callback)
    }

    fun getClinicOrders(api_token: String, doctor_id: Long, clinic_id: Long
                        , callback: BaseResponseListener<ReservationResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<ReservationResponse> = service.getClinicOrders(api_token, doctor_id, clinic_id)
        call.start(callback)
    }

    fun getAllClinics(api_token: String, doctor_id: Long
                        , callback: BaseResponseListener<AllClinicsResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<AllClinicsResponse> = service.getAllClinics(api_token, doctor_id)
        call.start(callback)
    }

    fun rejectClinicOrder(api_token: String, doctor_id: Long, order_id: Long, callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.rejectClinicOrder(api_token, doctor_id, order_id)
        call.start(callback)
    }

    fun updateDoctorProfile(doctor: Doctor, callback: BaseResponseListener<DoctorResponse>)
    {
        var imageData: MultipartBody.Part? = null
        if (doctor.photo != "")
        {
            val image_file = File(doctor.photo)
            if (image_file.exists())
                imageData = MultipartBody.Part
                    .createFormData("photo", image_file.name, createRequestBodyImage(image_file))
        }

        var cvData: MultipartBody.Part? = null
        if (doctor.cv != "")
        {
            val cv_file = File(doctor.cv)
            if (cv_file.exists())
                cvData = MultipartBody.Part.createFormData("cv", cv_file.name,
                    ApiConfigure.createRequestBodyPdf(
                        cv_file
                    )
                )
        }

        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<DoctorResponse> = service
            .updateDoctorProfile(user_id = createRequestBody(doctor.id.toString())
                , api_token = createRequestBody(doctor.api_token)
                , name = createRequestBody(doctor.name)
                , insurance = createRequestBody(doctor.insuranceIds)
                , degree_id = createRequestBody(doctor.degreeId.toString())
                , cv = cvData
                , image = imageData
                , title = createRequestBody(doctor.title)
                , phone2 = createRequestBody(doctor.phone2)
                , email = createRequestBody(doctor.email)
                , phone = createRequestBody(doctor.phone)
                , specialization_id = createRequestBody(doctor.specializationId.toString())
            )
        call.start(callback)
    }

    fun changeClinicAvailability(api_token: String, clinic_id: Long, doctor_id: Long
                        , callback: BaseResponseListener<BaseResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.changeClinicAvailability(api_token, clinic_id, doctor_id)
        call.start(callback)
    }

    fun updateClinicWorkingHours(api_token: String, clinic_id: Long, doctor_id: Long, shiftWorkingHours: List<ShiftWorkingHour>
                        , callback: BaseResponseListener<BaseResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        Log.d("saif", "updateClinicWorkingHours: size= ${shiftWorkingHours.size},     "+Gson().toJson(shiftWorkingHours))
        val call: Call<BaseResponse> = service.updateClinicWorkingHours(ShiftWorkingHoursRequest(api_token, clinic_id, doctor_id
            , Gson().toJson(shiftWorkingHours)))
        call.start(callback)
    }


}