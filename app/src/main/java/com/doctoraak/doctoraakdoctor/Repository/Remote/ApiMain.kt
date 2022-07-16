package com.doctoraak.doctoraakdoctor.Repository.Remote

import android.text.TextUtils
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.utils.start
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object ApiMain
{
    fun getSpecializations(callback: BaseResponseListener<SpecializationResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.getSpecialization()
            .start(callback)
    }

    fun getDegrees(callback: BaseResponseListener<DegreeResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.getDegrees()
            .start(callback)
    }

    fun getCity(callback: BaseResponseListener<CityResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.getCities()
            .start(callback)
    }

    fun getArea(city_id: Int? = null, callback: BaseResponseListener<AreaResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.getAreas(city_id)
            .start(callback)
    }
    //update_clinic

    fun getInsurances(callback: BaseResponseListener<InsuranceResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.getInsurances()
            .start(callback)
    }

    fun getPhone(callback: BaseResponseListener<PhoneResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.getPhone()
            .start(callback)
    }

    fun getContact(callback: BaseResponseListener<ContactResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.getContact()
            .start(callback)
    }


    fun getNotifications(userId : Long, userType : UserType, api_token : String
                         , callback: BaseResponseListener<NotificationsResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.getNotifications(userId, userType.name, api_token).start(callback)
    }

    fun removeNotifications(notification_id : Int, callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        service.removeNotifications(notification_id).start(callback)
    }

}