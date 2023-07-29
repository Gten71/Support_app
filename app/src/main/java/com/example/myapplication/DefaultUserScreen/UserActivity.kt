package com.example.myapplication.DefaultUserScreen

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.QuestionActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val btnText = findViewById<Button>(R.id.btnText)
        val btnAnswers = findViewById<Button>(R.id.btnAnswers)
        val btnExit = findViewById<FloatingActionButton>(R.id.btnExit)
        val btnQuestion = findViewById<FloatingActionButton>(R.id.questionUser)

        btnText.setOnClickListener {
            val intent = Intent(this, TextEntryActivity::class.java)
            startActivity(intent)
        }

        btnAnswers.setOnClickListener {
            val intent = Intent(this, AnsweresActivity::class.java)
            startActivity(intent)
        }
        btnQuestion.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }

        btnExit.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}