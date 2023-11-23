package com.example.playtomictonyaymane.ui.editProfile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentEditprofileBinding
import com.example.playtomictonyaymane.ui.notifications.NotificationsViewModel

class EditProfile: Fragment() {

    private lateinit var userProfileViewModel: NotificationsViewModel
    private var  _binding: FragmentEditprofileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(EditViewModel::class.java)

        _binding = FragmentEditprofileBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true)

            val userProfileViewModel: NotificationsViewModel by activityViewModels()

            binding.buttonSaveProfile.setOnClickListener{
                userProfileViewModel.apply {
                     firstName = binding.editTextFirstName.text.toString()
                     lastName = binding.editTextLastName.text.toString()
                     location = binding.editTextLocation.text.toString()
                     prefrence = binding.editTextPrefrence.text.toString()
                }
                findNavController().navigateUp()
            }





        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
           requireActivity().supportFragmentManager.popBackStackImmediate("FragmentUserBinding",0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}