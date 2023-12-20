package com.example.playtomictonyaymane.ui.OpenMatch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.MatchData
import com.example.playtomictonyaymane.R

class MyMatchesAdapter (private var myMatchesList: List<MatchData.Match>) :
    RecyclerView.Adapter<MyMatchesAdapter.MyMatchesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMatchesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exisiting_match, parent, false)
        return MyMatchesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyMatchesViewHolder, position: Int) {
        val match = myMatchesList[position]
        holder.bind(match)
    }

    override fun getItemCount(): Int {
        return myMatchesList.size
    }

    fun updateMatches(newMatches: List<MatchData.Match>) {
        this.myMatchesList = newMatches
        notifyDataSetChanged() // Notify the adapter to refresh the RecyclerView
    }

    inner class MyMatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(match: MatchData.Match) {
            val text = match.getTitle()

            titleTextView.text = text
            dateTextView.text = match.date

            itemView.setOnClickListener {
                // Voeg hier acties toe die moeten plaatsvinden wanneer op een match wordt geklikt
                Log.v("Matches", "Clicked on ${titleTextView.text} ${dateTextView.text}")
            }
        }
    }
}