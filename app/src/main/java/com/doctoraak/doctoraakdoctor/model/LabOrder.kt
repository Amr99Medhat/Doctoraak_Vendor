package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class LabOrderResponse(
    @SerializedName("data")
    var data: ArrayList<LabOrder>?
) : BaseResponse()

data class LabOrder(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("details")
    var details: List<LabDetail> = listOf(),
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("insurance_accept")
    var insuranceAccept: String = "",
    @SerializedName("insurance_code")
    var insuranceCode: String = "",
    @SerializedName("lab_id")
    var labId: Long = 0,
    @SerializedName("notes")
    var notes: String = "",
    @SerializedName("patient_id")
    var patientId: Long = 0,
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("patient")
    var patient: Patient = Patient(),
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

data class LabDetail(
    @SerializedName("analysis")
    var analysis: Analysis = Analysis(),
    @SerializedName("analysis_id")
    var analysisId: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("lab_order")
    var labOrder: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = ""
)


data class Analysis(
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

