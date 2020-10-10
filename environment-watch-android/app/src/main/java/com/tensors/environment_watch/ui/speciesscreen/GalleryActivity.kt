package com.tensors.environment_watch.ui.speciesscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.GalleryAdapter
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {
    val images = mutableListOf<Bitmap>()
    val done = MutableLiveData<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        done.observe(
            this,
            {
                gallery_of_photos.adapter = GalleryAdapter(applicationContext, images)
            }
        )

        FirebaseStorage.getInstance().reference.child("images/${intent.getStringExtra("requestName")}")
            .listAll().addOnSuccessListener { listResult ->
                listResult?.items?.forEach { storageReference ->
                    storageReference.getBytes(1024 * 1024)
                        .addOnSuccessListener { byteArray ->
                            images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))
                            gallery_of_photos.adapter = GalleryAdapter(applicationContext, images)
                        }
                }
            }
    }


    override fun onStart() {
        super.onStart()
    }


}