package com.example.mood_tracker.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Gravity
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
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
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

        // Set date text
//        val calendar = Calendar.getInstance().time
//        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale("pl")).format(calendar)
//        binding.xmlTextDate.text = dateFormat

        // Setup image click listeners
//        val imageIds = arrayOf(R.id.angry, R.id.sad, R.id.neutral, R.id.smile, R.id.happy)
//        for (imageId in imageIds) {
//            val imageView = root.findViewById<ImageView>(imageId)
//            imageView.setOnClickListener {
//                // Reset colors of all images
//                for (id in imageIds) {
//                    root.findViewById<ImageView>(id).setBackgroundColor(Color.TRANSPARENT)
//                }
//                // Set new color for clicked image
//                imageView.setBackgroundColor(Color.BLUE)
//            }
//        }
        var mood = ""
//        val editTextNote: EditText = root.findViewById(R.id.editTextNote)
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
            // Co się stanie po kliknięciu przycisku
//            if(!imageViewAngry.isSelected) {
//                text = "angry"
//            }
            //text = editTextNote.text.toString()
            if (mood in setOf("fatalny", "zły", "neutralny", "dobry", "doskonały")){
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val currentTime = timeFormat.format(Date())
                val note = editTextInput.text
                editTextInput.setText("")
                // val text = "$mood $currentDate $currentTime"
//                val text = "$mood $currentDate $note"
//                appendToFile(root.context, "history.txt", text)

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

    fun appendToFile(context: Context, fileName: String, data: String) {
        try {
            val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND)
            fos.write((data + "\n").toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToFile(context: Context, fileName: String, data: String) {
        try {
            val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fos.write(data.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readFromFile(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val fis = context.openFileInput(fileName)
            val isr = InputStreamReader(fis)
            val bufferedReader = BufferedReader(isr)
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            isr.close()
            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    private fun updateHistoryLayout(context: Context) {
        //val layoutInflater = LayoutInflater.from(requireContext())
//        val historyLayout: LinearLayout = binding.historyLayout
//        historyLayout.removeAllViews()
//
//        val dataFromFile = readFromFile(context, "history.txt")
//        val historyList = dataFromFile.split('\n')
//
//        for (historyRecord in historyList) {
//            //val itemView = layoutInflater.inflate(R.layout.item_selected_time, historyLayout, false)
//            //val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
//            val timeTextView: TextView = TextView(historyLayout.context)
//
//            timeTextView.text = historyRecord
//            historyLayout.addView(timeTextView)
//
//            //historyLayout.addView(itemView)
//        }

        tableContentLeft.removeAllViews()
        tableContentMiddle.removeAllViews()
        tableContentRight.removeAllViews()

//        val dataFromFile = readFromFile(context, "history.txt")
//        val historyList = dataFromFile.split('\n')

        val rowsLeft = mutableListOf<TableRow>()
        val rowsMiddle = mutableListOf<TableRow>()
        val rowsRight = mutableListOf<TableRow>()

        val historyList = readFromJsonFile(context.filesDir, "history.json")
        var index = 0
        for (historyRecord in historyList) {
            val mood = historyRecord.getString("mood")
            val date = historyRecord.getString("date")
            val note = historyRecord.getString("note")
            //val historyRecordList = historyRecord.split(' ')
//            if (historyRecordList.size == 3) {
//                val mood = historyRecordList[0]
//                val date = historyRecordList[1]
//                val time = historyRecordList[2]

            val layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1f

            // Tworzenie nowego wiersza dla lewej kolumny
            val tableRowLeft = TableRow(context)
            val textViewLeft = TextView(context)
            textViewLeft.text = mood
            textViewLeft.setPadding(8, 8, 8, 8)
            textViewLeft.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewLeft.layoutParams = layoutParams
            tableRowLeft.addView(textViewLeft)

            val tableRowMiddle = TableRow(context)
            val textViewMiddle = TextView(context)
            textViewMiddle.text = date
            textViewMiddle.setPadding(8, 8, 8, 8)
            textViewMiddle.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textViewMiddle.layoutParams = layoutParams
            tableRowMiddle.addView(textViewMiddle)

            // Tworzenie nowego wiersza dla prawej kolumny
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
//            when (mood) {
//                "fatalny" -> {
//                    tableRowLeft.setBackgroundColor(Color.rgb(233,57,57))
//                    tableRowMiddle.setBackgroundColor(Color.rgb(233,57,57))
//                    tableRowRight.setBackgroundColor(Color.rgb(233,57,57))
//                }
//                "zły" -> {
//                    tableRowLeft.setBackgroundColor(Color.rgb(239,113,3))
//                    tableRowMiddle.setBackgroundColor(Color.rgb(239,113,3))
//                    tableRowRight.setBackgroundColor(Color.rgb(239,113,3))
//                }
//                "neutralny" -> {
//                    tableRowLeft.setBackgroundColor(Color.rgb(255,189,5))
//                    tableRowMiddle.setBackgroundColor(Color.rgb(255,189,5))
//                    tableRowRight.setBackgroundColor(Color.rgb(255,189,5))
//                }
//                "dobry" -> {
//                    tableRowLeft.setBackgroundColor(Color.rgb(208,221,56))
//                    tableRowMiddle.setBackgroundColor(Color.rgb(208,221,56))
//                    tableRowRight.setBackgroundColor(Color.rgb(208,221,56))
//                }
//                "doskonały" -> {
//                    tableRowLeft.setBackgroundColor(Color.rgb(134,192,72))
//                    tableRowMiddle.setBackgroundColor(Color.rgb(134,192,72))
//                    tableRowRight.setBackgroundColor(Color.rgb(134,192,72))
//                }
//            }

            rowsLeft.add(tableRowLeft)
            rowsMiddle.add(tableRowMiddle)
            rowsRight.add(tableRowRight)
//            }
//            else {
//                continue
//            }
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


