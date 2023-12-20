package com.example.playtomictonyaymane.ui.tabs

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.MatchData
import com.example.playtomictonyaymane.R

class RecyclerAdapter(private var matchesList: List<MatchData.Match>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(matchesList[position])
    }

    override fun getItemCount(): Int {
        return matchesList.size
    }

    fun updateMatches(newMatches: List<MatchData.Match>) {
        matchesList = newMatches
        notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val codeTextView: TextView = itemView.findViewById(R.id.kodePertanyaan)
        private val categoryTextView: TextView = itemView.findViewById(R.id.kategori)
        private val contentTextView: TextView = itemView.findViewById(R.id.isiPertanyaan)

        fun bind(match: MatchData.Match) {
            codeTextView.text = match.getTitle()
            categoryTextView.text = match.date
            contentTextView.text = match.getDescription()

            // Implement click listener if required
            itemView.setOnClickListener {
                // Actions to take place when a match is clicked
            }
        }
    }
}

