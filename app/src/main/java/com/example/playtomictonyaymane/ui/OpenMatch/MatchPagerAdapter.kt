package com.example.playtomictonyaymane.ui.OpenMatch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter



class MatchPagerAdapter (fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ActivitiesOpenMatchesFragment()
            1 -> MyMatchesFragment()
            else -> ActivitiesOpenMatchesFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Available" // Titel voor het eerste tabblad
            1 -> "My Matches" // Titel voor het tweede tabblad
            else -> ""
        }
    }
}