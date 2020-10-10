package com.tensors.environment_watch.ui.mainscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.tensors.environment_watch.R
import kotlinx.android.synthetic.main.activity_main.*

class MainScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater;

            builder.setView(inflater.inflate(R.layout.activity_submit_request, null))
                .setPositiveButton("Submit",
                    { dialog, id ->

                    })
                .setNegativeButton("Cancel",
                    { dialog, id ->

                    })
            builder.create()

            builder.show()
        }

        alerts.setOnClickListener {
            startActivity(Intent(this, AlertActivity::class.java))
        }
    }
}