package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

class ContactResponse(
    @SerializedName("data")
    var list: List<String>?
) : BaseResponse()