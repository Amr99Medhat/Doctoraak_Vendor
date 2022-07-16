package com.doctoraak.doctoraakdoctor.Repository.Remote

import android.util.Log
import com.doctoraak.doctoraakdoctor.model.BaseResponse
import com.doctoraak.doctoraakdoctor.R
import com.google.gson.Gson
import java.io.IOException
import java.net.SocketException
import okhttp3.ResponseBody
import retrofit2.*
import java.io.FileNotFoundException


class BaseCallBack<T>(val listener: BaseResponseListener<T>) : Callback<T>
{
    override fun onResponse(call: Call<T>, response: Response<T>)
    {
        Log.d("saif", "--------------------------------------------------- ${call.request().url()} ---------------------------------------------------------")
        Log.d("saif", "onResponse code= "+response.code())
        Log.d("saif", "onResponse data= "+Gson().toJson(response.body()))
        Log.d("saif", "onResponse error= "+Gson().toJson(response.errorBody()))
        Log.d("saif", "---------------------------------------------------------------------------------------------------------------\n")

        if (response.isSuccessful)
        {
            response.body().let {

                if (it is BaseResponse) Log.d("saif", "onResponse status= "+(it as BaseResponse).status)
               
                if ((it as BaseResponse).status == 1)
                    listener.onSuccess(it)
                else
                {
                    Log.d("saif", "onResponse msg= "+(it as BaseResponse).msg)
                    listener.onErrorMsg((it as BaseResponse).msg ?: "")
                }
            }
        }
        else
        {
            val apiError = response.parseError()
            when (response.code())
            {
                401 -> listener.onError(R.string.unauthorized_user, apiError.message)
                500 -> listener.onError(R.string.server_error, apiError.message)
                else -> listener.onError(R.string.unexpected_error, apiError.message)
            }
        }

    }

    override fun onFailure(call: Call<T>, t: Throwable)
    {
        Log.d("saif", "--------------------------------------------------- ${call.request().url()} ---------------------------------------------------------")

        Log.e("saif", "onFailed, "+t.message, t)
        listener.onError(t.handleError(), t.message)
    }

}

private fun Response<*>.parseError(): ApiError
{
    try {
        val converter: Converter<ResponseBody, ApiError> = ApiConfigure.mainRetrofit
            .responseBodyConverter(ApiError::class.java, arrayOfNulls<Annotation>(0))

        var error: ApiError? = null
        this.errorBody()?.let {
            try {
                error = converter.convert(it)
            }catch (e: IOException)
            {
                error = ApiError()
            }
        }

        return error ?: ApiError()
    }catch (e: Exception)
    {
        return ApiError()
    }
}

private fun Throwable.handleError(): Int = when (this)
{
    is HttpException ->
    {
        when (code())
        {
            400 -> R.string.bad_request
            401 -> R.string.unauthorized_user
            404 -> R.string.request_not_found
            500 -> R.string.server_error
            else -> R.string.unexpected_error
        }
    }
    is SocketException -> R.string.not_connected_to_network
    is FileNotFoundException -> R.string.file_not_found_
    is IOException -> R.string.network_error
    else -> R.string.unexpected_error
}
