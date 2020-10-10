package com.tensors.environment_watch.ui.mainscreen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
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

        fab.setOnClickListener { view ->
            val alertDialog = AlertDialog.Builder(this).setView(layoutInflater.inflate(R.layout.activity_submit_request, null))
                .setPositiveButton("Submit"
                ) { dialog, id ->
                    val animalName = (dialog as Dialog).findViewById<EditText>(R.id.submit_species_name).text.toString()
                    if(image != null && animalName != "") {
                        val randomName = UUID.randomUUID().toString()
                        FirebaseStorage.getInstance().reference.child("submissions/${animalName}/${randomName}")
                            .putFile(image!!)
                            .addOnSuccessListener { p0 ->
                                Toast.makeText(applicationContext, "Submitted", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener { p0 ->
                                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(this, "Please don't leave fields empty.", Toast.LENGTH_LONG).show()
                    }

                }.create()

            alertDialog.show()
            alertDialog.findViewById<ImageButton>(R.id.submit_species_image)?.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*";
                startActivityForResult(intent, 0)
            }
        }

        alerts.setOnClickListener {
            startActivity(Intent(this, AlertActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0) {
            image = data?.data
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
        return Uri.parse(
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                UUID.randomUUID().toString(),
                null
            )
        )
    }
}