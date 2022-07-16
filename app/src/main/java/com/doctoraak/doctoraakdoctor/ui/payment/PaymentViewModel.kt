package com.doctoraak.doctoraakdoctor.ui.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

internal class PaymentViewModel : ViewModel()
{
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }
    val isFawry by lazy { SingleLiveData<BaseResponse>() }
    val phone by lazy { SingleLiveData<PhoneResponse>() }
    val paymentAuthResponse: MutableLiveData<PaymentAuthResponse> by lazy { MutableLiveData<PaymentAuthResponse>() }
    val orderRegisterationResponse: MutableLiveData<OrderRegisterationResponse> by lazy { MutableLiveData<OrderRegisterationResponse>() }
    val paymentTokenResponse: MutableLiveData<CardPaymentKeyResposne> by lazy { MutableLiveData<CardPaymentKeyResposne>() }


    fun confirmCode(){}

    fun getPhone() = PaymentRepository
        .getPhone( success = { model: PhoneResponse ->
            isLoading.setValue(false)
            phone.setValue(model)}
            , loading = { isLoading.setValue(true) }
            , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
            , error = {i, s ->
                errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})


    fun orderRegisteration(request: OrderRegisterationRequest) {
        PaymentRepository.orderRegisteration( request,success = {
            orderRegisterationResponse.setValue(it); isLoading.setValue(false)
        }, loading = { isLoading.setValue(true) }
            , errorMsg = { isLoading.setValue(false) ;errorMsg.setValue(it) }
            , error = {i, s -> isLoading.setValue(false) ; errorInt.setValue(Pair(i, s))})
    }

    fun getCardPaymentToken(request: CardPaymentKeyRequest) {
        PaymentRepository.getPaymentToken( request,success = {
            paymentTokenResponse.setValue(it); isLoading.setValue(false)
        }, loading = { isLoading.setValue(true) }
            , errorMsg = { isLoading.setValue(false) ;errorMsg.setValue(it) }
            , error = {i, s -> isLoading.setValue(false) ; errorInt.setValue(Pair(i, s))})
    }

    fun paymentAuth(apiKey: String) {
        PaymentRepository.paymentAuth( apiKey, success = {
            paymentAuthResponse.setValue(it); isLoading.setValue(false)
        }, loading = { isLoading.setValue(true) }
            , errorMsg = { isLoading.setValue(false) ;errorMsg.setValue(it) }
            , error = {i, s -> isLoading.setValue(false) ; errorInt.setValue(Pair(i, s))})
    }




}