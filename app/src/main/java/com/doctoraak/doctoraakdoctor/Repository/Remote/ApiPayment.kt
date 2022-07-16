package com.doctoraak.doctoraakdoctor.Repository.Remote

import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.start
import retrofit2.Call

object ApiPayment
{

    fun paymentAuth(apiKey : String,callback: BaseResponseListener<PaymentAuthResponse>){
        val service = ApiConfigure.paymentRetrofit.create(ApiEndPoint::class.java)
        val body = PaymentAuthRequest(apiKey)
        val call: Call<PaymentAuthResponse> = service.paymentAuth(body)
        call.start(callback)
    }

    fun orderRegisteration(request: OrderRegisterationRequest, callback: BaseResponseListener<OrderRegisterationResponse>){
        val service = ApiConfigure.paymentRetrofit.create(ApiEndPoint::class.java)
        val call: Call<OrderRegisterationResponse> = service.orderRegisteration(request)
        call.start(callback)
    }

    fun getPaymentToken(request: CardPaymentKeyRequest, callback: BaseResponseListener<CardPaymentKeyResposne>){
        val service = ApiConfigure.paymentRetrofit.create(ApiEndPoint::class.java)
        val call: Call<CardPaymentKeyResposne> = service.getPaymentToken(request)
        call.start(callback)
    }

}