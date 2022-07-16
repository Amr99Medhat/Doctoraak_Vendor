package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Reservation(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("clinic")
    var clinic: Clinic = Clinic(),
    @SerializedName("clinic_id")
    var clinicId: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("date")
    var date: String = "",
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("notes")
    var notes: String = "",
    @SerializedName("part_id")
    var partId: Int = 0,
    @SerializedName("patient")
    var patient: Patient = Patient(),
    @SerializedName("patient_id")
    var patientId: Int = 0,
    @SerializedName("reservation_number")
    var reservationNumber: Int = 0,
    @SerializedName("type")
    var type: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

data class ReservationResponse(
    @SerializedName("data")
    var data: List<Reservation>?
) : BaseResponse()