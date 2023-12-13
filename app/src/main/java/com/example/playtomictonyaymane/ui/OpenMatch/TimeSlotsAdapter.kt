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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R

class TimeSlotsAdapter(private val timeSlots: List<String>) :
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
            showConfirmationDialog(holder.itemView.context, currentTimeSlot)
        }
    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }

    private fun showConfirmationDialog(context: Context, selectedTimeSlot: String){
        var builder = AlertDialog.Builder(context)
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

            val dialog = AlertDialog.Builder(context).setView(dialogView).create()
            dialog.show()
            buttonStartMatch.setOnClickListener {
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // Voer acties uit wanneer 'No' wordt gekozen
            // Bijvoorbeeld: sluit dialoogvenster zonder match te starten
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
