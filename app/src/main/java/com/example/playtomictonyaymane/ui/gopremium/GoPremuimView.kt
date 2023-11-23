package com.example.playtomictonyaymane.ui.gopremium

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GoPremuimView : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "GoPremiumPage"
    }
    val text: LiveData<String> = _text
}