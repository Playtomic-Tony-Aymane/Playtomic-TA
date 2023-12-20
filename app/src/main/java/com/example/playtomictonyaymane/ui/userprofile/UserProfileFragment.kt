package com.example.playtomictonyaymane.ui.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.playtomictonyaymane.MainActivity
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentUserBinding
import com.example.playtomictonyaymane.ui.tabs.ViewPagerAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val editProfileButton: MaterialButton = view.findViewById(R.id.button_edit)
        val signoutButton: MaterialButton = view.findViewById(R.id.button_signout)

        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager2)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)


        editProfileButton.setOnClickListener {
<<<<<<< HEAD:app/src/main/java/com/example/playtomictonyaymane/ui/userprofile/UserProfileFragment.kt
            findNavController().navigate(R.id.editProfileFragment)
=======
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_navigation_user_to_editProfileFragment)
>>>>>>> master:app/src/main/java/com/example/playtomictonyaymane/ui/notifications/NotificationsFragment.kt
        }

        signoutButton.setOnClickListener {
            val activity: MainActivity = requireActivity() as MainActivity
            activity.signOut()
        }

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Activities"
                else -> null
            }
        }.attach()


        val userProfileViewModel = activityViewModels<NotificationsViewModel>().value
        // Observe LiveData fields and update UI accordingly
        userProfileViewModel.firstName.observe(viewLifecycleOwner) { firstName ->
            binding.userName.text = "$firstName ${userProfileViewModel.lastName.value}"
        }
        userProfileViewModel.lastName.observe(viewLifecycleOwner) { lastName ->
            binding.userName.text = "${userProfileViewModel.firstName.value} $lastName"
        }
        userProfileViewModel.location.observe(viewLifecycleOwner) { location ->
            binding.userLocation.text = location
        }
        userProfileViewModel.preference.observe(viewLifecycleOwner) { preference ->
            binding.prefrence.text = preference
        }
        // Load the profile which will update LiveData and trigger UI changes
        userProfileViewModel.loadProfile()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}