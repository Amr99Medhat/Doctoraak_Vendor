package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class Clinic(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("availability")
    var availability: Int = 0,
    @SerializedName("available_days")
    var availableDays: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("doctor")
    var doctor: Doctor = Doctor(),
    @SerializedName("doctor_id")
    var doctorId: Long = 0,
    @SerializedName("fees")
    var fees: String = "",
    @SerializedName("fees2")
    var fees2: String = "",
    @SerializedName("notes")
    var notes: String = "",
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("lng")
    var lang: String = "",
    @SerializedName("lat")
    var latt: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("waiting_time")
    var waitingTime: String = "",
    @SerializedName("area")
    var area: Area = Area(),
    @SerializedName("city")
    var city: City = City(),
    @SerializedName("city_id")
    var city_id: Int = -1,
    @SerializedName("area_id")
    var area_id: Int = -1,
    @SerializedName("working_hours")
    var shiftWorkingHours: List<ShiftWorkingHour> = listOf()
)


data class ClinicResponse(
    @SerializedName("data")
    var data: Clinic?
) : BaseResponse()

data class AllClinicsResponse(
    @SerializedName("data")
    var data: List<Clinic>?
) : BaseResponse()