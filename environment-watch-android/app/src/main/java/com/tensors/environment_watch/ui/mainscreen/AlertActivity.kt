package com.tensors.environment_watch.ui.mainscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.AlertAdapter
import kotlinx.android.synthetic.main.activity_alert.*

class AlertActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        alerts_list.adapter = AlertAdapter(this)
    }
}