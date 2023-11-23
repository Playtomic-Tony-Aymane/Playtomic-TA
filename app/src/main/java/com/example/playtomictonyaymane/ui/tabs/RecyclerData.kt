package com.example.playtomictonyaymane.ui.tabs

class RecyclerData ( var office: String,
                     var device: String,
                     var img: Int,
                     var isSelected: Boolean) {
    fun setIsSelected(value: Boolean) {
        isSelected = value
    }
}