package com.example.playtomictonyaymane.ui.tabs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.ActivityMainBinding
import com.example.playtomictonyaymane.databinding.FragmentActivitiesBinding

class ActivitiesFragment: Fragment(){

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.item_recycler, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        adapter.apply {

            layoutManager = LinearLayoutManager(activity)

            adapter = RecyclerAdapter()
        }
    }

}