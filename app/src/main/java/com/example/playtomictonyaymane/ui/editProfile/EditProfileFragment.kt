package com.example.playtomictonyaymane.ui.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentEditprofileBinding
import com.example.playtomictonyaymane.ui.notifications.NotificationsViewModel

class EditProfileFragment: Fragment() {

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

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // The use of NavUtils may be deprecated in some scenarios in favor of using NavController
                // for Fragment-based navigation. However, to mark the overriding method as deprecated,
                // you would use the @Deprecated annotation like so:
                //@Deprecated("Use NavController for navigation instead")
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button behavior
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(false)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(com.example.playtomictonyaymane.R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.popBackStack()
        }

        val userProfileViewModel: NotificationsViewModel by activityViewModels()
        binding.buttonSaveProfile.setOnClickListener{
            userProfileViewModel.apply {
                firstName = binding.editTextFirstName.text.toString()
                lastName = binding.editTextLastName.text.toString()
                location = binding.editTextLocation.text.toString()
                prefrence = binding.editTextPrefrence.text.toString()

                saveProfile()
            }
            //findNavController().navigateUp()
            //parentFragmentManager.popBackStack()
            //findNavController().navigate(R.id.action_navigation_editProfile_to_navigation_user)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.editProfileFragment, true)
                .build()

            findNavController().navigate(R.id.navigation_user, null, navOptions)
        }

        // bindings
        binding.editTextFirstName.setText(userProfileViewModel.firstName)
        binding.editTextLastName.setText(userProfileViewModel.lastName)
        binding.editTextLocation.setText(userProfileViewModel.location)
        binding.editTextPrefrence.setText(userProfileViewModel.prefrence)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}