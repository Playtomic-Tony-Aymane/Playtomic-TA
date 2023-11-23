package com.example.playtomictonyaymane.ui.gopremium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomictonyaymane.databinding.FragmentEditprofileBinding
import com.example.playtomictonyaymane.databinding.FragmentGopremiumBinding
import com.example.playtomictonyaymane.ui.editProfile.EditViewModel

class GoPremuimFragment: Fragment() {

    private var  _binding: FragmentGopremiumBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(GoPremuimView::class.java)

        _binding = FragmentGopremiumBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}