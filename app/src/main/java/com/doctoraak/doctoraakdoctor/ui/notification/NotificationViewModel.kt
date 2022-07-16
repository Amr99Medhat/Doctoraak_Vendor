package com.doctoraak.doctoraakdoctor.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.model.NotificationsResponse
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData
import com.doctoraak.doctoraakdoctor.utils.UserType

class NotificationViewModel : ViewModel()
{

    val notificationsResponse: MutableLiveData<NotificationsResponse>
            by lazy { MutableLiveData<NotificationsResponse>() }
    val removeNotification: SingleLiveData<BaseResponse> by lazy { SingleLiveData<BaseResponse>() }
    val isLoading: SingleLiveData<Boolean> by lazy { SingleLiveData<Boolean>() }
    val errorMsg: SingleLiveData<String> by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }


    fun getNotifications(userId: Long, userType: UserType, api_token: String) {

        NotificationRepository.getNotifications(userId,userType,api_token
            , success = {
            notificationsResponse.value = it; isLoading.setValue(false)
        }, loading = { isLoading.setValue(true) }
            , errorMsg = { isLoading.setValue(false) ;errorMsg.setValue(it) }
            , error = {i, s ->
                errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})
    }


    fun removeNotifications(notificationId: Int) {

        NotificationRepository.removeNotifications(notificationId
            , success = {
                removeNotification.setValue(it); isLoading.setValue(false)
            }, loading = { }
            , errorMsg = { errorMsg.setValue(it) }
            , error = {i, s -> errorInt.setValue(Pair(i, s)); })
    }


}