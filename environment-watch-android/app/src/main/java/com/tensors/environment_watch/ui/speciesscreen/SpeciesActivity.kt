package com.tensors.environment_watch.ui.speciesscreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.apptakk.http_request.HttpRequest
import com.apptakk.http_request.HttpRequestTask
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.Species
import kotlinx.android.synthetic.main.activity_species.*
import java.io.ByteArrayOutputStream


class SpeciesActivity : AppCompatActivity() {
    lateinit var species: Species
    lateinit var filePath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species)


        setUpView()
    }

    private fun setUpView() {
        species = Species(
            intent.getStringExtra("name")!!,
            intent.getStringExtra("fullName")!!,
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
            startActivity(Intent(this, GalleryActivity::class.java))
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
            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 0)
        } else {
            Toast.makeText(this, "Please grant all the permissions.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getImageUri(photo: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            photo,
            "pic",
            null
        )
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val image = data!!.extras!!.get("data") as Bitmap
        filePath = getImageUri(image)!!

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

        HttpRequestTask(
            HttpRequest(
                "10.0.0.207",
                HttpRequest.POST,
                getStringFromBitmap(image)
            )
        ) { response ->
            Toast.makeText(applicationContext, "Uploaded successfully: ${response.code}", Toast.LENGTH_LONG).show()
        }.execute()

        // after uploading I will go to another activity
        startActivity(Intent(this, ResultsActivity::class.java))

    }

    private fun getStringFromBitmap(bitmapPicture: Bitmap): String? {
        val COMPRESSION_QUALITY = 100
        val encodedImage: String
        val byteArrayBitmapStream = ByteArrayOutputStream()

        bitmapPicture.compress(
            Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
            byteArrayBitmapStream
        )

        val byteArray = byteArrayBitmapStream.toByteArray()
        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)

        return encodedImage
    }

    private fun getBitmapFromString(stringPicture: String): Bitmap? {
        val decodedString: ByteArray = Base64.decode(stringPicture, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

}