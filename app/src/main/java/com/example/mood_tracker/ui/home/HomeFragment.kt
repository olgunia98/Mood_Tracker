package com.example.mood_tracker.ui.home

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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mood_tracker.R
import com.example.mood_tracker.databinding.FragmentHomeBinding
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale
import android.content.Context
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader

class HomeFragment() : Fragment(), Parcelable {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var text = "nie wybrano emocji"

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Set date text
        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale("pl")).format(calendar)
        binding.xmlTextDate.text = dateFormat

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
        val editTextNote: EditText = root.findViewById(R.id.editTextNote)
        val imageViewAngry: ImageView = root.findViewById(R.id.angry)
        val imageViewSad: ImageView = root.findViewById(R.id.sad)
        val imageViewNeutral: ImageView = root.findViewById(R.id.neutral)
        val imageViewSmile: ImageView = root.findViewById(R.id.smile)
        val imageViewHappy: ImageView = root.findViewById(R.id.happy)
        val buttonSubmit: Button = root.findViewById(R.id.button_submit)

        //var text: String = "Nie wybrano emocji"

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
            appendToFile(root.context, "history.txt", text)
            Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show()
            val dataFromFile = readFromFile(root.context, "history.txt")

            println(dataFromFile)
        }


        return root
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

    fun saveToFile(context: Context, fileName: String, data: String) {
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


