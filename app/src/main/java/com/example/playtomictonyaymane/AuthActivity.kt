package com.example.playtomictonyaymane

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.playtomictonyaymane.ui.auth.EmailFragment
import com.example.playtomictonyaymane.ui.auth.PasswordFragment
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_auth)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<EmailFragment>(R.id.activity_auth_fragment_container_view)
            }
        }
    }

    fun authContinuePassword(email: String, exists: Boolean) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val passwordFragment = PasswordFragment(email, exists)
            replace(R.id.activity_auth_fragment_container_view, passwordFragment)
        }
    }

    fun authContinueHome()
    {
        // check that user is logged in
        val currentUser = AuthData.auth.currentUser
        if(currentUser == null){
            Log.v("Auth-Activity", "User not logged in, staying on page")
            Toast.makeText(
                this,
                "Unknown error trying to log in, please try again later",
                Toast.LENGTH_SHORT,
            ).show()
            return
        }
        Log.v("Auth-Activity", "User correctly authenticated, redirecting to home")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}