package com.example.mood_tracker

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mood_tracker.databinding.ActivityMainBinding
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.zip.DataFormatException
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )

        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val editTextNote: EditText = findViewById(R.id.editTextNote);
        val imageViewAngry: ImageView = findViewById(R.id.angry)
        val imageViewSad: ImageView = findViewById(R.id.sad)
        val imageViewNeutral: ImageView = findViewById(R.id.neutral)
        val imageViewSmile: ImageView = findViewById(R.id.smile)
        val imageViewHappy: ImageView = findViewById(R.id.happy)
        val buttonSubmit: Button = findViewById(R.id.button_submit)

        var text: String = "aaa"

        imageViewAngry.setOnClickListener {
            imageViewAngry.alpha = 1.0F
            imageViewSad.alpha = 0.5F
            imageViewNeutral.alpha = 0.5F
            imageViewSmile.alpha = 0.5F
            imageViewHappy.alpha = 0.5F
            text = "angry"
        }

        imageViewSad.setOnClickListener {
            imageViewAngry.alpha = 0.5F
            imageViewSad.alpha = 1.0F
            imageViewNeutral.alpha = 0.5F
            imageViewSmile.alpha = 0.5F
            imageViewHappy.alpha = 0.5F
            text = "sad"
        }

        imageViewNeutral.setOnClickListener {
            imageViewAngry.alpha = 0.5F
            imageViewSad.alpha = 0.5F
            imageViewNeutral.alpha = 1F
            imageViewSmile.alpha = 0.5F
            imageViewHappy.alpha = 0.5F
            text = "neutral"
        }

        imageViewSmile.setOnClickListener {
            imageViewAngry.alpha = 0.5F
            imageViewSad.alpha = 0.5F
            imageViewNeutral.alpha = 0.5F
            imageViewSmile.alpha = 1.0F
            imageViewHappy.alpha = 0.5F
            text = "smile"
        }

        imageViewHappy.setOnClickListener {
            imageViewAngry.alpha = 0.5F
            imageViewSad.alpha = 0.5F
            imageViewNeutral.alpha = 0.5F
            imageViewSmile.alpha = 0.5F
            imageViewHappy.alpha = 1.0F
            text = "happy"
        }

        buttonSubmit.setOnClickListener {
            // Co się stanie po kliknięciu przycisku
//            if(!imageViewAngry.isSelected) {
//                text = "angry"
//            }
            //text = editTextNote.text.toString()
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }
}