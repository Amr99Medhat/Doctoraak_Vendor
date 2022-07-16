package com.doctoraak.doctoraakdoctor.ui.payment

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiMain
import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiPayment
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

object PaymentRepository
{
    fun getPhone(success: (PhoneResponse)->Unit, loading: ()->Unit
                 , errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiMain.getPhone(object : BaseResponseListener<PhoneResponse> {
            override fun onSuccess(model: PhoneResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun getPaymentToken(request: CardPaymentKeyRequest, success: (CardPaymentKeyResposne)->Unit, loading: ()->Unit
                 , errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiPayment.getPaymentToken(request, object : BaseResponseListener<CardPaymentKeyResposne> {
            override fun onSuccess(model: CardPaymentKeyResposne) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun paymentAuth(apiKey: String, success: (PaymentAuthResponse)->Unit, loading: ()->Unit
                    , errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiPayment.paymentAuth(apiKey, object : BaseResponseListener<PaymentAuthResponse> {
            override fun onSuccess(model: PaymentAuthResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }


    fun orderRegisteration(request: OrderRegisterationRequest, success: (OrderRegisterationResponse)->Unit, loading: ()->Unit
                           , errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiPayment.orderRegisteration(request, object : BaseResponseListener<OrderRegisterationResponse> {
            override fun onSuccess(model: OrderRegisterationResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }



}