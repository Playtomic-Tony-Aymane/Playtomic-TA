package com.example.playtomictonyaymane.ui.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "editpage"
    }
    val text: LiveData<String> = _text
}