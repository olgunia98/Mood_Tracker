package com.example.mood_tracker

import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.mood_tracker.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Medicine : AppCompatActivity() {

    private var notificationId = 0

    private var startDate = ""
    private var endDate = ""
    private val timesList = mutableListOf<String>()

    private val selectedStartDatesList = mutableListOf<String>()
    private val selectedEndDatesList = mutableListOf<String>()
    private val selectedTimesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_medicine)

        val imageButton2: ImageButton = findViewById(R.id.imageButton2)
        imageButton2.setOnClickListener {
            onBackPressed()
        }

        val addTimeButton: Button = findViewById(R.id.addTimeButton)
        addTimeButton.setOnClickListener {
            showTimePickerDialog()
        }

        val addStartDateButton: Button = findViewById(R.id.addStartDateButton)
        addStartDateButton.setOnClickListener {
            showDatePickerDialog(selectedStartDatesList, findViewById(R.id.selectedStartDatesLayout), "start")
        }

        val addEndDateButton: Button = findViewById(R.id.addEndDateButton)
        addEndDateButton.setOnClickListener {
            showDatePickerDialog(selectedEndDatesList, findViewById(R.id.selectedEndDatesLayout), "end")
        }

        val saveButton: Button = findViewById(R.id.save)
        saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay.toInt(), minute.toInt())
                selectedTimesList.add(selectedTime)
                timesList.add(selectedTime)
                updateSelectedTimesLayout()
                Toast.makeText(this, "Selected time: $selectedTime", Toast.LENGTH_SHORT).show()
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog(dateList: MutableList<String>, layout: LinearLayout, type: String) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                dateList.add(selectedDate)
                if (type == "start"){
                    startDate = selectedDate
                }
                else if (type == "end") {
                    endDate = selectedDate
                }
                updateSelectedDatesLayout(dateList, layout, type)
                Toast.makeText(this, "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun updateSelectedDatesLayout(dateList: MutableList<String>, layout: LinearLayout, type: String) {
        layout.removeAllViews()

        for (selectedDate in dateList) {
            val itemView = layoutInflater.inflate(R.layout.item_selected_date, layout, false)
            val dateTextView: TextView = itemView.findViewById(R.id.timeTextView)
            val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

            dateTextView.text = selectedDate
            // selectedStartDate = selectedDate
            deleteButton.setOnClickListener {
                if (type == "start"){
                    startDate = ""
                }
                else if (type == "end") {
                    endDate = ""
                }
                dateList.remove(selectedDate)
                layout.removeView(itemView)
            }

            layout.addView(itemView)
        }
    }

    private fun updateSelectedTimesLayout() {
        val layoutInflater = LayoutInflater.from(this)
        val selectedTimesLayout: LinearLayout = findViewById(R.id.selectedTimesLayout)
        selectedTimesLayout.removeAllViews()

        for (selectedTime in selectedTimesList) {
            val itemView = layoutInflater.inflate(R.layout.item_selected_time, selectedTimesLayout, false)
            val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
            val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

            timeTextView.text = selectedTime
            deleteButton.setOnClickListener {
                selectedTimesList.remove(selectedTime)
                timesList.remove(selectedTime)
                updateSelectedTimesLayout()
            }

            selectedTimesLayout.addView(itemView)
        }
    }

    private fun saveData() {
        val drugName: EditText = findViewById(R.id.editTextDrugName)
        val dosage: EditText = findViewById(R.id.editTextNumber2)

        if (drugName.text.isNullOrEmpty()) {
            Toast.makeText(this, "Nie wybrano nazwy lekarstwa", Toast.LENGTH_SHORT).show()
        } else if (dosage.text.isNullOrEmpty()) {
            Toast.makeText(this, "Nie wybrano dawkowania", Toast.LENGTH_SHORT).show()
        } else if (startDate.isEmpty()) {
            Toast.makeText(this, "Nie wybrano daty początkowej", Toast.LENGTH_SHORT).show()
        } else if (endDate.isEmpty()) {
            Toast.makeText(this, "Nie wybrano daty końcowej", Toast.LENGTH_SHORT).show()
        } else if (timesList.isEmpty()) {
            Toast.makeText(this, "Nie wybrano godziny przyjmowania", Toast.LENGTH_SHORT).show()
        } else {
            val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
            val start = LocalDate.parse(startDate, dateFormatter)
            val end = LocalDate.parse(endDate, dateFormatter)

            if (end.isBefore(start)) {
                Toast.makeText(this, "Data końcowa musi być późniejsza niż data początkowa", Toast.LENGTH_SHORT).show()
            } else {
                setNotifications()
                saveToJsonFile(
                    "data.json",
                    drugName.text.toString(),
                    dosage.text.toString(),
                    startDate,
                    endDate,
                    timesList
                )

                val sharedPreferences = getSharedPreferences("medicines", MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                val medicineId = UUID.randomUUID().toString()
                editor.putString("$medicineId-drugName", drugName.text.toString())
                editor.putString("$medicineId-dosage", dosage.text.toString())
                editor.putStringSet("$medicineId-startDates", selectedStartDatesList.toSet())
                editor.putStringSet("$medicineId-endDates", selectedEndDatesList.toSet())
                editor.putStringSet("$medicineId-times", selectedTimesList.toSet())
                editor.apply()

                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun saveToJsonFile(
        filePath: String,
        drugName: String,
        dosage: String,
        startDate: String,
        endDate: String,
        timesList: List<String>
    ) {
        val file = File(filesDir, filePath)

        // Sprawdź, czy plik istnieje
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]") // Tworzymy pustą listę JSON, jeśli plik nie istnieje
        }

        // Wczytaj zawartość pliku JSON
        val json = file.readText()

        // Parsuj JSON jako JSONArray
        val jsonArray = JSONArray(json)

        // Dodaj nowy element jako JSONObject
        val newElement = JSONObject()
        newElement.put("drugName", drugName)
        newElement.put("dosage", dosage)
        newElement.put("startDate", startDate)
        newElement.put("endDate", endDate)
        newElement.put("timesList", JSONArray(timesList))

        jsonArray.put(newElement)

        // Konwertuj zaktualizowaną JSONArray z powrotem do JSON
        val updatedJson = jsonArray.toString()

        // Zapisz zaktualizowany JSON do pliku
        file.writeText(updatedJson)
    }

    private fun setNotifications() {
        cancelAllNotifications()

        val drugName: EditText = findViewById(R.id.editTextDrugName)
        val dosage: EditText = findViewById(R.id.editTextNumber2)

        val drugNameText = drugName.text.toString()
        val dosageText = dosage.text.toString()

        val dates = getDatesBetween(startDate, endDate)

        for (timeString in timesList) {
            val time = parseTime(timeString)
            for (date in dates){
                addNotification(date, time, drugNameText, dosageText)
            }
        }
    }

    private fun parseTime(timeString: String): LocalTime {
        val timeParts = timeString.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val formattedTime = String.format("%02d:%02d", hour, minute)
        return LocalTime.parse(formattedTime)
    }

    fun getDatesBetween(startDate: String, endDate: String): List<LocalDate> {
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val start = LocalDate.parse(startDate, formatter)
        val end = LocalDate.parse(endDate, formatter)

        val dates = mutableListOf<LocalDate>()
        var current = start
        while (!current.isAfter(end)) {
            dates.add(current)
            current = current.plusDays(1)
        }
        return dates
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyChannel"
            val descriptionText = "Channel for MyApp notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("my_channel_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun addNotification(date: LocalDate, time: LocalTime, drugName: String, dosage: String) {
        val dateTime = LocalDateTime.of(date, time)
        val zoneId = ZoneId.systemDefault()
        val millis = dateTime.atZone(zoneId).toInstant().toEpochMilli()

        scheduleNotification(millis, notificationId++, drugName, dosage)
    }

    private fun scheduleNotification(timeInMillis: Long, notificationId: Int, drugName: String, dosage: String) {
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("notification_id", notificationId)
            putExtra("drugName", drugName)
            putExtra("dosage", dosage)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        cancelAllNotifications()
//    }

    private fun cancelAllNotifications() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (id in 0 until notificationId) {
            val intent = Intent(this, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }


}
