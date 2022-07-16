package com.doctoraak.doctoraakdoctor.model

data class NotificationsResponse(
    val data: ArrayList<NotificationInfo>?
) : BaseResponse()

data class NotificationInfo(
    val created_at: String,
    var icon: String?,
    val id: Int,
    val message_ar: String,
    val message_en: String,
    val order_id: Long?,
    val seen: Int,
    val sent: Int,
    val title_ar: String,
    val title_en: String,
    val updated_at: String,
    val user_id: Int,
    val user_type: String,
    val order: Any?
)