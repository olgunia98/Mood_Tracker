package com.example.mood_tracker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import java.util.*

class Medicine : AppCompatActivity() {

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
            showDatePickerDialog(selectedStartDatesList, findViewById(R.id.selectedStartDatesLayout))
        }

        val addEndDateButton: Button = findViewById(R.id.addEndDateButton)
        addEndDateButton.setOnClickListener {
            showDatePickerDialog(selectedEndDatesList, findViewById(R.id.selectedEndDatesLayout))
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
                val selectedTime = "$hourOfDay:$minute"
                selectedTimesList.add(selectedTime)
                updateSelectedTimesLayout()
                Toast.makeText(this, "Selected time: $selectedTime", Toast.LENGTH_SHORT).show()
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog(dateList: MutableList<String>, layout: LinearLayout) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                dateList.add(selectedDate)
                updateSelectedDatesLayout(dateList, layout)
                Toast.makeText(this, "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun updateSelectedDatesLayout(dateList: MutableList<String>, layout: LinearLayout) {
        layout.removeAllViews()

        for (selectedDate in dateList) {
            val itemView = layoutInflater.inflate(R.layout.item_selected_date, layout, false)
            val dateTextView: TextView = itemView.findViewById(R.id.timeTextView)
            val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

            dateTextView.text = selectedDate
            deleteButton.setOnClickListener {
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
                updateSelectedTimesLayout()
            }

            selectedTimesLayout.addView(itemView)
        }
    }

    private fun saveData() {
        val drugName: EditText = findViewById(R.id.editTextDrugName)
        val dosage: EditText = findViewById(R.id.editTextNumber2)

        val data = Intent().apply {
            putExtra("drugName", drugName.text.toString())
            putExtra("dosage", dosage.text.toString())
            putStringArrayListExtra("startDates", ArrayList(selectedStartDatesList))
            putStringArrayListExtra("endDates", ArrayList(selectedEndDatesList))
            putStringArrayListExtra("times", ArrayList(selectedTimesList))
        }

        setResult(FragmentActivity.RESULT_OK, data)
        finish()
    }
}
