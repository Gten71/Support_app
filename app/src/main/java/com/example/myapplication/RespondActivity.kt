package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RespondActivity : AppCompatActivity() {
    private lateinit var etResponse: EditText
    private lateinit var tvProblemTitle: TextView
    private lateinit var tvProblemContent: TextView
    private lateinit var problem: Problem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_respond)

        problem = intent.getSerializableExtra("problem") as Problem

        tvProblemTitle = findViewById(R.id.tvProblemTitle)
        tvProblemContent = findViewById(R.id.tvProblemContent)
        etResponse = findViewById(R.id.etResponse)
        val btnSendResponse = findViewById<Button>(R.id.btnSendResponse)

        // Заполнение текстовых полей заголовка и содержания проблемы
        tvProblemTitle.text = problem.title
        tvProblemContent.text = problem.content

        btnSendResponse.setOnClickListener {
            val responseText = etResponse.text.toString().trim()
            if (responseText.isNotEmpty()) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val databaseRef = FirebaseDatabase.getInstance().reference.child("responses")
                    val responseKey = databaseRef.push().key

                    if (responseKey != null) {
                        Log.d("RespondActivity", "Problem Key: ${problem.key}")
                        val responseData = HashMap<String, Any>()
                        responseData["userId"] = userId
                        responseData["problemKey"] = problem.key
                        responseData["problemTitle"] = problem.title // Добавляем problemTitle
                        responseData["responseText"] = responseText

                        databaseRef.child(responseKey).setValue(responseData)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    finish()
                                } else {
                                }
                            }
                    }
                }
            }
        }
    }
}

