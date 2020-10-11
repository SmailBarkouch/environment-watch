package com.tensors.environment_watch.ui.speciesscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.GalleryAdapter
import kotlinx.android.synthetic.main.activity_gallery.*
import java.util.*

class GalleryActivity : AppCompatActivity() {
    val images = mutableListOf<Bitmap>()
    val imageDatas = mutableListOf<ImageData>()
    var dayView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        everImages()

        day_images.setOnClickListener {
            dayView = if(dayView) {
                Snackbar.make(gallery_layout, "Switching view to to show all images ever.", Snackbar.LENGTH_SHORT).show()
                images.clear()
                imageDatas.clear()
                everImages()
                false
            } else {
                images.clear()
                imageDatas.clear()
                Snackbar.make(gallery_layout, "Switching view to just show the last 24 hours of images.", Snackbar.LENGTH_SHORT).show()
                dayImages()
                true
            }
        }
    }

    fun everImages() {
        val database = Firebase.database.reference

        FirebaseStorage.getInstance().reference.child("images/${intent.getStringExtra("requestName")}")
            .listAll().addOnSuccessListener { listResult ->
                listResult?.items?.forEach { storageReference ->
                    storageReference.getBytes(1024 * 1024)
                        .addOnSuccessListener { byteArray ->
                            database.child("imageData").child(storageReference.name)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.value != null) {
                                            val bitmap = BitmapFactory.decodeByteArray(
                                                byteArray,
                                                0,
                                                byteArray.size
                                            )
                                            images.removeIf {
                                                it == bitmap
                                            }
                                            imageDatas.removeIf {
                                                it.name == storageReference.name
                                            }

                                            imageDatas.add(
                                                ImageData(
                                                    storageReference.name,
                                                    snapshot.child("likes").value as Long,
                                                    snapshot.child("dislikes").value as Long,
                                                )
                                            )
                                            images.add(
                                                bitmap
                                            )
                                            gallery_of_photos.adapter =
                                                GalleryAdapter(
                                                    applicationContext,
                                                    images,
                                                    imageDatas
                                                )
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}

                                })
                        }
                }
            }
    }

    fun dayImages() {
        val database = Firebase.database.reference
        val firebaseStorage = FirebaseStorage.getInstance().reference

        firebaseStorage.child("images/${intent.getStringExtra("requestName")}")
            .listAll().addOnSuccessListener { listResult ->
                listResult?.items?.forEach { storageReference ->
                    storageReference.getBytes(1024 * 1024)
                        .addOnSuccessListener { byteArray ->
                            database.child("imageData").child(storageReference.name)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.value != null) {
                                            firebaseStorage.child("coords/${intent.getStringExtra("requestName")}/${storageReference.name}")
                                            .getBytes(1024 * 1024)
                                            .addOnSuccessListener { byteArray ->
                                                val (lat, lon, time) = String(byteArray).split(" ")
                                                val currentTime = Date().time
                                                Log.e("SMAI", "ImageTime: $time, CurrentTime: $currentTime")
                                                if((currentTime-86400000..currentTime+86400000).contains(time.toLong())) {
                                                    val bitmap = BitmapFactory.decodeByteArray(
                                                        byteArray,
                                                        0,
                                                        byteArray.size
                                                    )
                                                    images.removeIf {
                                                        it == bitmap
                                                    }
                                                    imageDatas.removeIf {
                                                        it.name == storageReference.name
                                                    }

                                                    imageDatas.add(
                                                        ImageData(
                                                            storageReference.name,
                                                            snapshot.child("likes").value as Long,
                                                            snapshot.child("dislikes").value as Long,
                                                        )
                                                    )
                                                    images.add(
                                                        bitmap
                                                    )
                                                }
                                                gallery_of_photos.adapter =
                                                    GalleryAdapter(
                                                        applicationContext,
                                                        images,
                                                        imageDatas
                                                    )
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}

                                })
                        }
                }
            }
    }
}