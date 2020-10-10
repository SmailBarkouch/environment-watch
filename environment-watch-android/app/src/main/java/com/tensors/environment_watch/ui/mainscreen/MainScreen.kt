package com.tensors.environment_watch.ui.mainscreen

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.SectionsPagerAdapter
import java.util.*

class MainScreen : AppCompatActivity() {

    var image: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        if ( checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
        }


        fab.setOnClickListener { view ->
            val alertDialog = AlertDialog.Builder(this).setView(layoutInflater.inflate(R.layout.activity_submit_request, null))
                .setPositiveButton("Submit"
                ) { dialog, id ->
                    val animalName = (dialog as Dialog).findViewById<EditText>(R.id.submit_species_name).text.toString()
                    Log.e("Smail", "31231: ${image != null} && ${animalName != ""}")

                    if(image != null && animalName != "") {
                        val randomName = UUID.randomUUID().toString()
                        FirebaseStorage.getInstance().reference.child("submissions/${animalName}/${randomName}")
                            .putFile(image!!)
                            .addOnSuccessListener { p0 ->
                                Toast.makeText(applicationContext, "Submitted", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { p0 ->
                                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Please don't leave fields empty.", Toast.LENGTH_SHORT).show()
                    }

                }.create()

            alertDialog.show()
            alertDialog.findViewById<ImageButton>(R.id.submit_species_image)?.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*";
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED
            && grantResults[1] != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "We are unable to do alerts if you do not provide your location.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0) {
            image = data?.data
        }
    }
}