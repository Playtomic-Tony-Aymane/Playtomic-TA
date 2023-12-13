package com.example.playtomictonyaymane.ui.OpenMatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R

class AddMatchesFragment:Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_match_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.clubsAvailabilityRecyclerView)

        val clubsAvailabilityList = listOf(
            ClubAvailability("Hanger Padel Club", "Tomorrow", generateTimeSlots()),
            ClubAvailability("Example Club", "Today", generateTimeSlots()),
            ClubAvailability("Another Club", "10 Dec", generateTimeSlots()),
            ClubAvailability("Another Club", "Tomorrow", generateTimeSlots()),
            // Voeg meer clubs toe zoals nodig
        )


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val clubsAdapter = FieldsAdapter(clubsAvailabilityList)
        recyclerView.adapter = clubsAdapter



        return view


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
    data class ClubAvailability(
        val clubName: String,
        val day: String,
        val timeSlot: List<String> ,
        // Andere gegevens zoals beschikbaarheidstijden, etc.
    )
}