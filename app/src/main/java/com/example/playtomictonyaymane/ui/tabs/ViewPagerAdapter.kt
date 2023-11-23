package com.example.playtomictonyaymane.ui.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playtomictonyaymane.ui.notifications.NotificationsFragment

class ViewPagerAdapter(fragmentManager: NotificationsFragment) : FragmentStateAdapter(fragmentManager) {



    override fun getItemCount(): Int = 2 // Aantal tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActivitiesFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }


}