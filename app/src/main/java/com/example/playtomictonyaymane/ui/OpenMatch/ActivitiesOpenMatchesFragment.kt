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
    ): View? {
        _binding = FragmentOpenMatchActivitiesBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView = view.findViewById<RecyclerView>(R.id.matchesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the matches list
        val existingMatchesList = mutableListOf<MyMatchesFragment.Match>()

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

    private fun loadMatchesData(recyclerView: RecyclerView) {
        val matchList = mutableListOf<MyMatchesFragment.Match>()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        AuthData.db.collection("matches")
            .get()
            .addOnSuccessListener { matchesSnapshot ->
                // Iterate over each document (match) in the snapshot
                for (matchDocument in matchesSnapshot) {
                    // Fetch court name and date using the booking reference
                    val taskCourtAndDate = matchDocument.getDocumentReference("booking")?.get()?.continueWithTask { bookingTask ->
                        val bookingSnapshot = bookingTask.result
                        val courtRef = bookingSnapshot?.getDocumentReference("court")
                        val dateTimestamp = bookingSnapshot?.getTimestamp("date")
                        val courtTask = courtRef?.get()
                        Tasks.whenAllComplete(courtTask).continueWith { courtTaskResult ->
                            // Now we have both results from booking and court
                            val courtSnapshot = courtTaskResult.result.firstOrNull()?.result as? DocumentSnapshot
                            val courtName = courtSnapshot?.getString("name") ?: "Unknown"
                            val courtAddress = courtSnapshot?.getString("address") ?: "Unknown"
                            val date = dateTimestamp?.toDate()?.let { sdf.format(it) } ?: "Unknown"
                            // Combine the court name and date into a Pair to return
                            Pair("$courtName ($courtAddress)", date)
                        }
                    }

                    // Fetch owner fullname
                    val ownerTask = getFullNameFromUserDocument(matchDocument.getDocumentReference("owner"))

                    // Fetch list of player full names
                    val playersArray = matchDocument.get("players") as? List<DocumentReference> ?: listOf()
                    val playerTasks = playersArray.map { playerRef ->
                        getFullNameFromUserDocument(playerRef)
                    }

                    Tasks.whenAllComplete(taskCourtAndDate, ownerTask, *playerTasks.toTypedArray()).addOnCompleteListener {
                        // Court name fetched from the booking -> court reference chain
                        // Date fetched from booking reference
                        val (courtName, matchDate) = it.result[0]?.result as Pair<String, String>

                        // Owner name fetched from the owner reference
                        val ownerName = it.result[1]?.result.toString()

                        // Player names fetched from the players reference list
                        val playerNames = it.result.drop(2).mapNotNull { playerTask ->
                            (playerTask.result as? Task<*>)?.result.toString()
                        }

                        // Extract match specific fields
                        val matchType = matchDocument.getString("matchType") ?: "Unknown"
                        val matchGender = matchDocument.getString("matchGender") ?: "all"
                        val formattedDate = matchDate

                        val ownerId = matchDocument.getDocumentReference("owner")?.id ?: ""
                        val playerIds = (matchDocument.get("players") as? List<DocumentReference> ?: listOf()).map { playerRef ->
                            playerRef.id
                        }

                        // Create and add the match to the list
                        matchList.add(MyMatchesFragment.Match(id = matchDocument.id,
                            matchType = matchType,
                            matchGender = matchGender,
                            date = formattedDate,
                            ownerName = ownerName,
                            ownerId = ownerId,
                            playerNames = playerNames,
                            playerIds = playerIds,
                            courtName = courtName))

                        // Notify the adapter of the new updated list
                        recyclerView.adapter?.let { adapter ->
                            if (adapter is ActivitiesOpenMatchAdapter) {
                                adapter.updateMatches(matchList)
                            }
                            else if (adapter is MyMatchesAdapter) {
                                adapter.updateMatches(matchList)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }


    private fun getFullNameFromUserDocument(userRef: DocumentReference?): Task<String> {
        return userRef?.get()?.continueWith { userTask ->
            val userSnapshot = userTask.result
            userSnapshot?.getString("firstName").orEmpty() + " " + userSnapshot?.getString("lastName").orEmpty()
        } ?: Tasks.forResult("Unknown")
    }



}
