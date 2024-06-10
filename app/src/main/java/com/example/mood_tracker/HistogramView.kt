package com.example.mood_tracker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.example.mood_tracker.databinding.FragmentHomeBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistogramView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val barColors = arrayOf(Color.rgb(233,57,57), Color.rgb(239,113,3), Color.rgb(255,189,5), Color.rgb(208,221,56), Color.rgb(134,192,72))
    private val labels = arrayOf("fatalny", "zły", "neutralny", "dobry", "doskonały")
    private val title = "Częstość nastrojów w całej historii"
    private val title2 = "Częstość nastrojów w ostatnim tygodniu"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val data = floatArrayOf(0f, 0f, 0f, 0f, 0f)
        val data2 = floatArrayOf(0f, 0f, 0f, 0f, 0f)
        val historyList = readFromJsonFile(context.filesDir, "history.json")
        for (historyRecord in historyList) {
            val mood = historyRecord.getString("mood")
            val date = historyRecord.getString("date")
            when (mood) {
                "fatalny" -> data[0] += 1f
                "zły" -> data[1] += 1f
                "neutralny" -> data[2] += 1f
                "dobry" -> data[3] += 1f
                "doskonały" -> data[4] += 1f
            }
            if (isDateInLastWeek(date)){
                when (mood) {
                    "fatalny" -> data2[0] += 1f
                    "zły" -> data2[1] += 1f
                    "neutralny" -> data2[2] += 1f
                    "dobry" -> data2[3] += 1f
                    "doskonały" -> data2[4] += 1f
                }
            }
        }

        val width = width
        val height = height
        val barWidth = width.toFloat() / data.size

        val paint = Paint()
        paint.color = Color.BLACK

        paint.textSize = 50f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val histogramHeight = 570f
        // górny wykres
        val titleWidth = paint.measureText(title)
        canvas.drawText(title, (width - titleWidth) / 2, 50f, paint)
        var maxValue = data.maxOrNull() ?: 0f
        for (i in data.indices) {
            paint.color = barColors[i]
            val left = barWidth * (i-1) + barWidth * 1.1f
            val top = histogramHeight * (1 - data[i] / maxValue) + 80f
            val right = barWidth * i + 0.9f * barWidth
            val bottom = histogramHeight + 80f
            canvas.drawRect(left, top, right, bottom, paint)

            // Etykieta pod belką
            paint.color = Color.BLACK
            paint.textSize = 40f
            val labelWidth = paint.measureText(labels[i])
            val labelX = barWidth * i + (barWidth - labelWidth) / 2 // Wypośrodkowanie etykiety
            val labelY = 700 - 5f // Pozycja etykiety pod belką
            canvas.drawText(labels[i], labelX, labelY.toFloat(), paint)
        }

        // dolny wykres
        val shift = 800f
        //canvas.drawLine(0f, 0f, width * 1f, 0f, paint)
        canvas.drawLine(0f, shift - 50f, width * 1f, shift - 50f, paint)
        paint.textSize = 50f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val titleWidth2 = paint.measureText(title2)
        canvas.drawText(title2, (width - titleWidth2) / 2, 50f + shift, paint)
        maxValue = data2.maxOrNull() ?: 0f
        for (i in data2.indices) {
            paint.color = barColors[i]
            val left = barWidth * (i-1) + barWidth * 1.1f
            val top = histogramHeight * (1 - data2[i] / maxValue) + 80f
            val right = barWidth * i + 0.9f * barWidth
            val bottom = histogramHeight + 80f
            canvas.drawRect(left, top + shift, right, bottom + shift, paint)

            // Etykieta pod belką
            paint.color = Color.BLACK
            paint.textSize = 40f
            val labelWidth = paint.measureText(labels[i])
            val labelX = barWidth * i + (barWidth - labelWidth) / 2 // Wypośrodkowanie etykiety
            val labelY = 700 - 5f // Pozycja etykiety pod belką
            canvas.drawText(labels[i], labelX, labelY.toFloat() + shift, paint)
        }
    }

    fun isDateInLastWeek(dateStr: String): Boolean {
        // Format daty
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        return try {
            val givenDate: Date = dateFormat.parse(dateStr) ?: return false

            // Bieżąca data
            val currentDate = Calendar.getInstance()

            // Data sprzed tygodnia
            val lastWeekDate = Calendar.getInstance()
            lastWeekDate.add(Calendar.DAY_OF_YEAR, -7)

            // Sprawdź, czy dana data mieści się w przedziale ostatniego tygodnia
            givenDate.after(lastWeekDate.time) && givenDate.before(currentDate.time)
        } catch (e: ParseException) {
            // Zwróć false, jeśli nie uda się sparsować daty
            false
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

    private fun saveToFile(context: Context, fileName: String, data: String) {
        try {
            val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fos.write(data.toByteArray())
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
