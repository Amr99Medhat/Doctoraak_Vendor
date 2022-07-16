package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class PharmacyOrderResponse(
    @SerializedName("data")
    var data: List<PharmacyOrder>?
): BaseResponse()

class PharmacyOrder(
    @SerializedName("accept")
    var accept: String = "",
    @SerializedName("patient")
    var patient: Patient = Patient(),
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("details")
    var details: List<MedicineDetail> = listOf(),
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("insurance_accept")
    var insuranceAccept: String = "",
    @SerializedName("insurance_code")
    var insuranceCode: String = "",
    @SerializedName("notes")
    var notes: String = "",
    @SerializedName("patient_id")
    var patientId: Long = 0,
    @SerializedName("pharmacy_id")
    var pharmacyId: Long = 0,
    @SerializedName("pharmacy_order_id")
    var pharmacyOrderId: Long = 0,
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

data class MedicineDetail(
    @SerializedName("amount")
    var amount: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("medicine")
    var medicine: Medicine = Medicine(),
    @SerializedName("medicine_id")
    var medicineId: Int = 0,
    @SerializedName("medicine_type")
    var medicineType: MedicineType = MedicineType(),
    @SerializedName("medicine_type_id")
    var medicineTypeId: Int = 0,
    @SerializedName("pharmacy_order")
    var pharmacyOrder: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

data class Medicine(
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

data class MedicineType(
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