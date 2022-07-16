package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

class ShiftWorkingHoursRequest (
    @SerializedName("api_token")
    var api_token: String
    , @SerializedName("clinic_id")
    var clinic_id: Long
    , @SerializedName("doctor_id")
    var doctor_id: Long
    , @SerializedName("working_hours")
    var shiftWorkingHours: String
)