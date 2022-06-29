package com.rezaharisz.competencytest.data.local.source

import android.app.Application
import androidx.paging.DataSource
import com.rezaharisz.competencytest.data.local.model.UserListEntity
import com.rezaharisz.competencytest.data.local.model.UserLoginEntity
import com.rezaharisz.competencytest.data.local.room.UserDao
import com.rezaharisz.competencytest.data.local.room.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalDataSources(application: Application){

    private val userDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        userDao = db.userDao()
    }

    fun getAllUsers(): DataSource.Factory<Int, UserListEntity> = userDao.getUsers()

    fun insertUser(userLoginEntity: UserLoginEntity) =
        executorService.execute { userDao.insertUser(userLoginEntity) }

    fun insertListUser(listUser: List<UserListEntity>) =
        executorService.execute { userDao.insertListUser(listUser) }

    fun deleteAll() = executorService.execute { userDao.deleteAll() }

}