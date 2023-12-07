package com.example.playtomictonyaymane.ui.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R


class TimeSlotAdapter(private var timeSlots: List<String>, private var isLoading: Boolean, private val clickListener: (TimeSlotAdapter, String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewTypeLoading = 0
    private val viewTypeItem = 1
    private var selectedItem : String = ""

    override fun getItemCount(): Int = if (isLoading) 1 else timeSlots.size

    override fun getItemViewType(position: Int): Int = if (isLoading) viewTypeLoading else viewTypeItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewTypeLoading) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            LoadingViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.time_slot_item, parent, false)
            TimeSlotViewHolder(view, clickListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is TimeSlotViewHolder -> holder.bind(this, timeSlots[position], !isLoading)
            // No binding necessary for the LoadingViewHolder
        }
    }

    fun getSelectedTimeSlot(): String {
        return selectedItem
    }
    fun updateTimeslots(newTimeSlots: List<String>, loading: Boolean) {
//        if (!newTimeSlots.contains(selectedItem) && newTimeSlots.isNotEmpty()) {
//            selectedItem = newTimeSlots[0]
//        }
        timeSlots = newTimeSlots
        isLoading = loading
        notifyDataSetChanged()
    }

    // ViewHolder for the loading item
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // ViewHolder for the actual time slots
    class TimeSlotViewHolder(itemView: View, val clickListener: (TimeSlotAdapter, String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val buttonTimeSlot: Button = itemView.findViewById(R.id.time_slot_button)

        fun bind(timeSlotAdapter: TimeSlotAdapter, timeSlot: String, isEnabled: Boolean) {
            buttonTimeSlot.text = timeSlot
            buttonTimeSlot.isEnabled = isEnabled
            if(isEnabled) {
                buttonTimeSlot.setOnClickListener {
                    timeSlotAdapter.selectedItem = timeSlot
                    clickListener(timeSlotAdapter, timeSlot)
                }
            } else {
                buttonTimeSlot.setOnClickListener(null)
            }
        }
    }
}