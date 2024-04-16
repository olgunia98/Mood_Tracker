package com.example.mood_tracker.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mood_tracker.R
import com.example.mood_tracker.databinding.FragmentHomeBinding
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment() : Fragment(), Parcelable {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        val imageIds = arrayOf(R.id.angry, R.id.sad, R.id.neutral, R.id.smile, R.id.happy)
        for (imageId in imageIds) {
            val imageView = root.findViewById<ImageView>(imageId)
            imageView.setOnClickListener {
                // Reset colors of all images
                for (id in imageIds) {
                    root.findViewById<ImageView>(id).setBackgroundColor(Color.TRANSPARENT)
                }
                // Set new color for clicked image
                imageView.setBackgroundColor(Color.BLUE)
            }
        }

        return root
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


