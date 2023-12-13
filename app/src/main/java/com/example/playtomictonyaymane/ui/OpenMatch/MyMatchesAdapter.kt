package com.example.playtomictonyaymane.ui.OpenMatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R

class MyMatchesAdapter (private val myMatchesList: List<MyMatchesFragment.Match>) :
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

    inner class MyMatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(match: MyMatchesFragment.Match) {
            titleTextView.text = match.title
            dateTextView.text = match.date

            itemView.setOnClickListener {
                // Voeg hier acties toe die moeten plaatsvinden wanneer op een match wordt geklikt
            }
        }
    }
}