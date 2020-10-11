package com.tensors.environment_watch.api

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tensors.environment_watch.App
import com.tensors.environment_watch.R
import com.tensors.environment_watch.ui.speciesscreen.ImageData

class GalleryAdapter(context: Context, private val photos: List<Bitmap> = listOf(), private val imageDatas: List<ImageData>): BaseAdapter() {

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
        photoView?.findViewById<TextView>(R.id.like_amount)?.text = imageDatas[position].likes.toString()
        photoView?.findViewById<TextView>(R.id.dislike_amount)?.text = imageDatas[position].dislikes.toString()
        photoView?.findViewById<ImageButton>(R.id.like_button)?.setOnClickListener {
            Log.e("Smail", "Like button clicked!")
            if(!PreferenceManager.getDefaultSharedPreferences(App.instance).getBoolean("${imageDatas[position].name}_liked", false)) {
                val database = Firebase.database.reference
                imageDatas[position].likes += 1
                database.child("imageData").child(imageDatas[position].name).child("likes").setValue(imageDatas[position].likes)
                photoView.findViewById<TextView>(R.id.like_amount)?.text = (imageDatas[position].likes).toString()
                val pref = PreferenceManager.getDefaultSharedPreferences(App.instance).edit()
                pref.putBoolean("${imageDatas[position].name}_liked", true)
                pref.apply()
            } else {
                val database = Firebase.database.reference
                imageDatas[position].likes -= 1
                database.child("imageData").child(imageDatas[position].name).child("likes").setValue(imageDatas[position].likes)
                photoView.findViewById<TextView>(R.id.like_amount)?.text = (imageDatas[position].likes).toString()
                val pref = PreferenceManager.getDefaultSharedPreferences(App.instance).edit()
                pref.putBoolean("${imageDatas[position].name}_liked", false)
                pref.apply()
            }
        }

        photoView?.findViewById<ImageButton>(R.id.dislike_button)?.setOnClickListener {
            Log.e("Smail", "Dislike button clicked!")
            if(!PreferenceManager.getDefaultSharedPreferences(App.instance).getBoolean("${imageDatas[position].name}_disliked", false)) {
                val database = Firebase.database.reference
                imageDatas[position].dislikes += 1
                database.child("imageData").child(imageDatas[position].name).child("dislikes").setValue(imageDatas[position].dislikes)
                photoView.findViewById<TextView>(R.id.dislike_amount)?.text = (imageDatas[position].dislikes).toString()
                val pref = PreferenceManager.getDefaultSharedPreferences(App.instance).edit()
                pref.putBoolean("${imageDatas[position].name}_disliked", true)
                pref.apply()
            } else {
                val database = Firebase.database.reference
                imageDatas[position].dislikes -= 1
                database.child("imageData").child(imageDatas[position].name).child("dislikes").setValue(imageDatas[position].dislikes)
                photoView.findViewById<TextView>(R.id.dislike_amount)?.text = (imageDatas[position].dislikes).toString()
                val pref = PreferenceManager.getDefaultSharedPreferences(App.instance).edit()
                pref.putBoolean("${imageDatas[position].name}_disliked", false)
                pref.apply()
            }
        }

        return photoView!! // it's never null but kotlin doesn't realize that
    }

}