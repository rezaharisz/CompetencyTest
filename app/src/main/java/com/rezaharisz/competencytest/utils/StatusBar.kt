package com.rezaharisz.competencytest.utils

import android.content.res.Resources

class StatusBar {
    fun getHeight(resources: Resources): Int {
        val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(statusBarHeightId)
    }
}