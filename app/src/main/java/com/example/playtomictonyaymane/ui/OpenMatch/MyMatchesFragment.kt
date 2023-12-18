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
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Locale

class MyMatchesFragment : Fragment() {

    data class Match(
        val id: String,
        val matchType: String,
        val matchGender: String,
        val date: String,
        val courtName: String,
        val ownerName: String,
        val ownerId: String,
        val playerNames: List<String>,
        val playerIds: List<String>
    ) {
        fun getMaxPlayers(): Int {
            return 4
        }
        fun isFull(): Boolean {
            return playerNames.size >= getMaxPlayers()
        }
    }

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
        loadUserMatches(recyclerViewMyMatches)

        return view
    }

    private fun loadUserMatches(recyclerView: RecyclerView) {
        val currentUser = AuthData.auth.currentUser
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
                                adapter.updateMatches(matchList.filter { match -> match.ownerId == currentUser?.uid })
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