package com.rezaharisz.competencytest.pages.welcome

import android.app.Application
import androidx.lifecycle.ViewModel
import com.rezaharisz.competencytest.data.Repository
import com.rezaharisz.competencytest.data.local.model.UserLoginEntity

class WelcomeViewModel(application: Application): ViewModel() {

    private val repository = Repository(application)

    fun insertUser(userLoginEntity: UserLoginEntity) = repository.insertUser(userLoginEntity)

}