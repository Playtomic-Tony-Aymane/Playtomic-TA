package com.example.playtomictonyaymane.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playtomictonyaymane.AuthData

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Profile"
    }
    val text: LiveData<String> = _text
    var firstName: String = ""
    var lastName: String = ""
    var location : String = ""
    var prefrence: String = ""

    var userData: HashMap<String, String?> = hashMapOf()
        set(value) {
            field = value
            // Automatically update fields each time userData is updated
            syncFromUserData()
        }

    // Set fields using userData
    fun syncFromUserData() {
        firstName = userData["firstName"] ?: ""
        lastName = userData["lastName"] ?: ""
        location = userData["location"] ?: ""
        prefrence = userData["prefrence"] ?: ""
    }
    // Sync fields
    fun syncFromFields() {
        userData["firstName"] = firstName
        userData["lastName"] = lastName
        userData["location"] = location
        userData["prefrence"] = prefrence
    }

    fun saveProfile() {
        syncFromFields()
        AuthData.saveToFireStore(userData)
    }

    fun loadProfile(userDataValid: ((Boolean) -> Unit)? = null) {
        AuthData.loadFromFireStore({ data ->
            this.userData = data
            val valid = userData.values.none { it.isNullOrBlank() }
            userDataValid?.invoke(valid)
        }, {
            // Handle failure
            userDataValid?.invoke(false)
        })
    }
}