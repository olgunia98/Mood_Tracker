package com.example.mood_tracker

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mood_tracker.databinding.ActivityMainBinding
import java.util.Calendar
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private val timePickers = mutableListOf<TimePicker>()



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

//        val editTextNote: EditText = findViewById(R.id.editTextNote)
//        val imageViewAngry: ImageView = findViewById(R.id.angry)
//        val imageViewSad: ImageView = findViewById(R.id.sad)
//        val imageViewNeutral: ImageView = findViewById(R.id.neutral)
//        val imageViewSmile: ImageView = findViewById(R.id.smile)
//        val imageViewHappy: ImageView = findViewById(R.id.happy)
//        val buttonSubmit: Button = findViewById(R.id.button_submit)
//
//        var text: String = "Nie wybrano emocji"
//
//        imageViewAngry.setOnClickListener {
//            imageViewAngry.alpha = 1.0F
//            imageViewSad.alpha = 0.5F
//            imageViewNeutral.alpha = 0.5F
//            imageViewSmile.alpha = 0.5F
//            imageViewHappy.alpha = 0.5F
//            text = "angry"
//        }
//
//        imageViewSad.setOnClickListener {
//            imageViewAngry.alpha = 0.5F
//            imageViewSad.alpha = 1.0F
//            imageViewNeutral.alpha = 0.5F
//            imageViewSmile.alpha = 0.5F
//            imageViewHappy.alpha = 0.5F
//            text = "sad"
//        }
//
//        imageViewNeutral.setOnClickListener {
//            imageViewAngry.alpha = 0.5F
//            imageViewSad.alpha = 0.5F
//            imageViewNeutral.alpha = 1F
//            imageViewSmile.alpha = 0.5F
//            imageViewHappy.alpha = 0.5F
//            text = "neutral"
//        }
//
//        imageViewSmile.setOnClickListener {
//            imageViewAngry.alpha = 0.5F
//            imageViewSad.alpha = 0.5F
//            imageViewNeutral.alpha = 0.5F
//            imageViewSmile.alpha = 1.0F
//            imageViewHappy.alpha = 0.5F
//            text = "smile"
//        }
//
//        imageViewHappy.setOnClickListener {
//            imageViewAngry.alpha = 0.5F
//            imageViewSad.alpha = 0.5F
//            imageViewNeutral.alpha = 0.5F
//            imageViewSmile.alpha = 0.5F
//            imageViewHappy.alpha = 1.0F
//            text = "happy"
//        }
//
//        buttonSubmit.setOnClickListener {
//            // Co się stanie po kliknięciu przycisku
////            if(!imageViewAngry.isSelected) {
////                text = "angry"
////            }
//            //text = editTextNote.text.toString()
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
//        }

        //scheduleNotification(this)
    }

    companion object {
        private const val REQUEST_CODE_BASE = 100
        @SuppressLint("ScheduleExactAlarm")
        fun scheduleNotification(context: Context) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 13)
                set(Calendar.MINUTE, 22)
                set(Calendar.SECOND, 30)
            }

            // val intent = Intent(context, NotificationReceiver::class.java)
//            val editTextNote: EditText = findViewById(R.id.editTextNote)
//            val notificationText = (context as MainActivity).notificationText.text.toString()
            val notificationText = "Aspiryna"
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("notification_text", notificationText)
            }
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }

//        fun cancelNotifications(context: Context) {
//            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            for (index in 0 until (context as MainActivity).timePickers.size) {
//                val intent = Intent(context, NotificationReceiver::class.java)
//                val pendingIntent = PendingIntent.getBroadcast(
//                    context, REQUEST_CODE_BASE + index, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                )
//
//                alarmManager.cancel(pendingIntent)
//            }
//        }
    }

}