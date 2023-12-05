package com.example.playtomictonyaymane.ui.court

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentCourtbookingBinding
import com.example.playtomictonyaymane.databinding.FragmentPlayBinding
import com.example.playtomictonyaymane.ui.dashboard.DashboardFragment
import com.example.playtomictonyaymane.ui.tabs.TimeSlotAdapter


class BookingCourtFragment:Fragment() {
    private var _binding: FragmentCourtbookingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCourtbookingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Zoek de button op basis van de ID
        val buttonStartGame = view.findViewById<Button>(R.id.buttonStartGame)
        val editTextPlayerName = view.findViewById<EditText>(R.id.editTextPlayerName)
        val editTextGameCode = view.findViewById<EditText>(R.id.editTextGameCode)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val recyclerViewTimeSlots = view.findViewById<RecyclerView>(R.id.recyclerViewTimeSlots)

        val timeSlots = generateTimeSlots()

        val adapter = TimeSlotAdapter(timeSlots) { selectedTimeSlot ->
            // Voer acties uit met de geselecteerde tijdslot
        }

        recyclerViewTimeSlots.layoutManager = GridLayoutManager(requireContext(),5)
        recyclerViewTimeSlots.adapter = adapter


        // Voeg een klikgebeurtenis toe aan de knop
        buttonStartGame.setOnClickListener {
            val playerName = editTextPlayerName.text.toString()
            val gameCode = editTextGameCode.text.toString()
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth



            val db = DashboardFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            // Vervang het huidige fragment door PlayFragment
            transaction.replace(R.id.container, db)
            transaction.addToBackStack(null)
            transaction.commit()

        }
    }


    private fun generateTimeSlots(): List<String> {
        val timeSlots = mutableListOf<String>()
        val startTime = 9 // Startuur
        val endTime = 17// Einduur

        val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())

        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, startTime)
        calendar.set(java.util.Calendar.MINUTE, 0)

        while (calendar.get(java.util.Calendar.HOUR_OF_DAY) < endTime) {
            val timeSlot = timeFormat.format(calendar.time)
            timeSlots.add(timeSlot)
            calendar.add(java.util.Calendar.MINUTE, 30)
        }

        return timeSlots
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}