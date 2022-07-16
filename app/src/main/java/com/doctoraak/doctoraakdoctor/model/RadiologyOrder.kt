package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class RadiologyOrderResponse(
    @SerializedName("data")
    var data: ArrayList<RadiologyOrder>?
) : BaseResponse()

data class RadiologyOrder(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("details")
    var details: List<RadiologyDetail> = listOf(),
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("patient")
    var patient: Patient = Patient(),
    @SerializedName("insurance_accept")
    var insuranceAccept: String = "",
    @SerializedName("insurance_code")
    var insuranceCode: String = "",
    @SerializedName("notes")
    var notes: String = "",
    @SerializedName("patient_id")
    var patientId: Long = 0,
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("radiology_id")
    var radiologyId: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

data class RadiologyDetail(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("radiology_order")
    var radiologyOrder: Int = 0,
    @SerializedName("rays")
    var rays: Rays = Rays(),
    @SerializedName("rays_id")
    var raysId: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

data class Rays(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("name_ar")
    var nameAr: String = "",
    @SerializedName("name_fr")
    var nameFr: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
)


