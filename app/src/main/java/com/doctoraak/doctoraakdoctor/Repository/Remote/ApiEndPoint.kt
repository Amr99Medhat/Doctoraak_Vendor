package com.doctoraak.doctoraakdoctor.Repository.Remote

import com.doctoraak.doctoraakdoctor.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint
{
    ////////////////////// Main ///////////////////////////

    @GET("show_specialization")
    fun getSpecialization() : Call<SpecializationResponse>

    @GET("show_degree")
    fun getDegrees() : Call<DegreeResponse>

    @GET("show_city")
    fun getCities() : Call<CityResponse>

    @GET("show_area")
    fun getAreas(@Query("city_id") city_id: Int? = null) : Call<AreaResponse>

    @GET("show_insurance")
    fun getInsurances() : Call<InsuranceResponse>

    @GET("show_phone")
    fun getPhone() : Call<PhoneResponse>

    @GET("contact")
    fun getContact() : Call<ContactResponse>

    ////////////////////// LogIn ///////////////////////////

    /** this is used for doctor, medical center, hospital, optical center */
    @FormUrlEncoded
    @POST("doctor_login")
    fun logInDoctor(@Field("phone") phone: String, @Field("password") password: String) : Call<DoctorResponse>

    @FormUrlEncoded
    @POST("pharmacy_login")
    fun logInPharmacy(@Field("phone") phone: String, @Field("password") password: String) : Call<PharmacyResponse>

    @FormUrlEncoded
    @POST("lab_login")
    fun logInLab(@Field("phone") phone: String, @Field("password") password: String) : Call<LabResponse>

    @FormUrlEncoded
    @POST("radiology_login")
    fun logInRadiology(@Field("phone") phone: String, @Field("password") password: String) : Call<RadiologyResponse>

    ////////////////////// ForgetPassword ///////////////////////////

    @FormUrlEncoded
    @POST("doctor_forget_password")
    fun forgetPasswordDoctor(@Field("phone") phone: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("doctor_update_password")
    fun updatePasswordDoctor(@Field("phone") phone: String, @Field("old_password") old_password: String
                                     , @Field("new_password") new_password: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("pharmacy_forget_password")
    fun forgetPasswordPharmacy(@Field("phone") phone: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("pharmacy_update_password")
    fun updatePasswordPharmacy(@Field("phone") phone: String, @Field("old_password") old_password: String
                                     , @Field("new_password") new_password: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("radiology_forget_password")
    fun forgetPasswordRadiology(@Field("phone") phone: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("radiology_update_password")
    fun updatePasswordRadiology(@Field("phone") phone: String, @Field("old_password") old_password: String
                                     , @Field("new_password") new_password: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("lab_forget_password")
    fun forgetPasswordLab(@Field("phone") phone: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("lab_update_password")
    fun updatePasswordLab(@Field("phone") phone: String, @Field("old_password") old_password: String
                                     , @Field("new_password") new_password: String) : Call<BaseResponse>

    ////////////////////// Resend SMS ///////////////////////////

    @FormUrlEncoded
    @POST("doctor_resend")
    fun resendSmsDoctor(@Field("user_id") userId: Long) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("pharmacy_resend")
    fun resendSmsPharmacy(@Field("user_id") userId: Long) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("lab_resend")
    fun resendSmsLab(@Field("user_id") userId: Long) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("radiology_resend")
    fun resendSmsRadiology(@Field("user_id") userId: Long) : Call<BaseResponse>

    ////////////////////// Resend SMS using Phone Number For Forget Password///////////////////////////

    @FormUrlEncoded
    @POST("doctor_resend_second")
    fun resendSmsDoctor(@Field("phone") phone: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("pharmacy_resend_second")
    fun resendSmsPharmacy(@Field("phone") phone: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("lab_resend_second")
    fun resendSmsLab(@Field("phone") phone: String) : Call<BaseResponse>

    @FormUrlEncoded
    @POST("radiology_resend_second")
    fun resendSmsRadiology(@Field("phone") phone: String) : Call<BaseResponse>

    ////////////////////// FCM ///////////////////////////

    @POST("token/update")
    fun updateFCMToken(@Query("firebase_token") firebase_token: String,
                    @Query("user_id") user_id: Long,
                    @Query("user_type") user_type: String,
                    @Query("api_token") api_token: String)
            : Call<FirebaseTokenResponse>

    ////////////////////// Register ///////////////////////////

    @Multipart
    @POST("doctor_register")
    fun registerDoctor(@Part image: MultipartBody.Part?
                       , @Part cv: MultipartBody.Part?
                       , @Part("name") name: RequestBody
                       , @Part("email") email: RequestBody?
                       , @Part("phone") phone: RequestBody
                       , @Part("phone2") phone2: RequestBody?
                       , @Part("title") title: RequestBody?
                       , @Part("password") password: RequestBody
                       , @Part("gender") gender: RequestBody
                       , @Part("specialization_id") specialization_id: RequestBody
                       , @Part("degree_id") degree_id: RequestBody
                       , @Part("insurance") insurance: RequestBody
                       , @Part("is_medical_center") is_medical_center: RequestBody
                       , @Part("is_optical_center") is_optical_center: RequestBody
                       , @Part("isHospital") isHospital: RequestBody)
            : Call<DoctorResponse>


    @Multipart
    @POST("pharmacy_register")
    fun registerPharmacy(@Part image: MultipartBody.Part?
                         , @Part("name") name: RequestBody
                         , @Part("email") email: RequestBody?
                         , @Part("phone") phone: RequestBody
                         , @Part("password") password: RequestBody
                         , @Part("delivery") delivery: RequestBody
                         , @Part("lng") lng: RequestBody
                         , @Part("lat") lat: RequestBody
                         , @Part("city_id") city_id: RequestBody
                         , @Part("area_id") area_id: RequestBody
                         , @Part("working_hours") working_hours: RequestBody
                         , @Part("insurance") insurance: RequestBody)
            : Call<PharmacyResponse>

    @Multipart
    @POST("lab_register")
    fun registerLab(@Part image: MultipartBody.Part?
                    , @Part("name") name: RequestBody
                    , @Part("email") email: RequestBody?
                    , @Part("phone") phone: RequestBody
                    , @Part("password") password: RequestBody
                    , @Part("delivery") delivery: RequestBody
                    , @Part("lng") lng: RequestBody, @Part("lat") lat: RequestBody
                    , @Part("working_hours") working_hours: RequestBody
                    , @Part("city_id") city_id: RequestBody
                    , @Part("area_id") area_id: RequestBody
                    , @Part("insurance") insurance: RequestBody)
            : Call<LabResponse>

    @Multipart
    @POST("radiology_register")
    fun registerRadiology(@Part image: MultipartBody.Part?
                    , @Part("name") name: RequestBody
                    , @Part("email") email: RequestBody?, @Part("phone") phone: RequestBody
                    , @Part("password") password: RequestBody
                    , @Part("delivery") delivery: RequestBody
                    , @Part("lng") lng: RequestBody, @Part("lat") lat: RequestBody
                    , @Part("working_hours") working_hours: RequestBody
                    , @Part("city_id") city_id: RequestBody
                    , @Part("area_id") area_id: RequestBody
                    , @Part("insurance") insurance: RequestBody)
            : Call<RadiologyResponse>

    ////////////////////// Active Account ///////////////////////////

    @FormUrlEncoded
    @POST("doctor_verify_account")
    fun activeDoctor(@Field("user_id") user_id: Long, @Field("sms_code") sms_code: Int) : Call<DoctorResponse>

    @FormUrlEncoded
    @POST("pharmacy_verify_account")
    fun activePharmacy(@Field("user_id") user_id: Long, @Field("sms_code") sms_code: Int) : Call<PharmacyResponse>

    @FormUrlEncoded
    @POST("lab_verify_account")
    fun activeLab(@Field("user_id") user_id: Long, @Field("sms_code") sms_code: Int) : Call<LabResponse>

    @FormUrlEncoded
    @POST("radiology_verify_account")
    fun activeRadiology(@Field("user_id") user_id: Long, @Field("sms_code") sms_code: Int) : Call<RadiologyResponse>

    ////////////////////  Doctor  ///////////////////////////////////

    @Multipart
    @POST("doctor_create_clinic")
    fun createClinic(@Part image: MultipartBody.Part?
                       , @Part("fees") fees: RequestBody
                       , @Part("fees2") fees2: RequestBody
                       , @Part("lng") lng: RequestBody
                       , @Part("lat") lat: RequestBody
                       , @Part("phone") phone: RequestBody
                       , @Part("waiting_time") waiting_time: RequestBody
                       , @Part("city_id") city_id: RequestBody
                       , @Part("area_id") area_id: RequestBody
                       , @Part("working_hours") working_hours: RequestBody
                       , @Part("doctor_id") doctor_id: RequestBody
                       , @Part("api_token") api_token: RequestBody
    ): Call<ClinicResponse>

    @Multipart
    @POST("update_clinic")
    fun updateClinic(@Part image: MultipartBody.Part?
                     , @Part("fees") fees: RequestBody
                     , @Part("fees2") fees2: RequestBody
                     , @Part("lng") lng: RequestBody
                     , @Part("lat") lat: RequestBody
                     , @Part("phone") phone: RequestBody
                     , @Part("waiting_time") waiting_time: RequestBody
                     , @Part("city_id") city_id: RequestBody
                     , @Part("area_id") area_id: RequestBody
                     , @Part("doctor_id") doctor_id: RequestBody
                     , @Part("clinic_id") clinic_id: RequestBody
                     , @Part("api_token") api_token: RequestBody
    ): Call<ClinicResponse>

    @GET("clinic/order/get")
    fun getClinicOrders(@Query("api_token") api_token: String
                        , @Query("doctor_id") doctor_id: Long
                        , @Query("clinic_id") clinic_id: Long
    ): Call<ReservationResponse>

    @GET("clinic/get/doctor")
    fun getAllClinics(@Query("api_token") api_token: String
                        , @Query("doctor_id") doctor_id: Long
    ): Call<AllClinicsResponse>

    @FormUrlEncoded
    @POST("clinic/order/reject")
    fun rejectClinicOrder(@Field("api_token") api_token: String
                        , @Field("doctor_id") doctor_id: Long
                        , @Field("order_id") order_id: Long
    ): Call<BaseResponse>

    @GET("clinic/availability")
    fun changeClinicAvailability(@Query("api_token") api_token: String
                        , @Query("clinic_id") clinic_id: Long, @Query("doctor_id") doctor_id: Long
    ): Call<BaseResponse>

    @POST("doctor_update_clinic")
    fun updateClinicWorkingHours(@Body shiftWorkingHoursRequest: ShiftWorkingHoursRequest
    ): Call<BaseResponse>

    @Multipart
    @POST("doctor_update_profile")
    fun updateDoctorProfile(@Part("user_id") user_id: RequestBody
                            , @Part image: MultipartBody.Part?
                            , @Part cv: MultipartBody.Part?
                            , @Part("name") name: RequestBody
                            , @Part("api_token") api_token: RequestBody
                            , @Part("degree_id") degree_id: RequestBody
                            , @Part("insurance") insurance: RequestBody
                            , @Part("email") email: RequestBody?
                            , @Part("phone") phone: RequestBody
                            , @Part("specialization_id") specialization_id: RequestBody
                            , @Part("phone2") phone2: RequestBody?
                            , @Part("title") title: RequestBody?)
            : Call<DoctorResponse>

    ////////////////////  Pharmacy  ///////////////////////////////////

    @GET("pharmacy/order/get")
    fun getPharmacyOrders(@Query("api_token") api_token: String
                          , @Query("pharmacy_id") pharmacy_id: Long
    ): Call<PharmacyOrderResponse>

    @POST("pharmacy/accept/order")
    fun acceptPharmacyOrder(@Query("api_token") api_token: String
                            , @Query("pharmacy_id") pharmacy_id: Long
                            , @Query("order_id") order_id: Long
    ): Call<BaseResponse>

    @Multipart
    @POST("pharmacy_update_profile")
    fun updatePharmacy(@Part image: MultipartBody.Part?
                       ,@Part("user_id") user_id: RequestBody
                       , @Part("name") name: RequestBody
                       , @Part("api_token") api_token: RequestBody
                       , @Part("delivery") delivery: RequestBody
                       , @Part("lng") lng: RequestBody
                       , @Part("lat") lat: RequestBody
                       , @Part("working_hours") working_hours: RequestBody
                       , @Part("insurance") insurance: RequestBody
                       , @Part("city_id") city_id: RequestBody
                       , @Part("area_id") area_id: RequestBody
                       , @Part("phone2") phone: RequestBody
                       , @Part("email") email: RequestBody
    ): Call<PharmacyResponse>


    ////////////////////  Lab  ///////////////////////////////////

    @GET("lab/order/get")
    fun getLabOrders(@Query("api_token") api_token: String
                          , @Query("lab_id") lab_id: Long
    ): Call<LabOrderResponse>

    @Multipart
    @POST("lab_update_profile")
    fun updateProfileLab(@Part image: MultipartBody.Part?
                         , @Part("api_token") api_token: RequestBody
                         ,@Part("user_id") user_id: RequestBody
                        , @Part("name") name: RequestBody
                        , @Part("delivery") delivery: RequestBody
                        , @Part("lng") lng: RequestBody
                         , @Part("lat") lat: RequestBody
                        , @Part("working_hours") working_hours: RequestBody
                        , @Part("email") email: RequestBody
                        , @Part("phone2") phone: RequestBody
                        , @Part("city_id") city_id: RequestBody
                        , @Part("area_id") area_id: RequestBody
                        , @Part("insurance") insurance: RequestBody)
            : Call<LabResponse>

    ////////////////////  Radiology  ///////////////////////////////////

    @GET("radiology/order/get")
    fun getRadiologyOrders(@Query("api_token") api_token: String
                     , @Query("radiology_id") radiology_id: Long
    ): Call<RadiologyOrderResponse>

    @Multipart
    @POST("radiology_update_profile")
    fun updateProfileRadiology(@Part image: MultipartBody.Part?
                               , @Part("api_token") api_token: RequestBody
                               ,@Part("user_id") user_id: RequestBody
                               , @Part("name") name: RequestBody
                               , @Part("delivery") delivery: RequestBody
                               , @Part("lng") lng: RequestBody
                               , @Part("lat") lat: RequestBody
                               , @Part("email") email: RequestBody
                               , @Part("phone2") phone: RequestBody
                               , @Part("working_hours") working_hours: RequestBody
                               , @Part("city_id") city_id: RequestBody
                               , @Part("area_id") area_id: RequestBody
                               , @Part("insurance") insurance: RequestBody)
            : Call<RadiologyResponse>


    ////////////////////  Radiology And Lab ///////////////////////////////////

    @FormUrlEncoded
    @POST("accept-order")
    fun acceptLabOrRadiologyOrder(@Field("api_token") api_token: String
                                  , @Field("user_id") user_id: Long
                                  , @Field("order_id") order_id: Long
                                  , @Field("user_type") user_type: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("cancel-order")
    fun cancelLabOrRadiologyOrder(@Field("api_token") api_token: String
                                  , @Field("user_id") user_id: Long
                                  , @Field("order_id") order_id: Long
                                  , @Field("user_type") user_type: String
                                  , @Field("message") message: String
    ): Call<BaseResponse>

    ////////////////////  Notification  ///////////////////////////////////

    @GET("get/notification")
    fun getNotifications(@Query("user_id") user_id: Long
                         ,@Query("user_type") user_type: String
                         ,@Query("api_token") api_token: String)
            : Call<NotificationsResponse>

    @FormUrlEncoded
    @POST("notification/remove")
    fun removeNotifications(@Field("notification_id") notification_id: Int)
            : Call<BaseResponse>

    ////////////////////// Payment ///////////////////////////

    @POST("auth/tokens")
    fun paymentAuth(@Body body : PaymentAuthRequest) : Call<PaymentAuthResponse>

    @POST("ecommerce/orders")
    fun orderRegisteration(@Body body: OrderRegisterationRequest) : Call<OrderRegisterationResponse>

    @POST("acceptance/payment_keys")
    fun getPaymentToken(@Body body: CardPaymentKeyRequest) : Call<CardPaymentKeyResposne>


}