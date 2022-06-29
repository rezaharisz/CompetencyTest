package com.rezaharisz.competencytest.data.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rezaharisz.competencytest.data.local.model.UserListEntity
import com.rezaharisz.competencytest.data.local.model.UserLoginEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_item")
    fun getUsers(): DataSource.Factory<Int, UserListEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(userLoginEntity: UserLoginEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListUser(listUser: List<UserListEntity>)

    @Query("DELETE FROM user_item")
    fun deleteAll()

}