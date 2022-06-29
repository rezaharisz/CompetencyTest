package com.rezaharisz.competencytest.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.rezaharisz.competencytest.utils.ApiResponse
import com.rezaharisz.competencytest.utils.AppExecutors
import com.rezaharisz.competencytest.utils.Resource
import com.rezaharisz.competencytest.utils.StatusResponse

abstract class NetworkBoundResource<ResultType, RequestType>(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        @Suppress("LeakingThis")
        val dbSource = loadFromDB()

        result.addSource(dbSource){ resultType ->
            result.removeSource(dbSource)

            if (shouldFetch(resultType)){
                fetchFromNetwork(dbSource)
            } else{
                result.addSource(dbSource) { newResultType ->
                    result.value = Resource.success(newResultType)
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): LiveData<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    protected abstract fun saveCallResult(data: RequestType)

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>){

        val response = createCall()

        result.addSource(dbSource){
            result.value = Resource.loading(it)
        }

        result.addSource(response){ apiResponse ->
            result.removeSource(response)
            result.removeSource(dbSource)

            when(apiResponse.status){
                StatusResponse.SUCCESS -> appExecutors.diskIO().execute{
                    saveCallResult(apiResponse.body)
                    appExecutors.mainThread().execute {
                        result.addSource(loadFromDB()) {
                            result.value = Resource.success(it)
                        }
                    }
                }

                StatusResponse.EMPTY -> appExecutors.mainThread().execute {
                    result.addSource(loadFromDB()){
                        result.value = Resource.success(it)
                    }
                }

                StatusResponse.ERROR -> {
                    onFetchFailed()
                    result.addSource(dbSource){
                        result.value = Resource.error(apiResponse.message, it)
                    }
                }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<ResultType>> = result

}