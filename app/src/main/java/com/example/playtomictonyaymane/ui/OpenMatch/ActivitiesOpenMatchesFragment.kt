package com.example.playtomictonyaymane.ui.OpenMatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.MatchData
import com.example.playtomictonyaymane.MatchData.loadMatchesData
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentOpenMatchActivitiesBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Locale


class ActivitiesOpenMatchesFragment :Fragment(){

    private var  _binding: FragmentOpenMatchActivitiesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenMatchActivitiesBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView = view.findViewById<RecyclerView>(R.id.matchesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the matches list
        val existingMatchesList = mutableListOf<MatchData.Match>()

        // Initialize the adapter with an empty list first
        val matchesAdapter = ActivitiesOpenMatchAdapter(existingMatchesList, object : MatchUpdateListener {
            override fun onMatchUpdated(matchId: String) {
                // Code to refresh matches in case of update
                // Depending on your implementation, this might involve fetching the updated list
                // from Firestore again, updating your existing list, or just refreshing the RecyclerView

                // Example:
                // This will ask the adapter to only update the affected item if matchId is used to fetch matches again
                loadMatchesListAndUpdateAdapter(recyclerView)
            }
        })
        recyclerView.adapter = matchesAdapter

        loadMatchesData(recyclerView)

        binding.addMatchButton.setOnClickListener {
            // Navigation code
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_openMatchesFragment_to_addMatchesFragment)
        }

        return view
    }

    private fun loadMatchesListAndUpdateAdapter(recyclerView: RecyclerView) {
        loadMatchesData(recyclerView)
    }
}
