package com.example.playtomictonyaymane.ui.OpenMatch

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.AuthData
import com.example.playtomictonyaymane.R
import com.google.firebase.firestore.DocumentReference

interface MatchUpdateListener {
    fun onMatchUpdated(matchId: String)
}
class ActivitiesOpenMatchAdapter (private var matchesList: List<MyMatchesFragment.Match>, private val listener: MatchUpdateListener) : RecyclerView.Adapter<ActivitiesOpenMatchAdapter.MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activities_card, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matchesList[position]
        holder.bind(match)
    }

    override fun getItemCount(): Int {
        return matchesList.size
    }

    fun updateMatches(newMatches: List<MyMatchesFragment.Match>) {
        this.matchesList = newMatches.sortedBy { match -> match.date }
        notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
    }

    inner class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        //private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val fieldNameTextView: TextView = itemView.findViewById(R.id.fieldNameTextView)
        private val matchDetailsView: TextView = itemView.findViewById(R.id.matchDetailsView)
        private val addToMatchButton = itemView.findViewById<Button>(R.id.addToMatchButton)
        // Hier alle andere views in de kaart toevoegen

        @SuppressLint("NotifyDataSetChanged")
        fun bind(match: MyMatchesFragment.Match) {
            dateTextView.text = "Datum: ${match.date}"
            fieldNameTextView.text = "Veld: ${match.courtName}"
            var text: String = ""
            val matchType: String = when (match.matchType) {
                "friendly" -> "Friendly"
                "competitive" -> "Competitive"
                else -> ""
            }
            text += "$matchType "
            val matchGender: String = when (match.matchGender) {
                "men" -> "♂"
                "women" -> "♀"
                else -> "♂♀"
            }
            text += "$matchGender "
            text += "${match.playerNames.size}/${match.getMaxPlayers()}. "
            text += "Organisator: ${match.ownerName}"
            matchDetailsView.text = text

            addToMatchButton.setOnClickListener {
                Log.v("Matches", "Clicked on ${matchDetailsView.text} ${dateTextView.text} at ${fieldNameTextView.text}")
                showConfirmationDialog(match, matchType, matchGender) { this@ActivitiesOpenMatchAdapter.notifyDataSetChanged() }
            }

            addToMatchButton.isEnabled = true
            val userId: String? = AuthData.auth.currentUser?.uid
            if (userId != null) {
                if (match.ownerId == userId) {
                    addToMatchButton.isEnabled = false
                }
                else if (match.playerIds.contains(userId))
                {
                    addToMatchButton.isEnabled = false
                }
                else if (match.isFull())
                {
                    addToMatchButton.isEnabled = false
                }
            }
        }

        private fun showConfirmationDialog(match: MyMatchesFragment.Match, matchType: String, matchGender: String, notifyDataSetChanged: () -> Unit) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Participate in ${match.matchType} padel for ")
            builder.setPositiveButton("Yes") { dialog, _ ->
                // Retrieve the current user's document reference
                val currentUserRef = AuthData.auth.currentUser?.let { user ->
                    AuthData.db.collection("users").document(user.uid)
                }

                // Firestore transaction to add the current user to the 'players' list of the match
                currentUserRef?.let { userRef ->
                    AuthData.db.runTransaction { transaction ->
                        // Get the match document reference
                        val matchRef = AuthData.db.collection("matches").document(match.id)
                        // Retrieve the match document within the transaction
                        val matchDocumentSnapshot = transaction.get(matchRef)
                        // Get the current list of player references
                        val currentPlayersList = matchDocumentSnapshot.get("players") as? List<DocumentReference> ?: listOf()

                        // Check if user is not already in the list and add them
                        if (userRef !in currentPlayersList) {
                            val updatedPlayersList = currentPlayersList + userRef
                            transaction.update(matchRef, "players", updatedPlayersList)
                        }
                    }.addOnSuccessListener {
                        Log.d("MatchViewHolder", "User added to match successfully")
                        // Notify the adapter to refresh this item, if needed
                        listener.onMatchUpdated(match.id)
                    }.addOnFailureListener { e ->
                        Log.w("MatchViewHolder", "Transaction failed: ", e)
                    }
                }

                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}