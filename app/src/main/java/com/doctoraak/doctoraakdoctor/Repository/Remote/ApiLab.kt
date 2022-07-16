package com.doctoraak.doctoraakdoctor.Repository.Remote

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBody
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBodyImage
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.model.Lab
import com.doctoraak.doctoraakdoctor.model.LabOrderResponse
import com.doctoraak.doctoraakdoctor.model.LabResponse
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.utils.start
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File

object ApiLab
{
    fun getLabOrders(api_token: String, lab_id: Long
                          , callback: BaseResponseListener<LabOrderResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<LabOrderResponse> = service.getLabOrders(api_token, lab_id)
        call.start(callback)
    }


    fun updateProfileLab(lab: Lab, callback: BaseResponseListener<LabResponse>)
    {
        var imageData: MultipartBody.Part? = null
        if (lab.photo != "")
        {
            val image_file = File(lab.photo)
            if (image_file.exists())
                imageData = MultipartBody.Part
                    .createFormData("photo", image_file.name, createRequestBodyImage(image_file))
        }

        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<LabResponse> = service
            .updateProfileLab(user_id = createRequestBody(lab.id.toString())
                , api_token = createRequestBody(lab.api_token)
                , name = createRequestBody(lab.name)
                , insurance = createRequestBody(lab.insuranceIds)
                , lng = createRequestBody(lab.lang)
                , lat = createRequestBody(lab.latt)
                , email = createRequestBody(lab.email)
                , phone = createRequestBody(lab.phone)
                , delivery = createRequestBody(lab.delivery.toString())
                , working_hours = createRequestBody(Gson().toJson(lab.workingHours))
                , city_id = createRequestBody(lab.city_id.toString())
                , area_id = createRequestBody(lab.area_id.toString())
                , image = imageData)
        call.start(callback)
    }

    fun acceptLabOrder(api_token: String, lab_id: Long, order_id: Long
                     , callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.acceptLabOrRadiologyOrder(api_token
            , lab_id, order_id, UserType.LAB.name)
        call.start(callback)
    }

    fun cancelLabOrder(api_token: String, lab_id: Long, order_id: Long, msg: String
                     , callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.cancelLabOrRadiologyOrder(api_token
            , lab_id, order_id, UserType.LAB.name, msg)
        call.start(callback)
    }


}