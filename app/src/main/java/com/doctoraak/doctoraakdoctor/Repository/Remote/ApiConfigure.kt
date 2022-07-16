package com.doctoraak.doctoraakdoctor.Repository.Remote

import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiConfigure
{
    val BASE_URL: String by lazy { "http://v2.doctoraak.com/api/" }
    val BASE_URL_EXCEPT_API: String by lazy { "http://v2.doctoraak.com/" }
    val PAYMENT_URL : String by lazy { "https://accept.paymobsolutions.com/api/" }

    internal val mainRetrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    internal val paymentRetrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .callTimeout(2 , TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .build()

        Retrofit.Builder()
            .baseUrl(PAYMENT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createRequestBody(data: String)
            = RequestBody.create( MediaType.parse("text/plain"), data)

    fun createRequestBodyImage(file: File)
            = RequestBody.create( MediaType.parse("image/*"), file)

    fun createRequestBodyPdf(file: File)
            = RequestBody.create( MediaType.parse("application/pdf"), file)


}