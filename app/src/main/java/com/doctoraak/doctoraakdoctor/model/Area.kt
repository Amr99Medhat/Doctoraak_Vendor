package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

class Area(
    @SerializedName("city_id")
    var city_id: Int = 0
) : Main()

data class AreaResponse(
    @SerializedName("data")
    var list: List<Area>?
) : BaseResponse()