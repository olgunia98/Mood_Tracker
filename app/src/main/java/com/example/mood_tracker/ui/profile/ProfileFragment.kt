package com.example.mood_tracker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mood_tracker.R
import com.example.mood_tracker.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(){
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var titleTextView: TextView
    private lateinit var instructionTextView: TextView
    private lateinit var startButton: Button

    private lateinit var question0TextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var endTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var descriptionTextView: TextView


    private val questions = arrayOf(
        "1. Niewielkie zainteresowanie lub odczuwanie przyjemności z wykonywania czynności",
        "2. Uczucie smutku, przygnębienia lub beznadziejnośc.",
        "3. Kłopoty z zaśnięciem lub przerywany sen, albo zbyt długi sen",
        "4. Uczucie zmęczenia lub brak energii",
        "5. Brak apetytu lub przejadanie się",
        "6. Poczucie niezadowolenia z siebie — lub uczucie, że jest się do niczego, albo że zawiódł/zawiodła Pan/Pani siebie lub rodzinę",
        "7. Problemy ze skupieniem się na przykład przy czytaniu gazety lub oglądaniu telewizji",
        "8. Poruszanie się lub mówienie tak wolno, że inni mogliby to zauważyć? Albo wręcz przeciwnie — niemożność usiedzenia w miejscu lub podenerwowanie powodujące ruchliwość znacznie większą niż zwykle",
        "9. Myśli, że lepiej byłoby umrzeć, albo chęć zrobienia sobie jakiejś krzywdy"
    )
    private val answers = arrayOf(
        "Wcale nie dokuczały", "Kilka dni", "Więcej niż połowę dni", "Niemal codziennie"
    )
    private val correctAnswers = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var currentQuestion = 0
    private var points = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        titleTextView = root.findViewById(R.id.title_text_view)
        instructionTextView = root.findViewById(R.id.instruction_text_view)
        startButton = root.findViewById(R.id.start_button)

        question0TextView = root.findViewById(R.id.question_text_view_0)
        questionTextView = root.findViewById(R.id.question_text_view)
        answersRadioGroup = root.findViewById(R.id.answers_radio_group)
        submitButton = root.findViewById(R.id.submit_button)
        endTextView = root.findViewById(R.id.end_text_view)
        resultTextView = root.findViewById(R.id.result_text_view)
        descriptionTextView = root.findViewById(R.id.description_text_view)

        question0TextView.visibility = View.GONE
        questionTextView.visibility = View.GONE
        answersRadioGroup.visibility = View.GONE
        submitButton.visibility = View.GONE
        endTextView.visibility = View.GONE
        resultTextView.visibility = View.GONE
        descriptionTextView.visibility = View.GONE

        startButton.setOnClickListener {
            loadQuestion()
            titleTextView.visibility = View.GONE
            instructionTextView.visibility = View.GONE
            startButton.visibility = View.GONE
            question0TextView.visibility = View.VISIBLE
            questionTextView.visibility = View.VISIBLE
            answersRadioGroup.visibility = View.VISIBLE
            submitButton.visibility = View.VISIBLE
        }

        submitButton.setOnClickListener {
            val selectedId = answersRadioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedRadioButton = root.findViewById<RadioButton>(selectedId)
                val answerIndex = answersRadioGroup.indexOfChild(selectedRadioButton)

                points += answerIndex
                //Toast.makeText(this.context, points.toString(), Toast.LENGTH_SHORT).show()

//                if (answerIndex == correctAnswers[currentQuestion]) {
//                    Toast.makeText(this.context, "Correct!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this.context, "Incorrect!", Toast.LENGTH_SHORT).show()
//                }

                currentQuestion++
                if (currentQuestion < questions.size) {
                    loadQuestion()
                } else {
                    finishQuiz()
                }
            } else {
                Toast.makeText(this.context, "Proszę zaznaczyć odpowiedź.", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun loadQuestion() {
        questionTextView.text = questions[currentQuestion]
        for (i in 0 until answersRadioGroup.childCount) {
            (answersRadioGroup.getChildAt(i) as RadioButton).text = answers[i]
        }
        answersRadioGroup.clearCheck()
    }

    private fun finishQuiz() {
        question0TextView.visibility = View.GONE
        questionTextView.visibility = View.GONE
        answersRadioGroup.visibility = View.GONE
        submitButton.visibility = View.GONE
        endTextView.visibility = View.VISIBLE
        endTextView.text = "Wynik testu: " + points.toString()

        val diagnosis = when (points) {
            in 0..4 -> "Brak depresji"
            in 5..9 -> "Łagodna depresja"
            in 10..14 -> "Umiarkowana depresja"
            in 15..19 -> "Umiarkowanie ciężka depresja"
            else -> "Ciężka depresja"
        }

        resultTextView.visibility = View.VISIBLE
        resultTextView.text = diagnosis
        descriptionTextView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
