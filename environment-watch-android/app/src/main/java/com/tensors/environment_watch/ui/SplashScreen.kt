package com.tensors.environment_watch.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import com.firebase.ui.auth.AuthUI
import com.tensors.environment_watch.R
import com.tensors.environment_watch.ui.mainscreen.MainScreen
import com.tensors.environment_watch.ui.welcomescreen.WelcomeActivity

class SplashScreen: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build())
        Handler().postDelayed({
            if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("finishedWelcome", false)) {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), 0)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            startActivity(Intent(this, MainScreen::class.java))
            finish()
        }
    }
}