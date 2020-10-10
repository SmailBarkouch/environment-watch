package com.tensors.environment_watch.api

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tensors.environment_watch.R

class GalleryAdapter(context: Context, private val photos: List<Bitmap> = listOf()): BaseAdapter() {

    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = photos.size

    override fun getItem(position: Int): Any = photos[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var photoView = convertView

        if(photoView == null) {
            photoView = inflater.inflate(R.layout.photo_item_layout, null)
        }

        photoView?.findViewById<ImageView>(R.id.photo)?.setImageBitmap(photos[position])

        return photoView!! // it's never null but kotlin doesn't realize that
    }

}