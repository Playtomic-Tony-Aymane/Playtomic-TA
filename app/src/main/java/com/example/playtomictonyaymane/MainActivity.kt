package com.example.playtomictonyaymane

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.playtomictonyaymane.databinding.ActivityMainBinding
import com.example.playtomictonyaymane.ui.editProfile.EditProfileFragment
import com.example.playtomictonyaymane.ui.notifications.NotificationsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var firstRun: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (firstRun) {
            // TESTING
            //AuthData.auth.signOut()
            firstRun = false;
        }

        val currentUser = AuthData.auth.currentUser
        if(currentUser == null){
            Log.v("Auth", "User not logged in, redirecting to auth")
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
            finish()
        }
        Log.v("Auth", "User already logged in as $currentUser")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
             R.id.navigation_play,R.id.navigation_user, R.id.navigation_discovery
            )
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val userProfileViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        userProfileViewModel.loadProfile()

        // Check whether to load EditProfileFragment or not
        if (intent.getBooleanExtra("LOAD_PROFILE_FRAGMENT", false)) {
            Handler(Looper.getMainLooper()).post {
                // Commit a Fragment transaction to load EditProfileFragment
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, EditProfileFragment())
//                    .commit()
                navController.navigate(R.id.editProfileFragment)
            }
        }
    }

    fun signOut() {
        Log.v("Auth", "User has logged out, returning to auth")
        AuthData.auth.signOut()
        startActivity(Intent(this@MainActivity, AuthActivity::class.java))
        finish()
    }
}