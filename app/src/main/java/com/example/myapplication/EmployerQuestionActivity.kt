package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class EmployerQuestionActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employer_question)

        val imageClose = findViewById<ImageView>(R.id.imageClose)
        imageClose.setOnClickListener {
            finish()
        }

    }

}