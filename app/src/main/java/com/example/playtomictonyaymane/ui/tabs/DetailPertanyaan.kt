package com.example.playtomictonyaymane.ui.tabs

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playtomictonyaymane.R

class DetailPertanyaan:AppCompatActivity() {
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activities_detail)


        val title = intent.getStringExtra("TITLE")
        val category = intent.getStringExtra("CATEGORY")
        val content = intent.getStringExtra("CONTENT")

        val textViewTitle: TextView = findViewById(R.id.textViewTitle)
        val textViewCategory: TextView = findViewById(R.id.textViewCategory)
        val textViewContent: TextView = findViewById(R.id.textViewContent)

        textViewTitle.text = title
        textViewCategory.text = category
        textViewContent.text = content

    }
}