package com.example.playtomictonyaymane.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PasswordViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is email Fragment"
    }
    val text: LiveData<String> = _text
}