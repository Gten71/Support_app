package com.example.myapplication.DefaultUserScreen

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReplyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

        val problemTitle = intent.getStringExtra("problemTitle")
        val responseText = intent.getStringExtra("responseText")

        val tvProblemTitle = findViewById<TextView>(R.id.tvProblemTitle)
        val tvResponseText = findViewById<TextView>(R.id.tvResponseText)

        tvProblemTitle.text = problemTitle
        tvResponseText.text = responseText

        val imageClose = findViewById<ImageView>(R.id.imageClose)
        imageClose.setOnClickListener {
            finish()
        }
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }
    }

}