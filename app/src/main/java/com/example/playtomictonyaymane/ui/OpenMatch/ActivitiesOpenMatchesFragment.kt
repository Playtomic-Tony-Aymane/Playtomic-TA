package com.example.playtomictonyaymane.ui.OpenMatch

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R

class ActivitiesOpenMatchesFragment :Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_open_match_activities, container, false)

        // Ophalen van de views uit de XML
        val recyclerView = view.findViewById<RecyclerView>(R.id.matchesRecyclerView)


        // Simulatie van lijst met bestaande matches
        val existingMatchesList = listOf(
            Match("Match 1", "2023-12-15", "10:00", "Field A"),
            Match("Match 2", "2023-12-20", "14:30", "Field B"),
            Match("Match 2", "2023-12-20", "14:30", "Field B"),
            Match("Match 2", "2023-12-20", "14:30", "Field B"),
            Match("Match 2", "2023-12-20", "14:30", "Field B"),
            Match("Match 2", "2023-12-20", "14:30", "Field B"),
            Match("Match 2", "2023-12-20", "14:30", "Field B"),
            Match("Match 2", "2023-12-20", "14:30", "Field B"),

            // Voeg meer matches toe zoals nodig
        )

        // Zet de RecyclerView op met een aangepaste adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val matchesAdapter = ActivitiesOpenMatchAdapter(existingMatchesList)
        recyclerView.adapter = matchesAdapter




        return view
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
