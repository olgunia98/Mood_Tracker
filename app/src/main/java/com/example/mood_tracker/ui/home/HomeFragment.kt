package com.example.mood_tracker.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mood_tracker.R
import com.example.mood_tracker.databinding.FragmentHomeBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeFragment() : Fragment(), Parcelable {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    constructor(parcel: Parcel) : this() {

    }

    private lateinit var tableContentLeft: TableLayout
    private lateinit var tableContentMiddle: TableLayout
    private lateinit var tableContentRight: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tableContentLeft = root.findViewById(R.id.table_content_left)
        tableContentMiddle = root.findViewById(R.id.table_content_middle)
        tableContentRight = root.findViewById(R.id.table_content_right)

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        var mood = ""
        val imageViewAngry: ImageView = root.findViewById(R.id.angry)
        val imageViewSad: ImageView = root.findViewById(R.id.sad)
        val imageViewNeutral: ImageView = root.findViewById(R.id.neutral)
        val imageViewSmile: ImageView = root.findViewById(R.id.smile)
        val imageViewHappy: ImageView = root.findViewById(R.id.happy)
        val editTextInput: EditText = root.findViewById(R.id.editTextInput)
        val buttonSubmit: Button = root.findViewById(R.id.button_submit)

        imageViewAngry.setOnClickListener {
            imageViewAngry.alpha = 1.0F
            imageViewSad.alpha = 0.5F
            imageViewNeutral.alpha = 0.5F
            imageViewSmile.alpha = 0.5F
            imageViewHappy.alpha = 0.5F
            mood = "fatalny"
        }

        imageViewSad.setOnClickListener {
            imageViewAngry.alpha = 0.5F
            imageViewSad.alpha = 1.0F
            imageViewNeutral.alpha = 0.5F
            imageViewSmile.alpha = 0.5F
            imageViewHappy.alpha = 0.5F
            mood = "zły"
        }

        imageViewNeutral.setOnClickListener {
            imageViewAngry.alpha = 0.5F
            imageViewSad.alpha = 0.5F
            imageViewNeutral.alpha = 1F
            imageViewSmile.alpha = 0.5F
            imageViewHappy.alpha = 0.5F
            mood = "neutralny"
        }

        imageViewSmile.setOnClickListener {
            imageViewAngry.alpha = 0.5F
            imageViewSad.alpha = 0.5F
            imageViewNeutral.alpha = 0.5F
            imageViewSmile.alpha = 1.0F
            imageViewHappy.alpha = 0.5F
            mood = "dobry"
        }

        imageViewHappy.setOnClickListener {
            imageViewAngry.alpha = 0.5F
            imageViewSad.alpha = 0.5F
            imageViewNeutral.alpha = 0.5F
            imageViewSmile.alpha = 0.5F
            imageViewHappy.alpha = 1.0F
            mood = "doskonały"
        }

        buttonSubmit.setOnClickListener {
            if (mood in setOf("fatalny", "zły", "neutralny", "dobry", "doskonały")){
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val currentTime = timeFormat.format(Date())
                val note = editTextInput.text
                editTextInput.setText("")

                val jsonObject = JSONObject()
                jsonObject.put("mood", mood)
                jsonObject.put("date", currentDate)
                jsonObject.put("note", note)
                appendToJsonFile(root.context.filesDir, "history.json", jsonObject)

                updateHistoryLayout(root.context)
                Toast.makeText(root.context, "Dodano do rejestru.", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(root.context, "Nie wybrano emocji.", Toast.LENGTH_SHORT).show()
            }
        }
        updateHistoryLayout(root.context)

        return root
    }

    private fun appendToJsonFile(directory: File, fileName: String, jsonObject: JSONObject) {
        val file = File(directory, fileName)
        try {
            val jsonArray = if (file.exists()) {
                val content = file.readText()
                if (content.isNotEmpty()) {
                    JSONObject(content).getJSONArray("history")
                } else {
                    JSONArray()
                }
            } else {
                JSONArray()
            }
            jsonArray.put(jsonObject)
            val jsonToWrite = JSONObject().put("history", jsonArray)

            FileWriter(file).use { writer ->
                writer.write(jsonToWrite.toString())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFromJsonFile(directory: File, fileName: String): List<JSONObject> {
        val file = File(directory, fileName)
        val historyList = mutableListOf<JSONObject>()
        if (file.exists()) {
            try {
                val content = file.readText(Charset.defaultCharset())
                if (content.isNotEmpty()) {
                    val jsonArray = JSONObject(content).getJSONArray("history")
                    for (i in 0 until jsonArray.length()) {
                        historyList.add(jsonArray.getJSONObject(i))
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return historyList
    }

    private fun updateHistoryLayout(context: Context) {
        tableContentLeft.removeAllViews()
        tableContentMiddle.removeAllViews()
        tableContentRight.removeAllViews()

        val rowsLeft = mutableListOf<TableRow>()
        val rowsMiddle = mutableListOf<TableRow>()
        val rowsRight = mutableListOf<TableRow>()

        val historyList = readFromJsonFile(context.filesDir, "history.json")
        var index = 0
        for (historyRecord in historyList) {
            val mood = historyRecord.getString("mood")
            val date = historyRecord.getString("date")
            val note = historyRecord.getString("note")

            val layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1f

            val tableRowLeft = TableRow(context)
            val textViewLeft = TextView(context)
            textViewLeft.text = date
            textViewLeft.setPadding(8, 8, 8, 8)
            textViewLeft.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewLeft.layoutParams = layoutParams
            tableRowLeft.addView(textViewLeft)

            val tableRowMiddle = TableRow(context)
            val textViewMiddle = TextView(context)
            textViewMiddle.text = mood
            textViewMiddle.setPadding(8, 8, 8, 8)
            textViewMiddle.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewMiddle.layoutParams = layoutParams
            tableRowMiddle.addView(textViewMiddle)

            val tableRowRight = TableRow(context)
            val textViewRight = TextView(context)
            textViewRight.text = note
            textViewRight.setPadding(8, 8, 8, 8)
            textViewRight.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewRight.layoutParams = layoutParams
            tableRowRight.addView(textViewRight)

            if (index % 2 == 0){
                tableRowLeft.setBackgroundColor(Color.LTGRAY)
                tableRowMiddle.setBackgroundColor(Color.LTGRAY)
                tableRowRight.setBackgroundColor(Color.LTGRAY)
            }
            else {
                tableRowLeft.setBackgroundColor(Color.WHITE)
                tableRowMiddle.setBackgroundColor(Color.WHITE)
                tableRowRight.setBackgroundColor(Color.WHITE)
            }
            index++
            rowsLeft.add(tableRowLeft)
            rowsMiddle.add(tableRowMiddle)
            rowsRight.add(tableRowRight)
        }

        for (i in rowsLeft.size - 1 downTo 0) {
            tableContentLeft.addView(rowsLeft[i])
            tableContentMiddle.addView(rowsMiddle[i])
            tableContentRight.addView(rowsRight[i])
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeFragment> {
        override fun createFromParcel(parcel: Parcel): HomeFragment {
            return HomeFragment(parcel)
        }

        override fun newArray(size: Int): Array<HomeFragment?> {
            return arrayOfNulls(size)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }}


