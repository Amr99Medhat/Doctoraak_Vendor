package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class Insurance(
    @SerializedName("photo")
    var photo: String = ""
) : Main()

data class InsuranceResponse(
    @SerializedName("data")
    var data: List<Insurance>?
) : BaseResponse()