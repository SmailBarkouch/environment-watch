package com.tensors.environment_watch.ui.welcomescreen

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.tensors.environment_watch.R
import com.tensors.environment_watch.ui.mainscreen.MainScreen
import kotlinx.android.synthetic.main.activity_welcome3.*

class ThirdWelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome3)

        take_me_home.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), 0
            )
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("finishedWelcome", true).apply()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            startActivity(Intent(this, MainScreen::class.java))
            finishAffinity()
        }
    }
}