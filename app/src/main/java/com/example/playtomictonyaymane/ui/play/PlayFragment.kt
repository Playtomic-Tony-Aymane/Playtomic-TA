package com.example.playtomictonyaymane.ui.play
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentPlayBinding

class PlayFragment: Fragment() {
    private var _binding: FragmentPlayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val playViewModel =
            ViewModelProvider(this).get(PlayViewModel::class.java)

        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Zoek de button op basis van de ID
        val buttonStartGame = view.findViewById<Button>(R.id.buttonStartGame)

        // Zoek de DatePicker en TimePicker op basis van de ID's
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)

        // Voeg een klikgebeurtenis toe aan de knop
        buttonStartGame.setOnClickListener {
            // Verkrijg geselecteerde datum en tijd
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth

            val hour = timePicker.hour
            val minute = timePicker.minute

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
