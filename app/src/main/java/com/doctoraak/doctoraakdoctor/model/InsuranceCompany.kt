package com.doctoraak.doctoraakdoctor.model


import com.google.gson.annotations.SerializedName

data class InsuranceCompany(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_ar")
    val nameAr: String,
    @SerializedName("name_fr")
    val nameFr: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("url")
    val url: String
)