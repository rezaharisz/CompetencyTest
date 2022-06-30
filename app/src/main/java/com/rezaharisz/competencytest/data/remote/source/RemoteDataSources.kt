package com.rezaharisz.competencytest.data.remote.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rezaharisz.competencytest.data.remote.model.ResponseListUser
import com.rezaharisz.competencytest.data.remote.network.ApiCall
import com.rezaharisz.competencytest.utils.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemoteDataSources {

    private val apiService = ApiCall.retrofitService

    fun getResponseUser(
        visibleItem: Int,
        itemCount: Int
    ): LiveData<ApiResponse<List<ResponseListUser>>> {
        val result: MutableLiveData<ApiResponse<List<ResponseListUser>>> = MutableLiveData()
        var page = 1
        val perPage = 10
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["per_page"] = perPage.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getUser(params)
            if (response.isSuccessful) {
                val totalPage = response.body()?.totalPages

                totalPage?.let {
                    if (page < totalPage) {
                        if (visibleItem >= itemCount) {
                            page++
                            Log.e("CHECK1", visibleItem.toString())
                            Log.e("CHECK2", itemCount.toString())
                        }
                    }
                }

                result.postValue(response.body()?.data?.let { ApiResponse.success(it) })
            }
        }

        return result
    }

}