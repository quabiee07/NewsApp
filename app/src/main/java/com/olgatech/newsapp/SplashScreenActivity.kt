package com.olgatech.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {
    private val TIME_OUT:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        loadSplashScreen()
    }

    fun loadSplashScreen(){
        Handler().postDelayed({ // This method will be executed once the timer is over
            // Start app main activity
            val intent = Intent(this@SplashScreenActivity, NewsActivity::class.java)
            startActivity(intent)
            finish()
        }, TIME_OUT)
    }
}