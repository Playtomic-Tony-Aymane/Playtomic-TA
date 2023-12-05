package com.example.playtomictonyaymane.ui.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R

class TimeSlotAdapter(private val timeSlots: List<String>, private val listener: (String) -> Unit) :
    RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_slote, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = timeSlots[position]
        holder.bind(timeSlot)
    }

    override fun getItemCount(): Int = timeSlots.size

    inner class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeSlotText: TextView = itemView.findViewById(R.id.timeSlotText)

        fun bind(timeSlot: String) {
            timeSlotText.text = timeSlot
            itemView.setOnClickListener { listener(timeSlot) }
        }
    }
}
