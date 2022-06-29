package com.rezaharisz.competencytest.pages

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.rezaharisz.competencytest.R
import com.rezaharisz.competencytest.pages.welcome.WelcomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        setTransparentStatusBar()

        if (savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frame_container, WelcomeFragment(), WelcomeFragment::class.java.canonicalName)
                .commit()
        }
    }

    private fun setTransparentStatusBar(){
        window.statusBarColor = Color.TRANSPARENT
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

}