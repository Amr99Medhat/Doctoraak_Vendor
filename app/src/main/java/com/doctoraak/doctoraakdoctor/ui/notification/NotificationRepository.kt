package com.doctoraak.doctoraakdoctor.ui.notification

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiMain
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.model.NotificationsResponse
import com.doctoraak.doctoraakdoctor.utils.UserType


object NotificationRepository {

    fun getNotifications(
        userId: Long, userType: UserType, api_token: String
        , success: (NotificationsResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int, String?)->Unit) = ApiMain.getNotifications(userId, userType, api_token
            , object : BaseResponseListener<NotificationsResponse> {
                override fun onSuccess(model: NotificationsResponse) {
                    success(model)
                }
                override fun onLoading()
                {
                    loading()
                }
                override fun onErrorMsg(errorMsg: String) {
                    errorMsg(errorMsg)
                }
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
            })

    fun removeNotifications(notificationId: Int
        , success: (BaseResponse) -> Unit
        , loading: () -> Unit
        , errorMsg: (String) -> Unit, error: (Int, String?)->Unit) = ApiMain.removeNotifications(notificationId
        , object : BaseResponseListener<BaseResponse> {
            override fun onSuccess(model: BaseResponse) {
                success(model)
            }
            override fun onLoading()
            {
                loading()
            }
            override fun onErrorMsg(errorMsg: String) {
                errorMsg(errorMsg)
            }
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })


}