package com.doctoraak.doctoraakdoctor.Repository.Remote

import com.google.gson.annotations.SerializedName

class ApiError(
    @SerializedName("statusCode")
    var statusCode: Int = 0,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("endpoint")
    var endpoint: String = ""
)