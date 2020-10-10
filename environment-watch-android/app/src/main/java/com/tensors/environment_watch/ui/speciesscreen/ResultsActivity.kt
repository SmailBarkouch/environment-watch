package com.tensors.environment_watch.ui.speciesscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tensors.environment_watch.R
import com.tensors.environment_watch.ui.mainscreen.MainScreen
import kotlinx.android.synthetic.main.classifier_results_layout.*
import kotlin.system.exitProcess

class ResultsActivity : AppCompatActivity() {
    val errorTitle = "Uh-oh..."
    val errorDescription = "It seems we weren't able to verify that your\n" +
            "image was what you said it was. This is either due to your\n" +
            "image not being clear enough or the image not having any relevance\n" +
            "to the animal you choice."

    val successTitle = "Congratulations"
    val successDescription = "You're photo was clear enough to be verified by our\n" +
            "classifier. This has been uploaded to the database and now will be visible to\n" +
            "other users on this platform. Thank you!"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.classifier_results_layout)

        setTextUpBasedOnResults()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        back_to_homescreen.setOnClickListener {
            startActivity(Intent(this, MainScreen::class.java))
            finishAffinity()
        }

        quit_app.setOnClickListener {
            finishAffinity()
            exitProcess(0)
        }
    }

    private fun setTextUpBasedOnResults() {
//        TODO("Not yet implemented")
    }
}