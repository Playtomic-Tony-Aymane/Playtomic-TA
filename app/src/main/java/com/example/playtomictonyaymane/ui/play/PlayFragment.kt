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
import com.example.playtomictonyaymane.ui.dashboard.DashboardFragment
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

    fun saveDataToList(
        playerName: String,
        gameCode: String,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int
    ) {
        // Hier sla je de ontvangen gegevens op in een lijst, database, of waar je ze ook wilt bewaren
        // Bijvoorbeeld:
        val data = "$playerName - $gameCode - $year/$month/$day $hour:$minute"
        // Voeg de data toe aan de lijst in je fragment
        // data kan ook naar een adapter worden gestuurd om weer te geven in een RecyclerView

        // Update de UI of voer andere acties uit met de ontvangen gegevens
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Zoek de button op basis van de ID
        val buttonStartGame = view.findViewById<Button>(R.id.buttonStartGame)
        val editTextPlayerName = view.findViewById<EditText>(R.id.editTextPlayerName)
        val editTextGameCode = view.findViewById<EditText>(R.id.editTextGameCode)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)

        // Voeg een klikgebeurtenis toe aan de knop
        buttonStartGame.setOnClickListener {
            val playerName = editTextPlayerName.text.toString()
            val gameCode = editTextGameCode.text.toString()
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth

            val hour = timePicker.hour
            val minute = timePicker.minute

            val db = DashboardFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            // Vervang het huidige fragment door PlayFragment
            transaction.replace(R.id.container, db)
            transaction.addToBackStack(null)
            transaction.commit()

        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
