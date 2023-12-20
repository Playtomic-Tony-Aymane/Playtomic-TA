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

    // Convert fields into LiveData so that the UI can observe changes
    private val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String> = _firstName
    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> = _lastName
    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location
    private val _preference = MutableLiveData<String>()
    val preference: LiveData<String> = _preference

    var userData: HashMap<String, String?> = hashMapOf()
        set(value) {
            field = value
            // Automatically update fields each time userData is updated
            syncFromUserData()
        }

    // Set fields using userData
    fun syncFromUserData() {
        _firstName.value = userData["firstName"] ?: ""
        _lastName.value = userData["lastName"] ?: ""
        _location.value = userData["location"] ?: ""
        _preference.value = userData["preference"] ?: ""
    }
    // Sync fields
    fun syncFromFields() {
        userData["firstName"] = firstName.value
        userData["lastName"] = lastName.value
        userData["location"] = location.value
        userData["prefrence"] = preference.value
    }

    fun saveProfile() {
        syncFromFields()
        AuthData.saveToFireStore(userData)
    }

    fun loadProfile(userDataValid: ((Boolean) -> Unit)? = null) {
        AuthData.loadFromFireStore({ data ->
            this.userData = data
            syncFromUserData()
            val valid = userData.values.none { it.isNullOrBlank() }
            userDataValid?.invoke(valid)
        }, {
            // Handle failure
            userDataValid?.invoke(false)
        })
    }
}