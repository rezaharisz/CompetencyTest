package com.rezaharisz.competencytest.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rezaharisz.competencytest.data.local.model.UserListEntity
import com.rezaharisz.competencytest.data.local.model.UserLoginEntity

@Database(entities = [UserLoginEntity::class, UserListEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object{

        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase{
            if (INSTANCE == null){
                synchronized(UserDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_database.db"
                    ).build()
                }
            }
            return INSTANCE as UserDatabase
        }

    }

}