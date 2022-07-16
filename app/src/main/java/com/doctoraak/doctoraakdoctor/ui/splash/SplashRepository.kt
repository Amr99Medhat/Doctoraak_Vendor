package com.doctoraak.doctoraakdoctor.ui.splash

import com.doctoraak.doctoraakdoctor.Repository.Remote.ApiMain
import com.doctoraak.doctoraakdoctor.Repository.Remote.BaseResponseListener
import com.doctoraak.doctoraakdoctor.model.*

object SplashRepository
{
    fun getSpecialization(success: (SpecializationResponse)->Unit
                          , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiMain.getSpecializations(object : BaseResponseListener<SpecializationResponse> {
            override fun onSuccess(model: SpecializationResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun getDegree(success: (DegreeResponse)->Unit
                  , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiMain.getDegrees(object : BaseResponseListener<DegreeResponse> {
            override fun onSuccess(model: DegreeResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun getInsurances(success: (InsuranceResponse)->Unit
                      , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiMain.getInsurances(object : BaseResponseListener<InsuranceResponse> {
            override fun onSuccess(model: InsuranceResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun getCity(success: (CityResponse)->Unit
                      , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiMain.getCity(object : BaseResponseListener<CityResponse> {
            override fun onSuccess(model: CityResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun getArea(success: (AreaResponse)->Unit
                      , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiMain.getArea(callback = object : BaseResponseListener<AreaResponse> {
            override fun onSuccess(model: AreaResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

    fun getContact(success: (ContactResponse)->Unit
                      , loading: ()->Unit, errorMsg: (String)->Unit, error: (Int, String?)->Unit)
    {
        ApiMain.getContact(object : BaseResponseListener<ContactResponse> {
            override fun onSuccess(model: ContactResponse) {
                success(model)
            }
            override fun onLoading() = loading()
            override fun onErrorMsg(errorMsg: String) = errorMsg(errorMsg)
            override fun onError(errorTitleOrBodyId: Int, errorMsg: String?) = error(errorTitleOrBodyId, errorMsg)
        })
    }

}