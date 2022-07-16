package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

class City : Main()

data class CityResponse(
    @SerializedName("data")
    var list: List<City>?
) : BaseResponse()