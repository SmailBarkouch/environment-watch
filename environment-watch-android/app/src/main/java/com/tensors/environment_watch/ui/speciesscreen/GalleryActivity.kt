package com.tensors.environment_watch.ui.speciesscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.GalleryAdapter
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {
    val images = mutableListOf<Bitmap>()
    val imageDatas = mutableListOf<ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        Log.e("Smail", intent.getStringExtra("requestName")!!)
        val database = Firebase.database.reference

        FirebaseStorage.getInstance().reference.child("images/${intent.getStringExtra("requestName")}")
            .listAll().addOnSuccessListener { listResult ->
                listResult?.items?.forEach { storageReference ->
                    storageReference.getBytes(1024 * 1024)
                        .addOnSuccessListener { byteArray ->
                            database.child("imageData").child(storageReference.name).addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(snapshot.value != null) {
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
                                            GalleryAdapter(applicationContext, images, imageDatas)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}

                            })
                        }
                }
            }
    }


    override fun onStart() {
        super.onStart()
    }


}