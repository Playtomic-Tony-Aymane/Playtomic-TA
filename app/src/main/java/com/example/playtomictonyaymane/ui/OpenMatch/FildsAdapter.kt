package com.example.playtomictonyaymane.ui.OpenMatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R



class FieldsAdapter(private val clubsList: List<AddMatchesFragment.ClubAvailability>) :
    RecyclerView.Adapter<FieldsAdapter.ClubsViewHolder>() {

inner class ClubsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val clubName: TextView = itemView.findViewById(R.id.textClubName)
    val day: TextView = itemView.findViewById(R.id.textDay)
    val recyclerViewTimeSlots: RecyclerView = itemView.findViewById(R.id.recyclerViewTimeSlots) // Referentie toevoegen voor tijdsslots RecyclerView

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.open_match_cardview, parent, false)
        return ClubsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClubsViewHolder, position: Int) {
        val currentClub = clubsList[position]

        holder.clubName.text = currentClub.clubName
        holder.day.text = currentClub.day

        // Creëer een adapter voor de tijdsslots
        // List here!
        val timeSlotsAdapter = TimeSlotsAdapter(currentClub.startTimes, currentClub.bookingId)

        // Configureer de RecyclerView voor tijdsslots
        holder.recyclerViewTimeSlots.apply {
            layoutManager = GridLayoutManager(holder.itemView.context,4)
            adapter = timeSlotsAdapter
        }
    }




    override fun getItemCount(): Int {
        return clubsList.size
    }
}

