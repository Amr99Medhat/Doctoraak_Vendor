package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

open class BaseResponse
{
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("message")
    var msg: String? = ""
    @SerializedName("message_en")
    var message_en: String? = ""
}