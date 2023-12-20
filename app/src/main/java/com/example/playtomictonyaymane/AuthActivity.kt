package com.example.playtomictonyaymane

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.playtomictonyaymane.ui.auth.EmailFragment
import com.example.playtomictonyaymane.ui.auth.PasswordFragment

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

        AuthData.init()
        val currentUser = AuthData.auth.currentUser
        if(currentUser != null){
            Log.v("Auth-Activity", "User already logged in, trying to redirect")
            authContinueHome()
            return
        }
    }

    fun authContinuePassword(email: String, exists: Boolean) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val passwordFragment = PasswordFragment(email, exists)
            replace(R.id.activity_auth_fragment_container_view, passwordFragment)
        }
    }

    private fun authContinueHomeRedirect(isUserDataValid: Boolean, data: HashMap<String, String?>? = null)
    {
        Log.v("Auth-Activity", "User correctly authenticated, redirecting to home")
        val intent = Intent(this, MainActivity::class.java).apply {
            // Add an extra field to indicate whether to load EditProfileFragment or not
            putExtra("LOAD_PROFILE_FRAGMENT", !isUserDataValid)
            putExtra("LOAD_PROFILE_DATA", data)
        }
        startActivity(intent)
        finish()
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

        // check that data is valid
        AuthData.loadFromFireStore(success = { data ->
            if (AuthData.isUserDataValid(data))
            {
                Log.v("Auth-Activity", "User data is valid")
                authContinueHomeRedirect(true, data)
            }
            else
            {
                Log.v("Auth-Activity", "User data is invalid! Redirecting to Edit Profile")
                authContinueHomeRedirect(false)
            }
        }, failure = {
            Log.v("Auth-Activity", "User data has failure! Does not exist? Redirecting to Edit Profile")
            authContinueHomeRedirect(false)
        })
    }
}