package com.example.playtomictonyaymane.ui.OpenMatch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.AuthData
import com.example.playtomictonyaymane.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddMatchesFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var clubsAdapter: FieldsAdapter
    var clubsAvailabilityList = mutableListOf<ClubAvailability>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_match_list, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.clubsAvailabilityRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadBookedClubsAvailability()

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        //do something with your id
        return super.onOptionsItemSelected(item)
    }

    private fun loadBookedClubsAvailability() {
        val currentUser = AuthData.auth.currentUser
        val currentTime = Calendar.getInstance()
        val matchesRef = AuthData.db.collection("matches")

        // First, get all matches to find out which bookings are already part of a match
        matchesRef.get().addOnSuccessListener { matchesSnapshot ->
            // Collect booking references from matches
            val bookedRefsInMatches = matchesSnapshot.documents.mapNotNull { it.getDocumentReference("booking") }.toSet()

            // Then, query bookings and filter out those that are referenced in matches
            AuthData.db.collection("bookings")
                .whereEqualTo("owner", AuthData.db.collection("users").document(currentUser!!.uid))
                .whereGreaterThanOrEqualTo("date", currentTime.time)
                .get()
                .addOnSuccessListener { bookingsSnapshot ->
                    val clubsAvailabilityList = mutableListOf<ClubAvailability>()

                    for (document in bookingsSnapshot) {
                        val courtRef = document.getDocumentReference("court")
                        val bookingRef = document.reference
                        val startTime = document.getTimestamp("date")?.toDate()

                        // Skip if this booking is already in a match
                        if (bookingRef in bookedRefsInMatches) {
                            continue
                        }

                        courtRef?.get()?.addOnSuccessListener { courtDoc ->
                            val courtName = courtDoc.getString("name") ?: "Unknown"
                            val dayFormatted = SimpleDateFormat("dd MMM", Locale.getDefault()).format(startTime)
                            val startTimeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(startTime)

                            clubsAvailabilityList.add(
                                ClubAvailability(
                                    clubName = courtName,
                                    day = dayFormatted,
                                    startTimes = listOf(startTimeFormatted),
                                    bookingId = document.id
                                )
                            )

                            // Since we're in an async callback, update the adapter every time the list is modified
                            updateRecyclerView(clubsAvailabilityList)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the failure, e.g., show a message to the user
                    Log.e("AddMatchesFragment", "Error fetching bookings: ${e.message}", e)
                }
        }
    }

    private fun updateRecyclerView(clubsAvailabilityList: List<ClubAvailability>) {
        val clubsAdapter = FieldsAdapter(clubsAvailabilityList)
        recyclerView.adapter = clubsAdapter
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
        val day: String, // This could represent the date of the booking
        val startTimes: List<String>, // This would be single-item lists containing just the start time of the booking
        val bookingId: String
    )
}