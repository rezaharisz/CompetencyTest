package com.rezaharisz.competencytest.data.remote.source

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rezaharisz.competencytest.data.remote.model.ResponseListUser
import com.rezaharisz.competencytest.data.remote.model.ResponseUser
import com.rezaharisz.competencytest.data.remote.network.ApiCall
import com.rezaharisz.competencytest.utils.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemoteDataSources {

    private val apiService = ApiCall.retrofitService
    private val handler = Handler(Looper.getMainLooper())

    fun getResponseUser(params: HashMap<String, String>): LiveData<ApiResponse<List<ResponseListUser>>> {
        val result: MutableLiveData<ApiResponse<List<ResponseListUser>>> = MutableLiveData()

        handler.postDelayed({
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.getUser(params)

                if (response.isSuccessful){
                    result.postValue(response.body()?.data?.let { ApiResponse.success(it) })
                }
            }
        }, 2000)

        return result
    }

}