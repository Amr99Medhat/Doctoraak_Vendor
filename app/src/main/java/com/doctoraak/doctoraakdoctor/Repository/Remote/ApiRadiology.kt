package com.doctoraak.doctoraakdoctor.Repository.Remote

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBody
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.utils.start
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File

object ApiRadiology
{
    fun getRadiologyOrders(api_token: String, radiology_id: Long
                     , callback: BaseResponseListener<RadiologyOrderResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<RadiologyOrderResponse> = service.getRadiologyOrders(api_token, radiology_id)
        call.start(callback)
    }

    fun updateProfileRadiology(radiology: Radiology, callback: BaseResponseListener<RadiologyResponse>)
    {
        var imageData: MultipartBody.Part? = null
        if (radiology.photo != "")
        {
            val image_file = File(radiology.photo)
            if (image_file.exists())
                imageData = MultipartBody.Part
                    .createFormData("photo", image_file.name,
                        ApiConfigure.createRequestBodyImage(
                            image_file
                        )
                    )
        }

        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<RadiologyResponse> = service
            .updateProfileRadiology(user_id = createRequestBody(radiology.id.toString())
                , api_token = createRequestBody(radiology.api_token)
                , name = createRequestBody(radiology.name)
                , insurance = createRequestBody(radiology.insuranceIds)
                , lng = createRequestBody(radiology.lang)
                , lat = createRequestBody(radiology.latt)
                , delivery = createRequestBody(radiology.delivery.toString())
                , working_hours = createRequestBody(Gson().toJson(radiology.workingHours))
                , city_id = createRequestBody(radiology.city_id.toString())
                , area_id = createRequestBody(radiology.area_id.toString())
                , image = imageData
                , phone =  createRequestBody(radiology.phone)
                , email =  createRequestBody(radiology.email)
            )
        call.start(callback)
    }

    fun acceptRadiologyOrder(api_token: String, lab_id: Long, order_id: Long
                       , callback: BaseResponseListener<BaseResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.acceptLabOrRadiologyOrder(api_token
            , lab_id, order_id, UserType.RADIOLOGY.name)
        call.start(callback)
    }

    fun cancelRadiologyOrder(api_token: String, lab_id: Long, order_id: Long, msg: String
                       , callback: BaseResponseListener<BaseResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.cancelLabOrRadiologyOrder(api_token
            , lab_id, order_id, UserType.RADIOLOGY.name, msg)
        call.start(callback)
    }


}