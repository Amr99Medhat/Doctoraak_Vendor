package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class Doctor(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("cv")
    var cv: String = "",
    @SerializedName("degree_id")
    var degreeId: Int = -1,
    @SerializedName("email")
    var email: String = "",
    @SerializedName("gender")
    var gender: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("sms_code")
    var smsCode: Int = -1,
    @SerializedName("specialization_id")
    var specializationId: Int = -1,
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("firebase_token")
    var firebaseToken: String = "",
    @SerializedName("avaliable_days")
    var avaliableDays: Int = 0,
    @SerializedName("insurance")
    var insuranceIds: String = "", // in format "[1,2,3]"
    @SerializedName("reservation_rate")
    var reservationRate: String = "",
    @SerializedName("doctor_insurances")
    var insuranceList: List<InsuranceDoctor> = listOf(),
    @SerializedName("insurance_company")
    var insuranceCompany: List<InsuranceCompany> = listOf(),
    @SerializedName("title")
    var title: String = "",
    @SerializedName("phone2")
    var phone2: String = ""
): User()


class DoctorResponse(
    @SerializedName("data")
    var data: Doctor?
) : BaseResponse()


data class InsuranceDoctor(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("doctor_id")
    var doctor_id: Long = 0,
    @SerializedName("insurance_id")
    var insurance_id: Int = 0,
    @SerializedName("created_at")
    var created_at: String = "",
    @SerializedName("updated_at")
    var updated_at: String = ""
)