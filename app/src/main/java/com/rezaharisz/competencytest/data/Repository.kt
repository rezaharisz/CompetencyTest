package com.rezaharisz.competencytest.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.rezaharisz.competencytest.data.local.model.UserListEntity
import com.rezaharisz.competencytest.data.local.model.UserLoginEntity
import com.rezaharisz.competencytest.data.local.source.LocalDataSources
import com.rezaharisz.competencytest.data.remote.model.ResponseListUser
import com.rezaharisz.competencytest.data.remote.source.RemoteDataSources
import com.rezaharisz.competencytest.helper.NetworkBoundResource
import com.rezaharisz.competencytest.utils.ApiResponse
import com.rezaharisz.competencytest.helper.AppExecutors
import com.rezaharisz.competencytest.utils.Resource

class Repository(application: Application) {

    private val localDataSources = LocalDataSources(application)
    private val remoteDataSources = RemoteDataSources()
    private val appExecutors = AppExecutors()

    fun insertUser(userLoginEntity: UserLoginEntity) = localDataSources.insertUser(userLoginEntity)

    fun getAllUser(
        visibleItem: Int,
        itemCount: Int
    ): LiveData<Resource<PagedList<UserListEntity>>> {
        return object :
            NetworkBoundResource<PagedList<UserListEntity>, List<ResponseListUser>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<UserListEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(localDataSources.getAllUsers(), config).build()
            }

            override fun shouldFetch(data: PagedList<UserListEntity>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<ResponseListUser>>> {
                return remoteDataSources.getResponseUser(visibleItem, itemCount)
            }

            override fun saveCallResult(data: List<ResponseListUser>) {
                val listUser = ArrayList<UserListEntity>()
                for (i in data) {
                    val user = i.id?.let {
                        UserListEntity(
                            it,
                            i.email,
                            i.firstName,
                            i.lastName,
                            i.avatar
                        )
                    }
                    if (user != null) {
                        listUser.add(user)
                    }
                }
                localDataSources.insertListUser(listUser)
            }
        }.asLiveData()
    }

    fun deleteAll() = localDataSources.deleteAll()

}