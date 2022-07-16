package com.doctoraak.doctoraakdoctor.Repository.Remote

import android.util.Log
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBody
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.model.Pharmacy
import com.doctoraak.doctoraakdoctor.model.PharmacyOrderResponse
import com.doctoraak.doctoraakdoctor.model.PharmacyResponse
import com.doctoraak.doctoraakdoctor.utils.start
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File

object ApiPharmacy
{
    fun getPharmacyOrders(api_token: String, pharmacy_id: Long
                        , callback: BaseResponseListener<PharmacyOrderResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<PharmacyOrderResponse> = service.getPharmacyOrders(api_token, pharmacy_id)
        call.start(callback)
    }

    fun acceptPharmacyOrder(api_token: String, pharmacy_id: Long, order_id: Long
                        , callback: BaseResponseListener<BaseResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<BaseResponse> = service.acceptPharmacyOrder(api_token, pharmacy_id, order_id)
        call.start(callback)
    }

    fun updateProfile(pharmacy: Pharmacy, callback: BaseResponseListener<PharmacyResponse>)
    {
        var imageData: MultipartBody.Part? = null
        if (pharmacy.photo != "")
        {
            val image_file = File(pharmacy.photo)
            if (image_file.exists())
                imageData =
                    MultipartBody.Part.createFormData("photo", image_file.name
                        ,
                        ApiConfigure.createRequestBodyImage(
                            image_file
                        )
                    )
        }

        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<PharmacyResponse> = service
            .updatePharmacy(user_id = createRequestBody(pharmacy.id.toString())
                , api_token = createRequestBody(pharmacy.api_token)
                , name = createRequestBody(pharmacy.name)
                , insurance = createRequestBody(pharmacy.insuranceIds)
                , lng = createRequestBody(pharmacy.lang)
                , lat = createRequestBody(pharmacy.latt)
                , delivery = createRequestBody(pharmacy.delivery.toString())
                , working_hours = createRequestBody(Gson().toJson(pharmacy.workingHours))
                , image = imageData
                , phone = createRequestBody(pharmacy.phone)
                , email = createRequestBody(pharmacy.email)
                , city_id = createRequestBody(pharmacy.city_id.toString())
                , area_id = createRequestBody(pharmacy.area_id.toString()))

        Log.d("saif", "${Gson().toJson(pharmacy)}")

        call.start(callback)
    }


}