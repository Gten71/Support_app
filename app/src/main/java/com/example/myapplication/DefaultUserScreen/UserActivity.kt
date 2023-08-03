package com.example.myapplication.DefaultUserScreen

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ChatActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.preference.PreferenceManager
import com.example.myapplication.MainActivity

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Retrieve the UID from shared preferences
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val uid = sharedPref.getString("uid", "default_uid") // Replace "default_uid" with your default value

        val btnText = findViewById<Button>(R.id.btnText)
        val btnAnswers = findViewById<Button>(R.id.btnAnswers)
        val btnExit = findViewById<FloatingActionButton>(R.id.btnExit)
        val btnQuestion = findViewById<FloatingActionButton>(R.id.questionUser)
        val btnChat = findViewById<Button>(R.id.btnChat)


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

        btnChat.setOnClickListener {
            // Use the same uid to set the userId and userName for the ChatActivity
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("userId", uid)
            intent.putExtra("userName", if (uid == "Mu3pjnrWKbM0V3aVAY5bpVaQfHG2") "Gten > Employer" else "User > Employer")
            startActivity(intent)
        }
    }
}
