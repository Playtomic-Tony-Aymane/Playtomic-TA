package com.example.playtomictonyaymane.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentUserBinding
import com.example.playtomictonyaymane.ui.editProfile.EditProfile
import com.example.playtomictonyaymane.ui.gopremium.GoPremuimFragment
import com.example.playtomictonyaymane.ui.tabs.RecyclerAdapter
import com.example.playtomictonyaymane.ui.tabs.ViewPagerAdapter
//import com.example.playtomictonyaymane.ui.tabs.ViewPagerAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NotificationsFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    val userProfileViewModel: NotificationsViewModel by activityViewModels()


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
        val goPremiumButton: MaterialButton = view.findViewById(R.id.button_prem)

        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager2)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)


        editProfileButton.setOnClickListener {
            val Editfragmet = EditProfile()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, Editfragmet)
            transaction.addToBackStack("FragmentUserBinding")
            transaction.commit()


        }

        goPremiumButton.setOnClickListener {
            val premfragmet = GoPremuimFragment()

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, premfragmet)
            transaction.addToBackStack("FragmentUserBinding")
            transaction.commit()

        }

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Activities"
                else -> null
            }
        }.attach()





        binding.userName.text = "${userProfileViewModel.firstName} ${userProfileViewModel.lastName}"
        binding.userLocation.text = userProfileViewModel.location
        binding.prefrence.text = userProfileViewModel.prefrence
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}