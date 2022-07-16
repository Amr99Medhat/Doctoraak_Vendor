package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

class Degree : Main()

data class DegreeResponse(
    @SerializedName("data")
    var list: List<Degree>?
) : BaseResponse()