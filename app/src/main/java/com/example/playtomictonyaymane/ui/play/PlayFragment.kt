package com.example.playtomictonyaymane.ui.play
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentPlayBinding
import com.example.playtomictonyaymane.ui.OpenMatch.OpenMatchesFragment
import com.example.playtomictonyaymane.ui.court.BookingCourtFragment
import com.example.playtomictonyaymane.ui.dashboard.DashboardFragment
import com.example.playtomictonyaymane.ui.notifications.NotificationsFragment
import com.example.playtomictonyaymane.ui.tabs.ActivitiesFragment

class PlayFragment: Fragment() {
    private var _binding: FragmentPlayBinding? = null

    // This property is only valid between onCreateView and
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

        binding.buttonAddCourt.setOnClickListener {

            val addCourt = BookingCourtFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, addCourt)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.buttonAddmatch.setOnClickListener {

            val addOpenMatch = OpenMatchesFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, addOpenMatch)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
