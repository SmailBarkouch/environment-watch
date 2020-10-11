package com.tensors.environment_watch

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.api.species
import java.util.*


class App: Application() {
    override fun onCreate() {
        super.onCreate()


    }
}