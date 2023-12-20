package com.example.playtomictonyaymane.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomictonyaymane.AuthActivity
import com.example.playtomictonyaymane.AuthData
import com.example.playtomictonyaymane.databinding.FragmentAuthEmailBinding

class EmailFragment : Fragment() {

    private var _binding: FragmentAuthEmailBinding? = null
    private var TAG: String = "Auth-Email"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val authEmailViewModel =
            ViewModelProvider(this).get(EmailViewModel::class.java)

        _binding = FragmentAuthEmailBinding.inflate(inflater, container, false)

        binding.buttonAuthContinue.setOnClickListener { authContinueEmail() }

        val root: View = binding.root

//        val textView: TextView = binding.textEmail
//        authEmailViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    fun authContinueEmail() {
        var email : String = binding.editTextTextEmailAddress.text.toString()

        // check if email is valid
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(activity, "Email is not valid!", Toast.LENGTH_SHORT).show()
            binding.editTextTextEmailAddress.error = "Please enter a valid email"
            return
        }

        // check if email exists in firebase
        authCheckIfEmailExists(email)
    }
    fun authContinuePassword(email: String, exists: Boolean) {
        val authActivity: AuthActivity = requireActivity() as AuthActivity
        authActivity.authContinuePassword(email, exists)
    }
    fun authCheckIfEmailExists(email: String) {
        AuthData.auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                val list = task.result.signInMethods
                if (list == null) {
                    Log.v(TAG, "checkIfEmailExists:success null")

                    authContinuePassword(email, false)
                    return@addOnCompleteListener
                }
                val exists = list.isNotEmpty()
                Log.v(TAG, "checkIfEmailExists:success $exists")

                authContinuePassword(email, exists)
                return@addOnCompleteListener
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "checkIfEmailExists:failure", task.exception)
                Toast.makeText(
                    activity,
                    "Authentication failed on email step",
                    Toast.LENGTH_SHORT,
                ).show()

                return@addOnCompleteListener
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}