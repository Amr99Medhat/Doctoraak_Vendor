package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

data class ShiftWorkingHour(
    @SerializedName("active")
    var active: Int = 0,
    /** days is from 1 -> 7 */
    @SerializedName("day")
    var day: Int = 0,
    @SerializedName("part1_from")
    var part1_from: String? = "",
    @SerializedName("part1_to")
    var part1_to: String? = "",
    @SerializedName("part2_from")
    var part2_from: String? = "",
    @SerializedName("part2_to")
    var part2_to: String? = ""
)