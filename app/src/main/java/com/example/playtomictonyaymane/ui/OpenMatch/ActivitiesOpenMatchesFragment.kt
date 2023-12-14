package com.example.playtomictonyaymane.ui.OpenMatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.AuthData
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentOpenMatchActivitiesBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ActivitiesOpenMatchesFragment :Fragment(){

    private var  _binding: FragmentOpenMatchActivitiesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenMatchActivitiesBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView = view.findViewById<RecyclerView>(R.id.matchesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the matches list
        val existingMatchesList = mutableListOf<Match>()

        // Initialize the adapter with an empty list first
        val matchesAdapter = ActivitiesOpenMatchAdapter(existingMatchesList)
        recyclerView.adapter = matchesAdapter

        loadMatchesData(existingMatchesList, matchesAdapter)

        binding.addMatchButton.setOnClickListener {
            // Navigation code
        }

        return view
    }

    private fun loadMatchesData(matchesList: MutableList<Match>, adapter: ActivitiesOpenMatchAdapter) {
        val currentTime = Calendar.getInstance().time // Get the current time

        AuthData.db.collection("matches")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val matchId = document.id
                    val matchType = document.getString("matchType") ?: "Unknown"
                    val matchGender = document.getString("matchGender") ?: "Unknown"
                    val bookingRef = document.getDocumentReference("booking")

                    bookingRef?.get()?.addOnSuccessListener { bookingDoc ->
                        val courtRef = bookingDoc.getDocumentReference("court")
                        val date = bookingDoc.getTimestamp("date")?.toDate()
                        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val formattedDate = sdfDate.format(date)
                        val formattedTime = sdfTime.format(date)

                        courtRef?.get()?.addOnSuccessListener { courtDoc ->
                            val courtName = courtDoc.getString("name") ?: "Unknown"
                            val courtAddress = courtDoc.getString("address") ?: "Unknown"

                            val match = Match(
                                name = "Match $matchId",
                                date = formattedDate,
                                time = formattedTime,
                                fieldName = "$courtName ($courtAddress)"
                            )
                            matchesList.add(
                                match
                            )

                            adapter.notifyDataSetChanged() // Notify the adapter about the updated list
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }


    private fun showMatchDetails(selectedMatch: Match) {
        // Implementeer logica om de details van de geselecteerde match te tonen
        // Pas de datum, tijd, veldnaam, spelersgrid, matchtype, niveau, prijs, etc. aan met de gegevens van de geselecteerde match
    }

    // Voeg een model toe voor de Match-gegevens
    data class Match(
        val name: String,
        val date: String,
        val time: String,
        val fieldName: String
        // Voeg hier meer gegevens toe zoals spelers, matchtype, niveau, prijs, etc.
    )
}
