package com.example.myapplication.popularProblem

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class PopularProblem3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popular_problem3)

        val imageClose = findViewById<ImageView>(R.id.imageClose)
        imageClose.setOnClickListener {
            finish()
        }
    }
}