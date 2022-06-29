package com.rezaharisz.competencytest.data.remote.network

import com.rezaharisz.competencytest.BuildConfig.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiCall {

    private val client = OkHttpClient.Builder().build()
    val retrofitService: ApiService by lazy {
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            client(client)
            baseUrl(BASE_URL)
        }
            .build()
            .create()
    }

}