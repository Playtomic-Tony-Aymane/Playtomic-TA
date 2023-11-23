package com.example.playtomictonyaymane.ui.play
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.playtomictonyaymane.databinding.FragmentPlayBinding

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

        // Hier kun je de logica voor de knoppen en andere interacties toevoegen
        binding.addMatchButton.setOnClickListener {
            // Voeg hier de logica toe voor het toevoegen van een match
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
