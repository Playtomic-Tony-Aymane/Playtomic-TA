package com.example.playtomictonyaymane.ui.signout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignOutView : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "GoPremiumPage"
    }
    val text: LiveData<String> = _text
}