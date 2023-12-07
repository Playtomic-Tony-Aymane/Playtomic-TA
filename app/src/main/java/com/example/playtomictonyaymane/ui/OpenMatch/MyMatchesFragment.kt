package com.example.playtomictonyaymane.ui.OpenMatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R

class MyMatchesFragment:Fragment() {

    // Voorbeeld van een modelklasse voor een Match
    data class Match(val id: Int, val title: String, val date: String)

    // Een lijst van bestaande matches voorbereiden
    private fun prepareExistingMatches(): List<Match> {
        val matchList = mutableListOf<Match>()
        matchList.add(Match(1, "Match 1", "2023-12-01"))
        matchList.add(Match(2, "Match 2", "2023-12-03"))
        matchList.add(Match(3, "Match 3", "2023-12-05"))
        // Voeg hier meer matches toe op dezelfde manier
        return matchList
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_my_matches, container, false)

        val recyclerViewMyMatches: RecyclerView = view.findViewById(R.id.recyclerViewMyMatches)
        recyclerViewMyMatches.layoutManager = LinearLayoutManager(requireContext())

        // Gebruik een adapter om de lijst van jouw matches in te vullen
        val myMatches = prepareExistingMatches()
        val adapter = MyMatchesAdapter(myMatches)
        recyclerViewMyMatches.adapter = adapter

        return view
    }
}