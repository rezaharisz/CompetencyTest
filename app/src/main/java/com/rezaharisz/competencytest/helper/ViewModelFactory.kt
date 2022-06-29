package com.rezaharisz.competencytest.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezaharisz.competencytest.pages.guest.GuestViewModel
import com.rezaharisz.competencytest.pages.welcome.WelcomeViewModel

class ViewModelFactory private constructor(private val application: Application): ViewModelProvider.NewInstanceFactory(){

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when{
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                return WelcomeViewModel(application) as T
            }

            modelClass.isAssignableFrom(GuestViewModel::class.java) -> {
                return GuestViewModel(application) as T
            }
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}