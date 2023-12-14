package com.example.playtomictonyaymane.ui.court

//import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.AuthData
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentCourtbookingBinding
import com.example.playtomictonyaymane.ui.dashboard.DashboardFragment
import com.example.playtomictonyaymane.ui.notifications.NotificationsFragment
import com.example.playtomictonyaymane.ui.tabs.TimeSlotAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class BookingCourtFragment:Fragment() {
    private var _binding: FragmentCourtbookingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCourtbookingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Zoek de button op basis van de ID
        val buttonStartGame = view.findViewById<Button>(R.id.buttonStartGame)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val recyclerViewTimeSlots = view.findViewById<RecyclerView>(com.example.playtomictonyaymane.R.id.recyclerViewTimeSlots)

        datePicker.init(
            datePicker.year, datePicker.month, datePicker.dayOfMonth
        ) { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            loadAvailableTimeSlots(view, calendar.time)
        }

        val calendarToday = Calendar.getInstance()
        // Set Time to 0 to avoid timezone issues
        calendarToday.set(Calendar.HOUR_OF_DAY, 0)
        calendarToday.set(Calendar.MINUTE, 0)
        calendarToday.set(Calendar.SECOND, 0)
        calendarToday.set(Calendar.MILLISECOND, 0)
        datePicker.minDate = calendarToday.timeInMillis

        val spinnerCourtList = view.findViewById<Spinner>(R.id.spinnerCourtList)
        fetchCourtsAndSetupSpinner(spinnerCourtList)

        val adapterTimeSlot = TimeSlotAdapter(emptyList(), true) { timeSlotAdapter: TimeSlotAdapter, selectedTimeSlot: String ->

        }
        recyclerViewTimeSlots.layoutManager = GridLayoutManager(requireContext(), 5)
        recyclerViewTimeSlots.adapter = adapterTimeSlot

//        val timeSlots = generateTimeSlots()
//
//        val adapterTimeSlot = TimeSlotAdapter(timeSlots) { selectedTimeSlot ->
//            // Voer acties uit met de geselecteerde tijdslot
//        }

//        recyclerViewTimeSlots.adapter = adapterTimeSlot

        // Load the time slots for the currently selected date on start
        val calendar = Calendar.getInstance()
        calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
        loadAvailableTimeSlots(view, calendar.time)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(com.example.playtomictonyaymane.R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.popBackStack()
        }

        // Voeg een klikgebeurtenis toe aan de knop
        buttonStartGame.setOnClickListener {
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth
            val selectedTimeSlot = adapterTimeSlot.getSelectedTimeSlot()
            if (selectedTimeSlot == null || selectedTimeSlot == "") {
                Toast.makeText(context, "Please select a time slot", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedCourt = spinnerCourtList.selectedItem as? CourtItem
            if (selectedCourt == null) {
                Toast.makeText(context, "Please select a court", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            buttonStartGame.isEnabled = false

            val selectedCourtRef = AuthData.db.collection("courts").document(selectedCourt.documentId)
            saveBookingToFirestore(year, month, day, selectedTimeSlot, selectedCourtRef) { success: Boolean ->
                if (success) {
                    findNavController().navigateUp()
                    val dashboard = DashboardFragment()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()

                    // Vervang het huidige fragment door PlayFragment
//                    transaction.replace(R.id.container, dashboard)
//                    transaction.addToBackStack("Play")
//                    transaction.commit()

                    findNavController().navigateUp()
                }
                else
                {
                    buttonStartGame.isEnabled = true
                }
            }
        }
    }

    private fun timeSlotWithinBookedRange(slot: Calendar, bookingStart: Date, bookingEnd: Date): Boolean {
        val bookingStartCal = Calendar.getInstance()
        val bookingEndCal = Calendar.getInstance()
        bookingStartCal.time = bookingStart
        bookingEndCal.time = bookingEnd

        // Normalize the year, month, and date
        bookingStartCal.set(Calendar.MILLISECOND, 0)
        bookingEndCal.set(Calendar.MILLISECOND, 0)
        slot.set(Calendar.YEAR, bookingStartCal.get(Calendar.YEAR))
        slot.set(Calendar.MONTH, bookingStartCal.get(Calendar.MONTH))
        slot.set(Calendar.DAY_OF_MONTH, bookingStartCal.get(Calendar.DAY_OF_MONTH))
        slot.set(Calendar.MILLISECOND, 0)

        val r1 = slot.time == bookingStartCal.time
        val r2 = slot.time.after(bookingStartCal.time) || slot.time.equals(bookingStartCal.time)
        val r3 = slot.time.before(bookingEndCal.time) && !slot.time.equals(bookingEndCal.time)

        val a1 = r1
        val a2 = r2 && r3

        //Log.d("timeslot", "${slot.time} $r1 $r2 $r3")

        // Compare only the times, ignore the date
        return a1 || a2
    }

    private fun saveBookingToFirestore(year: Int, month: Int, day: Int, selectedTimeSlot: String?, courtRef: DocumentReference, onFinish: (Boolean) -> Unit) : Boolean {
        if (selectedTimeSlot == null) {
            Toast.makeText(context, "Please select a time slot", Toast.LENGTH_SHORT).show()
            return false
        }

        // Parsing the selected time slot
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month) // Note: month is 0-indexed
            set(Calendar.DAY_OF_MONTH, day)

            // Assuming the time slot is in the format "HH:mm"
            val time = timeFormat.parse(selectedTimeSlot)
            time?.let {
                set(Calendar.HOUR_OF_DAY, it.hours)
                set(Calendar.MINUTE, it.minutes)
            }

            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

        }.time

        // Firestore Timestamp
        val timestamp = Timestamp(date)

        val currentUser = AuthData.auth.currentUser
        val ownerRef = currentUser?.let {
            AuthData.db.collection("users").document(it.uid)
        }

        if (ownerRef == null)
        {
            Toast.makeText(context, "Error saving booking: User not found", Toast.LENGTH_LONG).show()
            return false
        }

        // Construct the Booking object
        val bookingData = hashMapOf(
            "owner" to ownerRef,
            "court" to courtRef,
            "date" to timestamp,
            "duration" to 90 // Example duration
        )

        // Saving the booking to Firestore
        AuthData.db.collection("bookings")
            .add(bookingData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "Booking saved with ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                onFinish(true)
                // Perform other actions if needed after booking is saved
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error saving booking: ${e.message}", Toast.LENGTH_LONG).show()
                onFinish(false)
                // Handle the error appropriately
            }

        return true
    }

    private fun loadAvailableTimeSlots(view: View, selectedDate: Date) {
        val recyclerViewTimeSlots = view.findViewById<RecyclerView>(com.example.playtomictonyaymane.R.id.recyclerViewTimeSlots)

        // Show loading indicator
        val adapterTimeSlot = recyclerViewTimeSlots.adapter as TimeSlotAdapter
        adapterTimeSlot.updateTimeslots(emptyList(), true)

        // Construct start and end points for the selected date
        val dayStart = Calendar.getInstance().apply {
            time = selectedDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.time
        val dayEnd = Calendar.getInstance().apply {
            time = selectedDate
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.time

        val dayStartCal = Calendar.getInstance().apply {
            time = dayStart
        }
        val currentTime = Calendar.getInstance().time

        AuthData.db.collection("bookings")
            .whereGreaterThanOrEqualTo("date", dayStart)
            .whereLessThanOrEqualTo("date", dayEnd)
            .get()
            .addOnSuccessListener { documents ->
                val bookedRanges = documents.mapNotNull { document ->
                    val startTime = document.getTimestamp("date")?.toDate()
                    val duration = (document.getLong("duration") ?: return@mapNotNull null).toInt()

                    startTime?.let { start ->
                        val end = Calendar.getInstance().apply {
                            time = start
                            add(Calendar.MINUTE, duration)
                        }.time
                        start to end
                    }
                }.sortedBy { it.first } // Sort by the start times of the bookings for easier management.

                val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())

                // Generate the complete list of time slots for the day
                val fullDayTimeSlots = generateTimeSlots()

                // Calculate the last possible starting time for a 90-minute slot before the day ends
//                val lastStartTime = Calendar.getInstance().apply {
//                    time = dayEnd
//                    add(Calendar.MINUTE, -90) // 90 Minutes before end time
//                }.time
//
//                val lastStartSlot = timeFormat.format(lastStartTime)

                // Filter to find only available (unbooked) 90-minute time slots
                // .takeWhile { it <= lastStartSlot }
                // Filtering the time slots to exclude any that overlap with booked slots.
                val availableTimeSlots = fullDayTimeSlots.filter { startTimeStr ->
                    val startTimeCal = Calendar.getInstance().apply {
                        time = timeFormat.parse(startTimeStr) ?: throw IllegalArgumentException("Invalid time string: $startTimeStr")
                        set(Calendar.YEAR, dayStartCal.get(Calendar.YEAR))
                        set(Calendar.MONTH, dayStartCal.get(Calendar.MONTH))
                        set(Calendar.DAY_OF_MONTH, dayStartCal.get(Calendar.DAY_OF_MONTH))
                    }

                    // Filter out times before the current time
                    val slotStartTime = startTimeCal.time
                    if (slotStartTime.before(currentTime)) return@filter false

                    // Increment the start time by 90 minutes to get the end time of this prospective booking slot
                    val potentialEndTimeCal = (startTimeCal.clone() as Calendar).apply {
                        add(Calendar.MINUTE, 90)
                    }

                    // Ensure that the 90-minute slot doesn't overlap with any existing bookings
                    // and that it also does not surpass the last possible start time slot.
                    val a2 = bookedRanges.none { (bookingStart, bookingEnd) ->
                        val a1 = timeSlotOverlapsBookedRange(startTimeCal.time, potentialEndTimeCal.time, bookingStart, bookingEnd)

                        Log.v("Timeslots", "`${startTimeCal.time}` TO `${potentialEndTimeCal.time}` vs `${bookingStart}` TO `${bookingEnd}` -> ${a1}")

                        return@none a1
                    }
                    Log.v("Timeslots", "${a2}")
                    return@filter a2
                }

                // Update RecyclerView adapter
                adapterTimeSlot.updateTimeslots(availableTimeSlots, false)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching bookings", exception)
                // Handle the error appropriately
                adapterTimeSlot.updateTimeslots(emptyList(), false)
            }
    }
    private fun timeSlotOverlapsBookedRange(slotStart: Date, slotEnd: Date, bookingStart: Date, bookingEnd: Date): Boolean {
        // A slot overlaps with a booking if it starts before the booking ends and ends after the booking starts.
        // This will cover any partial or full overlap within the booking range.
        return slotStart.before(bookingEnd) && slotEnd.after(bookingStart)
    }

    private fun fetchCourtsAndSetupSpinner(spinner: Spinner) {
        val courtsList: MutableList<CourtItem> = mutableListOf()

        AuthData.db.collection("courts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val documentId = document.id
                    val name = document.getString("name") ?: ""
                    val address = document.getString("address") ?: ""
                    courtsList.add(CourtItem(documentId, name, address))
                }

                val adapter = CourtSpinnerAdapter(requireContext(), courtsList)
                spinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("Courts", "Error getting documents: ", exception)
            }
    }

    private fun generateTimeSlots(): List<String> {
        val timeSlots = mutableListOf<String>()
        val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())

        val startTime = 9
        val endTime = 17
        val calendar = java.util.Calendar.getInstance()

        calendar.set(java.util.Calendar.HOUR_OF_DAY, startTime)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)

        val lastStartTime = Calendar.getInstance().apply {
            time = calendar.time
            set(Calendar.HOUR, endTime)
            add(Calendar.MINUTE, -90) // 90 Minutes before end time
        }.time

        while (calendar.get(java.util.Calendar.HOUR_OF_DAY) < endTime) {
            if (calendar.time > lastStartTime) {
                break
            }
            val timeSlot = timeFormat.format(calendar.time)
            timeSlots.add(timeSlot)
            calendar.add(java.util.Calendar.MINUTE, 30)
        }

        return timeSlots
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class CourtItem(val documentId: String, val name: String, val address: String)
class CourtSpinnerAdapter(context: Context, private val courts: List<CourtItem>) : ArrayAdapter<CourtItem>(context, 0, courts) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val court = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_court, parent, false)

        val textViewName = view.findViewById<TextView>(R.id.textViewCourtName)
        val textViewAddress = view.findViewById<TextView>(R.id.textViewCourtAddress)

        textViewName.text = court?.name
        textViewAddress.text = court?.address

        return view
    }
}
