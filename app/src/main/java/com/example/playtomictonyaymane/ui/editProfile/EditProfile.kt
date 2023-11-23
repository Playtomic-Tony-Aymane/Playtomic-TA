package com.example.playtomictonyaymane.ui.editProfile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomictonyaymane.databinding.FragmentDiscoveryBinding
import com.example.playtomictonyaymane.databinding.FragmentEditprofileBinding
import com.example.playtomictonyaymane.databinding.FragmentGopremiumBinding
import com.example.playtomictonyaymane.ui.dashboard.DashboardViewModel

class EditProfile: Fragment() {
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
            setDisplayHomeAsUpEnabled(true)


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