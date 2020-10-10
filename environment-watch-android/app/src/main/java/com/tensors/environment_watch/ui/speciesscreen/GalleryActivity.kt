package com.tensors.environment_watch.ui.speciesscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.GalleryAdapter
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        gallery_of_photos.adapter = GalleryAdapter(this)
    }
}