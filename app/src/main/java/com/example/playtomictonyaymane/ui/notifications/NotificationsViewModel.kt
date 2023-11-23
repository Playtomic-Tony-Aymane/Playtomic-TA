package com.example.playtomictonyaymane.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Profile"
    }
    val text: LiveData<String> = _text
    var firstName: String = ""
    var lastName: String = ""
    var location : String = ""
    var prefrence: String = ""

}