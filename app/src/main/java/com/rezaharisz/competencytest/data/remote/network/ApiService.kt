package com.rezaharisz.competencytest.data.remote.network

import com.rezaharisz.competencytest.data.remote.model.ResponseUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("users")
    suspend fun getUser(
        @QueryMap params: HashMap<String, String>
    ): Response<ResponseUser>

}