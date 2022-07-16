package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class Specialization(
    @SerializedName("icon")
    var icon: String = ""
) : Main()

data class SpecializationResponse(
    @SerializedName("data")
    var data: List<Specialization>?
) : BaseResponse()