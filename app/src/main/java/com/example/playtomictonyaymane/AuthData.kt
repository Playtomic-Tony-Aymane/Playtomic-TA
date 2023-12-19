package com.example.playtomictonyaymane

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

object AuthData
{
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    fun init() {
        AuthData.db = FirebaseFirestore.getInstance()
        AuthData.auth = Firebase.auth
    }
    fun saveToFireStore(userMap: HashMap<String, String?>) {
        val userMapString = userMap.entries.joinToString { "${it.key}=${it.value}" }
        Log.v("Auth-DB", "Saving to DB: $userMapString")
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .set(userMap)
    }

    fun loadFromFireStore(success: (HashMap<String, String?>) -> Unit, failure: () -> Unit) {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    success(document.data as HashMap<String, String?>)
                } else {
                    failure()
                }
            }
            .addOnFailureListener { e ->
                failure()
            }
    }

    fun isUserDataValid(userData: HashMap<String, String?>): Boolean {
        return listOf("firstName", "lastName", "location", "prefrence").all { key ->
            val dataSnapshotValue = userData[key]
            dataSnapshotValue != null && dataSnapshotValue.isNotBlank()
        }
    }
}
