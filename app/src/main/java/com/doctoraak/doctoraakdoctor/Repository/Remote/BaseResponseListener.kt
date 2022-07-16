package com.doctoraak.doctoraakdoctor.Repository.Remote

import com.doctoraak.doctoraakdoctor.R

interface BaseResponseListener<T>
{
    fun onSuccess(model: T)
    fun onLoading()
    fun onErrorMsg(errorMsg: String)
    /**
     * @param errorTitleId is the string id which you can get it like: ('getString(errorMsgId)')
     * called when the request is failed
     * */
    fun onError(errorTitleOrBodyId: Int = R.string.error, errorMsg: String? = null)
}