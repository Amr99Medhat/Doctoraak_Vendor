package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName


data class Patient(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("address")
    var address: String = "",
    @SerializedName("address_ar")
    var addressAr: String = "",
    @SerializedName("address_fr")
    var addressFr: String = "",
    @SerializedName("api_token")
    var apiToken: String = "",
    @SerializedName("birthdate")
    var birthdate: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("firebase_token")
    var firebaseToken: String = "",
    @SerializedName("gender")
    var gender: String = "",
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("insurance_code")
    var insuranceCode: String = "",
    @SerializedName("insurance_id")
    var insuranceId: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("name_ar")
    var nameAr: String = "",
    @SerializedName("name_fr")
    var nameFr: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("sms_code")
    var smsCode: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
)