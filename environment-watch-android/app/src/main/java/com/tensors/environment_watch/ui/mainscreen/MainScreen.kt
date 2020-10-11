package com.tensors.environment_watch.ui.mainscreen

import android.Manifest
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.SectionsAdapter
import com.tensors.environment_watch.api.species
import com.tensors.environment_watch.ui.SplashScreen
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainScreen : AppCompatActivity() {

    var image: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsAdapter = SectionsAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsAdapter
        tabs.setupWithViewPager(view_pager)

        if ( checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
        } else {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected && checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
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
                        alert(myLat, myLon)
                    }

                }, 1000, 120000)
            }

        }


        email_animals.setOnClickListener { view ->
            val alertDialog = AlertDialog.Builder(this).setView(
                layoutInflater.inflate(
                    R.layout.activity_submit_request,
                    null
                )
            )
                .setPositiveButton(
                    "Submit"
                ) { dialog, id ->
                    val animalName = (dialog as Dialog).findViewById<EditText>(R.id.submit_species_name).text.toString()
                    Log.e("Smail", "31231: ${image != null} && ${animalName != ""}")

                    if(image != null && animalName != "") {
                        val randomName = UUID.randomUUID().toString()
                        FirebaseStorage.getInstance().reference.child("submissions/${animalName}/${randomName}")
                            .putFile(image!!)
                            .addOnSuccessListener { p0 ->
                                Toast.makeText(applicationContext, "Submitted", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { p0 ->
                                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Please don't leave fields empty.", Toast.LENGTH_SHORT).show()
                    }

                }.create()

            alertDialog.show()
            alertDialog.findViewById<ImageButton>(R.id.submit_species_image)?.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*";
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED
        ) {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected && checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
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
                        alert(myLat, myLon)
                    }

                }, 1000, 120000)
            }
        }
    }

    private fun alert(myLat: Double?, myLon: Double?) {
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
                                    if ((currentTime - 120000..currentTime + 120000).contains(
                                            time.toLong()
                                        ) && (latLast - 1..latLast + 1).contains(lat.toFloat()) && (lonLast - 1..lonLast + 1).contains(
                                            lon.toFloat()
                                        )
                                    ) {
                                        count++
                                    }

                                    if (count > 5 && (latLast - 1..latLast + 1).contains(
                                            myLat!!.toFloat()
                                        ) && (lonLast - 1..lonLast + 1).contains(
                                            myLon!!.toFloat()
                                        )
                                    ) {
                                        val intent = Intent(
                                            applicationContext,
                                            SplashScreen::class.java
                                        ).apply {
                                            flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        }
                                        val pendingIntent = PendingIntent.getActivity(
                                            applicationContext,
                                            0,
                                            intent,
                                            0
                                        )

                                        var builder = NotificationCompat.Builder(
                                            applicationContext,
                                            "animal_watch_alert"
                                        )
                                            .setSmallIcon(R.drawable.icon)
                                            .setContentTitle("Spotting alert")
                                            .setContentText("Many endangered animals have been spotted near you!")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setContentIntent(pendingIntent)
                                            .setAutoCancel(true)

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            val channel = NotificationChannel(
                                                "animal_watch_alert",
                                                "Environment Watch Alert",
                                                NotificationManager.IMPORTANCE_DEFAULT
                                            ).apply {
                                                description =
                                                    "Many endangered birds have been spotted near you!"
                                            }
                                            val notificationManager: NotificationManager =
                                                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                                            notificationManager.createNotificationChannel(channel)
                                        }

                                        with(NotificationManagerCompat.from(applicationContext)) {
                                            notify(0, builder.build())
                                        }
                                        count = 0
                                    }
                                }
                            }
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0) {
            image = data?.data
        }
    }
}