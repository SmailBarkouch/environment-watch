package com.tensors.environment_watch.ui.speciesscreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.GalleryAdapter
import com.tensors.environment_watch.api.Species
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.activity_species.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class SpeciesActivity : AppCompatActivity() {
    lateinit var species: Species
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {googleMap ->
            FirebaseStorage.getInstance().reference.child("coords/${species.httpRequestName}")
                .listAll().addOnSuccessListener { listResult ->
                    Log.e("Smail", "Why: ${listResult.items.size}")
                    listResult?.items?.forEach { storageReference ->
                        storageReference.getBytes(1024 * 1024).addOnSuccessListener {
                            val (lat, lon) = String(it).split(" ")
                            googleMap?.addMarker(MarkerOptions().position(LatLng(lat.toDouble(), lon.toDouble())))
                        }
                    }
                }
        }

        setUpView()
    }

    private fun setUpView() {
        species = Species(
            intent.getStringExtra("name")!!,
            intent.getStringExtra("fullName")!!,
            intent.getStringExtra("httpRequestName")!!,
            intent.getStringExtra("state")!!,
            intent.getStringExtra("fullDescription")!!
        )

        specific_species_name.text = species.name
        specific_species_full_name.text = species.fullName
        specific_species_state.text = species.state
        specific_species_description.text = species.description

        when (species.name) {
            "Red-cockaded Woodpecker" -> specific_species_image.setImageResource(R.drawable.specific1)
            "Yellow-billed Cuckoo" -> specific_species_image.setImageResource(R.drawable.specific2)
            "Florida Jay" -> specific_species_image.setImageResource(R.drawable.specific3)
            "Rusty Blackbird" -> specific_species_image.setImageResource(R.drawable.specific4)
            "Red-legged Kittiwake" -> specific_species_image.setImageResource(R.drawable.specific5)
        }

        species_fab.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 0
                )
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 0)
            }
        }

        specific_species_gallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)


            intent.putExtra("requestName", species.httpRequestName)
            startActivity(intent)
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
            && grantResults[2] == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_LONG).show()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 0)
        } else {
            Toast.makeText(this, "Please grant all the permissions.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val image = data!!.extras!!.get("data") as Bitmap

        if (interpretImageMatches(image)) {

            uploadImage(image)
        } else {
            // Some other stuff
        }

    }

    private fun interpretImageMatches(image: Bitmap): Boolean {
        // Some code here using the python classifier

        return true
    }

    private fun uploadImage(image: Bitmap) {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            finish()
            Toast.makeText(
                this,
                "Please accept location permissions in settings.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val lat = location?.latitude
        val lon = location?.longitude
        // Under here you put your code where you upload the images to firebase with the coordinates

        val tempUri = getImageUri(applicationContext, image)

        FirebaseStorage.getInstance().reference.child("images/${species.httpRequestName}/${UUID.randomUUID()}")
            .putFile(tempUri!!)
            .addOnSuccessListener { p0 ->
                Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { p0 ->
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_LONG).show()
            }

        val tempFile = File.createTempFile(UUID.randomUUID().toString(), null)
        tempFile.writeText("$lat $lon")
        FirebaseStorage.getInstance().reference.child("coords/${species.httpRequestName}/${UUID.randomUUID()}.txt")
            .putFile(tempFile.toUri())
            .addOnSuccessListener { p0 ->
                Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { p0 ->
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_LONG).show()
            }

        // after uploading I will go to another activity
        startActivity(Intent(this, ResultsActivity::class.java))

    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
        return Uri.parse(
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                UUID.randomUUID().toString(),
                null
            )
        )
    }

}
