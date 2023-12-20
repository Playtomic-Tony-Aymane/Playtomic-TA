package com.example.playtomictonyaymane

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.ui.OpenMatch.ActivitiesOpenMatchAdapter
import com.example.playtomictonyaymane.ui.OpenMatch.MyMatchesAdapter
import com.example.playtomictonyaymane.ui.OpenMatch.MyMatchesFragment
import com.example.playtomictonyaymane.ui.tabs.RecyclerAdapter
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MatchData
{
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
        fun getTitle(): String
        {
            var text: String = when (this.matchType) {
                "friendly" -> "Friendly "
                "competitive" -> "Competitive "
                else -> ""
            }
            text += when (this.matchGender) {
                "men" -> "♂ "
                "women" -> "♀ "
                else -> "♂♀ "
            }
            text += this.courtName

            return text
        }

        fun getDescription(): String
        {
            return "$date $ownerName"
        }
    }


    fun loadMyMatches(recyclerView: RecyclerView)
    {
        val currentUser = AuthData.auth.currentUser

        loadMatchesData(recyclerView) { matches: MutableList<Match> ->
            matches.removeIf { match: Match -> match.ownerId != currentUser?.uid }
        }
    }
    fun loadRelevantMatches(recyclerView: RecyclerView)
    {
        val currentUser = AuthData.auth.currentUser

        loadMatchesData(recyclerView) { matches: MutableList<Match> ->
            matches.removeIf { match: Match -> match.ownerId != currentUser?.uid && !match.playerIds.contains(currentUser?.uid) }
        }
    }
    fun loadMatchesData(recyclerView: RecyclerView, func: (MutableList<Match>) -> Unit = {}) {
        val matchList = mutableListOf<Match>()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = Date()

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

                        val matchDateParsed = sdf.parse(matchDate)
                        if (matchDateParsed != null && !matchDateParsed.before(currentDate))
                        {
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
                            matchList.add(Match(id = matchDocument.id,
                                matchType = matchType,
                                matchGender = matchGender,
                                date = formattedDate,
                                ownerName = ownerName,
                                ownerId = ownerId,
                                playerNames = playerNames,
                                playerIds = playerIds,
                                courtName = courtName))

                            // Run optional parameter, to e.g. filter the list
                            val lengthBefore = matchList.size
                            func(matchList)
                            val lengthAfter = matchList.size
                            Log.v("MatchData", "MatchList filtered from $lengthBefore to $lengthAfter")

                            matchList.sortBy {
                                match: Match -> match.date
                            }

                            // Notify the adapter of the new updated list
                            recyclerView.adapter?.let { adapter ->
                                if (adapter is ActivitiesOpenMatchAdapter) {
                                    adapter.updateMatches(matchList)
                                }
                                else if (adapter is MyMatchesAdapter) {
                                    adapter.updateMatches(matchList)
                                }
                                else if (adapter is RecyclerAdapter) {
                                    adapter.updateMatches(matchList)
                                }
                                else
                                {
                                    Log.e("MatchData", "Adapter is not recognized!")
                                }
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
