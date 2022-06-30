package com.rezaharisz.competencytest.pages.guest

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.rezaharisz.competencytest.data.Repository
import com.rezaharisz.competencytest.data.local.model.UserListEntity
import com.rezaharisz.competencytest.utils.Resource

class GuestViewModel(application: Application): ViewModel() {

    private val repository = Repository(application)

    fun getAllUser(visibleItem: Int, itemCount: Int): LiveData<Resource<PagedList<UserListEntity>>> =
        repository.getAllUser(visibleItem, itemCount)

    fun clearData() = repository.deleteAll()

}