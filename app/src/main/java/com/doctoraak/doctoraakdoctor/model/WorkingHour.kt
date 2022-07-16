package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class WorkingHour(
    @SerializedName("active")
    var active: Int = 0,
    /** days is from 1 -> 7 */
    @SerializedName("day")
    var day: Int = 0,
    @SerializedName("part_from")
    var part_from: String = "",
    @SerializedName("part_to")
    var part_to: String = ""
)