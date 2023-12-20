package com.example.playtomictonyaymane.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.MatchData
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.ui.OpenMatch.MyMatchesAdapter
import com.example.playtomictonyaymane.ui.play.PlayFragment

class ActivitiesFragment: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        //val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)
        //recyclerView.layoutManager = LinearLayoutManager(activity)

        val recyclerViewMyMatches: RecyclerView = requireView().findViewById(R.id.recycler_view)
        val matchesAdapter = RecyclerAdapter(listOf())
        recyclerViewMyMatches.adapter = matchesAdapter
        recyclerViewMyMatches.layoutManager = LinearLayoutManager(requireContext())

        // Fetch and display the user's matches
        MatchData.loadRelevantMatches(recyclerViewMyMatches)

        view?.findViewById<Button>(R.id.btnAddActivity)?.setOnClickListener {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.navigation_play)


        }
    }

}