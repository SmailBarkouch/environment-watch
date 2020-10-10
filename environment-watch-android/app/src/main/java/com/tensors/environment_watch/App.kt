package com.tensors.environment_watch

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.core.app.NotificationCompat
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.api.species
import java.util.*


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers: List<String> = locationManager.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val location = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                    bestLocation = location
                }
            }

            val myLat = bestLocation?.latitude
            val myLon = bestLocation?.longitude

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    species.forEach { specificSpecies ->
                        FirebaseStorage.getInstance().reference.child("coords/${specificSpecies.httpRequestName}")
                            .listAll().addOnSuccessListener { listResult ->
                                var count = 0
                                var latLast = -1f
                                var lonLast = -1f
                                val currentTime = Date().time
                                listResult?.items?.forEach { storageReference ->
                                    storageReference.getBytes(1024 * 1024)
                                        .addOnSuccessListener { byteArray ->
                                            val (lat, lon, time) = String(byteArray).split(" ")
                                            if (latLast == -1f) {
                                                latLast = lat.toFloat()
                                                lonLast = lon.toFloat()
                                            } else {
                                                if ((currentTime - 180000..currentTime + 180000).contains(
                                                        time.toLong()
                                                    ) && (latLast - 1..latLast + 1).contains(lat.toFloat()) && (lonLast - 1..lonLast + 1).contains(
                                                        lon.toFloat()
                                                    )
                                                ) {
                                                    count++
                                                } else {
                                                    count = 0
                                                }
                                                if (count > 5 && (latLast - 1..latLast + 1).contains(
                                                        myLat!!.toFloat()
                                                    ) && (lonLast - 1..lonLast + 1).contains(myLon!!.toFloat())
                                                ) {
                                                    val notificationBuilder =
                                                        NotificationCompat.Builder(
                                                            applicationContext
                                                        )
                                                            .setSmallIcon(R.drawable.icon)
                                                            .setContentTitle("Spotting alert")
                                                            .setContentText("Many endangered birds have been spotted near you!")
                                                    val notificationManager =
                                                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                                    notificationManager.notify(
                                                        1,
                                                        notificationBuilder.build()
                                                    )
                                                    count = 0
                                                }
                                            }
                                        }
                                }
                            }
                    }
                }

            }, 120000, 120000)
        }


    }

    companion object {
    }
}