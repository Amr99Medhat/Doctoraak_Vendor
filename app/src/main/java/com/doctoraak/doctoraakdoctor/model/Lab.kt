package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class Lab(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("sms_code")
    var smsCode: Int = -1,
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("insurance")
    var insuranceIds: String = "", // in format "[1,2,3]"
    @SerializedName("delivery")
    var delivery: Int = 0,
    @SerializedName("lng")
    var lang: String = "",
    @SerializedName("lat")
    var latt: String = "",
    @SerializedName("city_id")
    var city_id: Int = -1,
    @SerializedName("area_id")
    var area_id: Int = -1,
    @SerializedName("city")
    var city: City = City(),
    @SerializedName("area")
    var area: Area = Area(),
    @SerializedName("firebase_token")
    var firebaseToken: String = "",
    @SerializedName("avaliable_days")
    var avaliableDays: Int = 0,
    @SerializedName("working_hours")
    var workingHours: List<WorkingHour> = listOf(),
    @SerializedName("lab_insurances")
    var insuranceList: List<InsuranceLab> = listOf(),
    @SerializedName("insurance_company")
    var insuranceCompany: List<InsuranceCompany> = listOf()
): User()


class LabResponse(
    @SerializedName("data")
    var data: Lab?
) : BaseResponse()


data class InsuranceLab(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("lab_id")
    var pharmacy_id: Long = 0,
    @SerializedName("insurance_id")
    var insurance_id: Int = 0,
    @SerializedName("created_at")
    var created_at: String = "",
    @SerializedName("updated_at")
    var updated_at: String = ""
)