package com.tensors.environment_watch.api

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tensors.environment_watch.R

class AlertAdapter(context: Context, private val description: List<String> = listOf()): BaseAdapter() {

    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = description.size

    override fun getItem(position: Int): Any = description[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var alertView = convertView

        if(alertView == null) {
            alertView = inflater.inflate(R.layout.species_card_layout, null)
        }

        alertView?.findViewById<TextView>(R.id.photo)?.text = description[position]

        return alertView!! // it's never null but kotlin doesn't realize that
    }

}