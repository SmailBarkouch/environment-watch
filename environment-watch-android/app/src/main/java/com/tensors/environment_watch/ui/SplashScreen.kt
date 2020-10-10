package com.tensors.environment_watch.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import com.tensors.environment_watch.R
import com.tensors.environment_watch.ui.mainscreen.MainScreen
import com.tensors.environment_watch.ui.welcomescreen.WelcomeActivity

class SplashScreen: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Handler().postDelayed({
            if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("finishedWelcome", false)) {
                startActivity(Intent(this, MainScreen::class.java))
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }

            finish()
        }, 200)
    }
}