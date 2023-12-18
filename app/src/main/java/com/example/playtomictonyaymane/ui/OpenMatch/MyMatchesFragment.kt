package com.example.playtomictonyaymane.ui.OpenMatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.AuthData
import com.example.playtomictonyaymane.R
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Locale

class MyMatchesFragment : Fragment() {

    data class Match(val id: String, val title: String, val date: String)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_my_matches, container, false)

        val recyclerViewMyMatches: RecyclerView = view.findViewById(R.id.recyclerViewMyMatches)
        recyclerViewMyMatches.layoutManager = LinearLayoutManager(requireContext())

        // Fetch and display the user's matches
        loadUserMatches(recyclerViewMyMatches)

        return view
    }

    private fun loadUserMatches(recyclerView: RecyclerView) {
        val currentUser = AuthData.auth.currentUser
        val matchList = mutableListOf<Match>()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = AuthData.db.collection("users").document(userId)

            // Create a Firestore query for matches where the user is a player or the owner
            AuthData.db.collection("matches")
                .whereArrayContains("players", userRef)
                .get()
                .addOnSuccessListener { matchesSnapshot ->
                    for (document in matchesSnapshot) {
                        val id = document.id
                        val title = document.getString("matchType") ?: "Unknown"
                        val dateTimestamp = document.getTimestamp("date")
                        val formattedDate = dateTimestamp?.toDate()?.let { sdf.format(it) } ?: "Unknown"

                        matchList.add(Match(id, title, formattedDate))
                    }
                    // Set up adapter
                    val adapter = MyMatchesAdapter(matchList)
                    recyclerView.adapter = adapter
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }


            // Additionally fetch matches where current user is the owner
            AuthData.db.collection("matches")
                .whereEqualTo("owner", userRef)
                .get()
                .addOnSuccessListener { matchesSnapshot ->
                    for (document in matchesSnapshot) {
                        val id = document.id
                        val title = document.getString("matchType") ?: "Unknown"
                        val dateTimestamp = document.getTimestamp("date")
                        val formattedDate = dateTimestamp?.toDate()?.let { sdf.format(it) } ?: "Unknown"

                        // Check if match already added from previous query
                        val matchExists = matchList.any { it.id == id }
                        if (!matchExists) {
                            matchList.add(Match(id, title, formattedDate))
                        }
                    }

                    // Since we might have additional data, notify the adapter
                    val adapter = recyclerView.adapter as? MyMatchesAdapter
                    adapter?.updateMatches(matchList)
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }
}