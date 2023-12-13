package com.example.playtomictonyaymane.ui.OpenMatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R

class ActivitiesOpenMatchAdapter (private val matchesList: List<ActivitiesOpenMatchesFragment.Match>) : RecyclerView.Adapter<ActivitiesOpenMatchAdapter.MatchViewHolder>() {

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

    inner class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val fieldNameTextView: TextView = itemView.findViewById(R.id.fieldNameTextView)
        // Hier alle andere views in de kaart toevoegen

        fun bind(match: ActivitiesOpenMatchesFragment.Match) {
            dateTextView.text = "Datum: ${match.date}"
            timeTextView.text = "Tijd: ${match.time}"
            fieldNameTextView.text = "Veld: ${match.fieldName}"
            // Bind andere matchgegevens aan de bijbehorende views in de kaart
        }
    }
}