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
        // data
        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale("pl")).format(calendar)
        val dateTextView = findViewById<TextView>(R.id.xml_text_date)
        dateTextView.text = dateFormat


        // buzki
        val imageIds = arrayOf(R.id.angry, R.id.sad, R.id.neutral, R.id.smile, R.id.happy)

        for (imageId in imageIds) {
            val imageView = findViewById<ImageView>(imageId)
            imageView.setOnClickListener {
                // Resetowanie kolorów wszystkich obrazków
                for (id in imageIds) {
                    findViewById<ImageView>(id).setBackgroundColor(Color.TRANSPARENT)
                }
                // Ustawienie nowego koloru dla klikniętego obrazka
                imageView.setBackgroundColor(Color.BLUE)
            }
        }







    }

}