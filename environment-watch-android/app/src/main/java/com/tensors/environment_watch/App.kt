package com.tensors.environment_watch

import android.app.Application
import android.graphics.PointF
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.api.species
import java.util.*

class App: Application() {
    override fun onCreate() {
        super.onCreate()
//        Timer().schedule(object: TimerTask() {
//            override fun run() {
//                species.forEach { specificSpecies ->
//                    FirebaseStorage.getInstance().reference.child("coords/${specificSpecies.httpRequestName}")
//                        .listAll().addOnSuccessListener { listResult ->
//                            listResult?.items?.forEach { storageReference ->
//                                storageReference.getBytes(1024 * 1024).addOnSuccessListener { byteArray ->
//                                    val (lat, lon) = String(byteArray).split(" ")
//
//                                }
//                            }
//                        }
//                }
//            }
//
//        }, 1, 20000)
    }

    companion object {
        var alerts = mutableListOf<String>()
    }
    }