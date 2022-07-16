package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class PhoneResponse(
    @SerializedName("data")
    var phone: String?
) : BaseResponse()