package com.example.mood_tracker.ui.notifications

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mood_tracker.R
import com.example.mood_tracker.databinding.FragmentNotificationsBinding
import com.example.mood_tracker.Medicine

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE_ADD_MEDICINE = 1

    private val medicinesList = mutableListOf<Medicine>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonNewMedicine: Button = binding.root.findViewById(R.id.new_medicine)
        buttonNewMedicine.setOnClickListener {
            val intent = Intent(activity, com.example.mood_tracker.Medicine::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_MEDICINE)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_MEDICINE && resultCode == Activity.RESULT_OK && data != null) {
            val drugName = data.getStringExtra("drugName")
            val dosage = data.getStringExtra("dosage")
            val startDates = data.getStringArrayListExtra("startDates")
            val endDates = data.getStringArrayListExtra("endDates")
            val times = data.getStringArrayListExtra("times")

            if (drugName != null && dosage != null && startDates != null && endDates != null && times != null) {
                val medicine = Medicine(drugName, dosage, startDates, endDates, times)
                medicinesList.add(medicine)
                updateMedicinesLayout()
            } else {
                Toast.makeText(context, "Failed to add medicine", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateMedicinesLayout() {
        val medicinesLayout: LinearLayout = binding.root.findViewById(R.id.medicinesLayout)
        medicinesLayout.removeAllViews()

        for (medicine in medicinesList) {
            val itemView = layoutInflater.inflate(R.layout.item_medicine, medicinesLayout, false)
            val drugNameTextView: TextView = itemView.findViewById(R.id.drugNameTextView)
            val dosageTextView: TextView = itemView.findViewById(R.id.dosageTextView)
            val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

            drugNameTextView.text = medicine.drugName
            dosageTextView.text = medicine.dosage
            deleteButton.setOnClickListener {
                medicinesList.remove(medicine)
                updateMedicinesLayout()
            }

            medicinesLayout.addView(itemView)
        }
    }

    data class Medicine(
        val drugName: String,
        val dosage: String,
        val startDates: ArrayList<String>,
        val endDates: ArrayList<String>,
        val times: ArrayList<String>
    )
}
