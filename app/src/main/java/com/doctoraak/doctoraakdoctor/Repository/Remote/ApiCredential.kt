package com.doctoraak.doctoraakdoctor.Repository.Remote

import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBody
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBodyImage
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiConfigure.createRequestBodyPdf
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.doctoraak.doctoraakdoctor.utils.start
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File

object ApiCredential
{
    ////////////////////// LogIn ///////////////////////////

    fun <M> logIn(userType: UserType, phone: String, password: String,callback: BaseResponseListener<M>)
            where M: BaseResponse
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<M>

        call = when (userType)
        {
            UserType.LAB -> service.logInLab(phone, password) as Call<M>
            UserType.PHARMACY -> service.logInPharmacy(phone, password) as Call<M>
            UserType.RADIOLOGY -> service.logInRadiology(phone, password) as Call<M>
            else -> service.logInDoctor(phone, password) as Call<M>
        }

        call.start(callback)
    }

    ////////////////////// ForgetPassword ///////////////////////////

    fun forgetPassword(userType: UserType, phone: String, callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)

        when (userType)
        {
            UserType.LAB -> service.forgetPasswordLab(phone)
            UserType.PHARMACY -> service.forgetPasswordPharmacy(phone)
            UserType.RADIOLOGY -> service.forgetPasswordRadiology(phone)
            else -> service.forgetPasswordDoctor(phone)
        }
            .start(callback)
    }

    fun updatePassword(userType: UserType, phone: String, password: String, new_password: String
                       , callback: BaseResponseListener<BaseResponse>
    )
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)

        when (userType)
        {
            UserType.LAB -> service.updatePasswordLab(phone, old_password = password, new_password = new_password)
            UserType.PHARMACY -> service.updatePasswordPharmacy(phone, old_password = password, new_password = new_password)
            UserType.RADIOLOGY -> service.updatePasswordRadiology(phone, old_password = password, new_password = new_password)
            else -> service.updatePasswordDoctor(phone, old_password = password, new_password = new_password)
        }
            .start(callback)
    }

    fun resendSms(userType: UserType, phone: String, callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)

        when (userType)
        {
            UserType.LAB -> service.resendSmsLab(phone)
            UserType.PHARMACY -> service.resendSmsPharmacy(phone)
            UserType.RADIOLOGY -> service.resendSmsRadiology(phone)
            else -> service.resendSmsDoctor(phone)
        }
            .start(callback)
    }

    ////////////////////// Register ///////////////////////////

    fun registerDoctor(doctor: Doctor, callback: BaseResponseListener<DoctorResponse>)
    {
        val image_file = File(doctor.photo)
        val imageData =
            MultipartBody.Part.createFormData("photo", image_file.name, createRequestBodyImage(image_file))

        val cv_file = File(doctor.cv)
        val cvData =
            MultipartBody.Part.createFormData("cv", cv_file.name, createRequestBodyPdf(cv_file))

        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)

        val call: Call<DoctorResponse> = service
            .registerDoctor(phone = createRequestBody(doctor.phone)
                , gender = createRequestBody(doctor.gender)
                , password = createRequestBody(doctor.password)
                , email = if (doctor.email.isNotBlank()) createRequestBody(doctor.email) else null
                , name = createRequestBody(doctor.name)
                , title = if (doctor.title.isNotBlank()) createRequestBody(doctor.title) else null
                , phone2 = if (doctor.phone2.isNotBlank()) createRequestBody(doctor.phone2) else null
                , specialization_id = createRequestBody(doctor.specializationId.toString())
                , degree_id = createRequestBody(doctor.degreeId.toString())
                , insurance = createRequestBody(doctor.insuranceIds)
                , cv = if (doctor.cv.isNotBlank() && cv_file.exists()) cvData else null

                , is_medical_center = if (SessionManager.getUserType() == UserType.MEDICAL_CENTER) createRequestBody("1") else createRequestBody("0")
                , is_optical_center = if (SessionManager.getUserType() == UserType.OPTICAL_CENTER) createRequestBody("1") else createRequestBody("0")
                , isHospital = if (SessionManager.getUserType() == UserType.HOSPITAL) createRequestBody("1") else createRequestBody("0")

                , image = if (doctor.photo.isNotBlank() && image_file.exists()) imageData else null)
        call.start(callback)
    }

    fun registerPharmacy(pharmacy: Pharmacy, callback: BaseResponseListener<PharmacyResponse>)
    {
        val image_file = File(pharmacy.photo)
        val imageData =
            MultipartBody.Part.createFormData("photo", image_file.name, createRequestBodyImage(image_file))

        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<PharmacyResponse> = service
            .registerPharmacy(phone = createRequestBody(pharmacy.phone)
                , password = createRequestBody(pharmacy.password)
                , email = if (pharmacy.email.isNotBlank()) createRequestBody(pharmacy.email) else null
                , name = createRequestBody(pharmacy.name)
                , insurance = createRequestBody(pharmacy.insuranceIds)
                , lng = createRequestBody(pharmacy.lang)
                , lat = createRequestBody(pharmacy.latt)
                , city_id = createRequestBody(pharmacy.city_id.toString())
                , area_id = createRequestBody(pharmacy.area_id.toString())
                , delivery = createRequestBody(pharmacy.delivery.toString())
                , working_hours = createRequestBody(Gson().toJson(pharmacy.workingHours))
                , image = if (pharmacy.photo.isNotBlank() && image_file.exists()) imageData else null)
        call.start(callback)
    }

    fun registerLab(lab: Lab, callback: BaseResponseListener<LabResponse>)
    {
        val image_file = File(lab.photo)
        val imageData =
            MultipartBody.Part.createFormData("photo", image_file.name, createRequestBodyImage(image_file))

        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<LabResponse> = service
            .registerLab(phone = createRequestBody(lab.phone)
                , password = createRequestBody(lab.password)
                , email = if (lab.email.isNotBlank()) createRequestBody(lab.email) else null
                , name = createRequestBody(lab.name)
                , insurance = createRequestBody(lab.insuranceIds)
                , lng = createRequestBody(lab.lang)
                , lat = createRequestBody(lab.latt)
                , delivery = createRequestBody(lab.delivery.toString())
                , working_hours = createRequestBody(Gson().toJson(lab.workingHours))
                , city_id = createRequestBody(lab.city_id.toString())
                , area_id = createRequestBody(lab.area_id.toString())
                , image = if (lab.photo.isNotBlank() && image_file.exists()) imageData else null)
        call.start(callback)
    }

    fun registerRadiology(radiology: Radiology, callback: BaseResponseListener<RadiologyResponse>)
    {
        val image_file = File(radiology.photo)
        val imageData =
            MultipartBody.Part.createFormData("photo", image_file.name, createRequestBodyImage(image_file))

        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call: Call<RadiologyResponse> = service
            .registerRadiology(phone = createRequestBody(radiology.phone)
                , password = createRequestBody(radiology.password)
                , email = if (radiology.email.isNotBlank()) createRequestBody(radiology.email) else null
                , name = createRequestBody(radiology.name)
                , insurance = createRequestBody(radiology.insuranceIds)
                , lng = createRequestBody(radiology.lang)
                , lat = createRequestBody(radiology.latt)
                , delivery = createRequestBody(radiology.delivery.toString())
                , working_hours = createRequestBody(Gson().toJson(radiology.workingHours))
                , city_id = createRequestBody(radiology.city_id.toString())
                , area_id = createRequestBody(radiology.area_id.toString())
                , image = if (radiology.photo.isNotBlank() && image_file.exists()) imageData else null)
        call.start(callback)
    }

    ////////////////////// Active Account ///////////////////////////

    fun <M> activeAccount(userType: UserType, user_id: Long, sms_code: Int, callback: BaseResponseListener<M>) where M:BaseResponse
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)
        val call: Call<M>

        call = when (userType)
        {
            UserType.LAB -> service.activeLab(user_id, sms_code) as Call<M>
            UserType.PHARMACY -> service.activePharmacy(user_id, sms_code) as Call<M>
            UserType.RADIOLOGY -> service.activeRadiology(user_id, sms_code) as Call<M>
            else -> service.activeDoctor(user_id, sms_code) as Call<M>
        }

        call.start(callback)
    }

    ////////////////////// Resend SMS ///////////////////////////

    fun resendSms(userType: UserType, user_id: Long, callback: BaseResponseListener<BaseResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(ApiEndPoint::class.java)

        val call: Call<BaseResponse>
        call = when (userType)
        {
            UserType.LAB -> service.resendSmsLab(user_id)
            UserType.PHARMACY -> service.resendSmsPharmacy(user_id)
            UserType.RADIOLOGY -> service.resendSmsRadiology(user_id)
            else -> service.resendSmsDoctor(user_id)
        }

        call.start(callback)
    }

    ////////////////////////////// FCM ////////////////////////////

    fun updateFCMToken(firebase_token: String, user_id: Long, user_type: UserType, api_token: String,
                            callback: BaseResponseListener<FirebaseTokenResponse>)
    {
        val service = ApiConfigure.mainRetrofit.create(
            ApiEndPoint::class.java)
        val call  = service.updateFCMToken(firebase_token , user_id , user_type.name , api_token)
        call.start(callback)
    }


    
}


