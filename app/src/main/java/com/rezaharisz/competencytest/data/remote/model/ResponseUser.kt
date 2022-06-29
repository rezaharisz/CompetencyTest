package com.rezaharisz.competencytest.data.remote.model

import com.google.gson.annotations.SerializedName

data class ResponseUser(

    @field:SerializedName("data")
    val data: ArrayList<ResponseListUser>? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,

)
