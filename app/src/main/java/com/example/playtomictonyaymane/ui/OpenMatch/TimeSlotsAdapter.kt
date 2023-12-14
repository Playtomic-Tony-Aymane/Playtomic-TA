package com.example.playtomictonyaymane.ui.OpenMatch

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.AuthData
import com.example.playtomictonyaymane.R

class TimeSlotsAdapter(private val timeSlots: List<String>, private val bookingId: String) :
    RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotViewHolder>() {

    inner class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeSlotText: TextView = itemView.findViewById(R.id.timeSlotText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_slote, parent, false)
        return TimeSlotViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val currentTimeSlot = timeSlots[position]
        holder.timeSlotText.text = currentTimeSlot

        holder.itemView.setOnClickListener {
            // Toon een dialoogvenster wanneer een tijdslot wordt aangeklikt
            showConfirmationDialog(holder.itemView.context, currentTimeSlot, bookingId)
        }
    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }

    private fun showConfirmationDialog(context: Context, selectedTimeSlot: String, bookingId: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Start match at $selectedTimeSlot for 90 minutes?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.configure_match_popup, null) as LinearLayout

            val checkboxCompetitive = dialogView.findViewById<CheckBox>(R.id.checkboxCompetitive)
            val checkboxFriendly = dialogView.findViewById<CheckBox>(R.id.checkboxFriendly)
            val checkboxAllPlayers = dialogView.findViewById<CheckBox>(R.id.checkboxAllPlayers)
            val checkboxOnlyMen = dialogView.findViewById<CheckBox>(R.id.checkboxOnlyMen)
            val checkboxOnlyWomen = dialogView.findViewById<CheckBox>(R.id.checkboxOnlyWomen)
            val buttonStartMatch = dialogView.findViewById<Button>(R.id.buttonStartMatch)

            val secondDialog = AlertDialog.Builder(context).setView(dialogView).create()
            secondDialog.show()

            buttonStartMatch.setOnClickListener {
                // Determine the match type and gender from the checkboxes
                val matchType = when {
                    checkboxCompetitive.isChecked -> "competitive"
                    checkboxFriendly.isChecked -> "friendly"
                    else -> "friendly" // default to friendly if none selected
                }
                val matchGender = when {
                    checkboxAllPlayers.isChecked -> "all"
                    checkboxOnlyMen.isChecked -> "men"
                    checkboxOnlyWomen.isChecked -> "women"
                    else -> "all" // default to all if none selected
                }

                val currentUserRef = AuthData.auth.currentUser?.let { user ->
                    AuthData.db.collection("users").document(user.uid)
                }

                // Create a reference to the booking document
                val bookingRef = AuthData.db.collection("bookings").document(bookingId)

                // Define the match data to be saved into Firestore
                val matchData = hashMapOf(
                    "matchType" to matchType,
                    "matchGender" to matchGender,
                    "owner" to currentUserRef,
                    "players" to listOf(currentUserRef), // Assuming the current user starts as the only player
                    "booking" to bookingRef // Add the reference to the associated booking
                    // Add other match details like date/time if applicable
                )

                // Save the match to Firestore
                currentUserRef?.let {
                    AuthData.db.collection("matches").add(matchData).addOnSuccessListener {
                        Toast.makeText(context, "Match saved successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to save match: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                secondDialog.dismiss()
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
