package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

open class User
{
    @SerializedName("api_token")
    var api_token: String = ""
    @SerializedName("id")
    var id: Long = 0
}