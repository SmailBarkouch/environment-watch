package com.tensors.environment_watch.ui.welcomescreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tensors.environment_watch.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        get_started.setOnClickListener {
            startActivity(Intent(this, SecondWelcomeActivity::class.java))
        }
    }
}