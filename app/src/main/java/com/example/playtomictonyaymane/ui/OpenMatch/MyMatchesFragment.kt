package com.example.playtomictonyaymane.ui.OpenMatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.AuthData
import com.example.playtomictonyaymane.MatchData
import com.example.playtomictonyaymane.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Locale

class MyMatchesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_my_matches, container, false)

        val recyclerViewMyMatches: RecyclerView = view.findViewById(R.id.recyclerViewMyMatches)
        val matchesAdapter = MyMatchesAdapter(listOf())
        recyclerViewMyMatches.adapter = matchesAdapter
        recyclerViewMyMatches.layoutManager = LinearLayoutManager(requireContext())

        // Fetch and display the user's matches
        MatchData.loadRelevantMatches(recyclerViewMyMatches)

        return view
    }

}