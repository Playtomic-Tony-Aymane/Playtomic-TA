package com.example.playtomictonyaymane.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomictonyaymane.AuthActivity
import com.example.playtomictonyaymane.AuthData.auth
import com.example.playtomictonyaymane.databinding.FragmentAuthPasswordBinding
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class PasswordFragment(val email: String, val exists: Boolean) : Fragment() {

    private var _binding: FragmentAuthPasswordBinding? = null
    private var TAG: String = "Auth-Password"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val authPasswordViewModel =
            ViewModelProvider(this).get(PasswordViewModel::class.java)

        _binding = FragmentAuthPasswordBinding.inflate(inflater, container, false)

        binding.buttonAuthContinue.setOnClickListener { authContinuePassword() }

        val root: View = binding.root

        val titleTextView: TextView = binding.textTitle
        val textTextView: TextView = binding.textDescription
        authPasswordViewModel.text.observe(viewLifecycleOwner) {
            if (exists)
            {
                titleTextView.text = "Welcome back!"
                textTextView.text = "Please enter your password to log in"
            }
            else
            {
                titleTextView.text = "Welcome!"
                textTextView.text = "Please enter your password to register"
            }
        }
        return root
    }
    fun authContinueHome() {
        val authActivity: AuthActivity = requireActivity() as AuthActivity
        authActivity.authContinueHome()
    }
    fun authContinuePassword() {
        val password: String = binding.editTextTextPassword.text.toString()

        if (exists)
        {
            auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success ${auth.currentUser}")
                    authContinueHome()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity,
                        "Authentication failed ${task.exception?.toString()}",
                        Toast.LENGTH_LONG,
                    ).show()

                    if (task.exception != null) {
                        when (task.exception!! as FirebaseAuthException) {
                            is FirebaseAuthInvalidCredentialsException -> binding.editTextTextPassword.error = "Password is wrong"
                            is FirebaseAuthInvalidUserException -> binding.editTextTextPassword.error = "Error during sign up" // This shouldn't happen
                            else -> binding.editTextTextPassword.error = "Error during sign up"
                        };
                    }
                }
            }
        }
        else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success ${auth.currentUser}")
                        authContinueHome()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity,
                            "Authentication failed ${task.exception?.toString()}",
                            Toast.LENGTH_LONG,
                        ).show()

                        if (task.exception != null) {
                            when (task.exception!! as FirebaseAuthException) {
                                is FirebaseAuthWeakPasswordException -> binding.editTextTextPassword.error = "Password should be 6 characters or longer"
                                else -> binding.editTextTextPassword.error = "Error during sign up"
                            };
                        }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}