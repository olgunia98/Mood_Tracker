package com.example.mood_tracker.ui.notifications

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mood_tracker.R
import com.example.mood_tracker.databinding.FragmentNotificationsBinding
import java.util.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val selectedTimesList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get reference to the button in the layout
        val addTimeButton: Button = binding.addTimeButton

        // Set OnClickListener to show the time picker dialog
        addTimeButton.setOnClickListener {
            showTimePickerDialog()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker?, hourOfDay: Int, minute: Int ->
                // Handle time selection
                val selectedTime = "$hourOfDay:$minute"
                selectedTimesList.add(selectedTime)
                updateSelectedTimesLayout()

                // You can save the selected time here in SharedPreferences, a ViewModel, or any other storage mechanism
                // For demonstration purposes, I'll just display a Toast
                Toast.makeText(requireContext(), "Selected time: $selectedTime", Toast.LENGTH_SHORT).show()
            },
            hour,
            minute,
            false // Set to true if you want 24-hour format
        )

        // Show the time picker dialog
        timePickerDialog.show()
    }

    private fun updateSelectedTimesLayout() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val selectedTimesLayout: LinearLayout = binding.selectedTimesLayout
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
}


//package com.example.mood_tracker.ui.notifications
//
//import android.app.TimePickerDialog
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import android.widget.TimePicker
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.mood_tracker.databinding.FragmentNotificationsBinding
//import java.util.*
//
//class NotificationsFragment : Fragment() {
//
//    private var _binding: FragmentNotificationsBinding? = null
//    private val binding get() = _binding!!
//
//    private val selectedTimesList = mutableListOf<String>()
//    private lateinit var selectedTimesTextView: TextView
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        // Initialize selected times TextView
//        selectedTimesTextView = binding.selectedTimeTextView
//
//        // Get reference to the button in the layout
//        val timePickerButton: Button = binding.timePickerButton
//
//        // Set OnClickListener to show the time picker dialog
//        timePickerButton.setOnClickListener {
//            showTimePickerDialog()
//        }
//
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun showTimePickerDialog() {
//        val calendar = Calendar.getInstance()
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)
//
//        // Create a new instance of TimePickerDialog
//        val timePickerDialog = TimePickerDialog(
//            requireContext(),
//            { _: TimePicker?, hourOfDay: Int, minute: Int ->
//                // Handle time selection
//                val selectedTime = "$hourOfDay:$minute"
//                selectedTimesList.add(selectedTime)
//                updateSelectedTimesTextView()
//
//                // You can save the selected time here in SharedPreferences, a ViewModel, or any other storage mechanism
//                // For demonstration purposes, I'll just display a Toast
//                Toast.makeText(requireContext(), "Selected time: $selectedTime", Toast.LENGTH_SHORT).show()
//            },
//            hour,
//            minute,
//            false // Set to true if you want 24-hour format
//        )
//
//        // Show the time picker dialog
//        timePickerDialog.show()
//    }
//
//    private fun updateSelectedTimesTextView() {
//        val selectedTimesText = selectedTimesList.joinToString(separator = "\n")
//        selectedTimesTextView.text = selectedTimesText
//    }
//}
