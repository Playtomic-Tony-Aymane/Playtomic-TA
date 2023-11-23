package com.example.playtomictonyaymane.ui.editProfile

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.playtomictonyaymane.databinding.FragmentEditprofileBinding


class EditProfileFragment: Fragment() {
    private var  _binding: FragmentEditprofileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
            // the button doesn't work yet
            //setDisplayHomeAsUpEnabled(true)


        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(com.example.playtomictonyaymane.R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            //navController.navigate(com.example.playtomictonyaymane.R.id.action_navigation_user_to_editProfileFragment)
            navController.popBackStack()
        }

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            //requireActivity().supportFragmentManager.popBackStackImmediate("UserProfile", 0)
//            //requireActivity().supportFragmentManager.popBackStackImmediate()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}