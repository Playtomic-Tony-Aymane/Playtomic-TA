package com.example.playtomictonyaymane.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.ui.play.PlayFragment

class ActivitiesFragment: Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter = RecyclerAdapter()
        recyclerView.adapter = adapter


        view?.findViewById<Button>(R.id.btnAddActivity)?.setOnClickListener {
            val playFragment = PlayFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            // Vervang het huidige fragment door PlayFragment
            transaction.replace(R.id.container, playFragment)
            transaction.addToBackStack(null)
            transaction.commit()


        }
    }

}