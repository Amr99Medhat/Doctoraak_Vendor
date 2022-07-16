package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

open class Main(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("name_ar")
    var name_ar: String = "",
    /** Not used in the Application. */
    @SerializedName("name_fr")
    var name_fr: String = "",
    @SerializedName("created_at")
    var created_at: String = "",
    @SerializedName("updated_at")
    var updated_at: String = ""
)