package com.example.playtomictonyaymane.ui.play
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentPlayBinding

class PlayFragment: Fragment() {
    private var _binding: FragmentPlayBinding? = null

    // This property is only valid between onCreateView andfèi
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val playViewModel =
            ViewModelProvider(this).get(PlayViewModel::class.java)

        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding.buttonAddCourt.setOnClickListener {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.bookingCourtFragment)

        }

        binding.buttonAddmatch.setOnClickListener {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.openMatchesFragment)

        }


    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
