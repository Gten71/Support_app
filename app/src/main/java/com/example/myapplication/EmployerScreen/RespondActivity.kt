package com.example.myapplication.EmployerScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Models.Problem
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.bumptech.glide.Glide

class RespondActivity : AppCompatActivity() {
    private lateinit var etResponse: EditText
    private lateinit var tvProblemTitle: TextView
    private lateinit var tvProblemContent: TextView
    private lateinit var ivProblemPhoto: ImageView // Добавляем переменную для ImageView
    private lateinit var problem: Problem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_respond)

        tvProblemTitle = findViewById(R.id.tvProblemTitle)
        tvProblemContent = findViewById(R.id.tvProblemContent)
        etResponse = findViewById(R.id.etResponse)
        ivProblemPhoto = findViewById(R.id.ivProblemPhoto)

        problem = intent.getSerializableExtra("problem") as Problem

        tvProblemTitle.text = problem.title
        tvProblemContent.text = problem.content

        // Проверяем, есть ли URL фото в объекте проблемы
        val photoUrl = problem.photoUrl
        if (photoUrl != null && photoUrl.isNotEmpty()) {
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(ivProblemPhoto)

            ivProblemPhoto.visibility = View.VISIBLE
        } else {
            // Прячем ImageView, если нет фото
            ivProblemPhoto.visibility = View.GONE
        }

        // Добавляем обработчик нажатия кнопки "Send response"
        val btnSendResponse = findViewById<Button>(R.id.btnSendResponse)
        btnSendResponse.setOnClickListener {
            val responseText = etResponse.text.toString().trim()
            if (responseText.isNotEmpty()) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val databaseRef = FirebaseDatabase.getInstance().reference.child("responses")
                    val responseKey = databaseRef.push().key

                    if (responseKey != null) {
                        val responseData = HashMap<String, Any>()
                        responseData["userId"] = userId
                        responseData["problemKey"] = problem.key
                        responseData["problemTitle"] = problem.title
                        responseData["responseText"] = responseText

                        databaseRef.child(responseKey).setValue(responseData)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    finish()
                                } else {
                                    // Обработка ошибки, если не удалось отправить ответ
                                }
                            }
                    }
                }
            }
        }

        // Добавляем обработчик нажатия на кнопку "Question"
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, EmployerQuestionActivity::class.java)
            startActivity(intent)
        }

        // Добавляем обработчик нажатия на кнопку "Close"
        val imageClose = findViewById<ImageView>(R.id.imageClose)
        imageClose.setOnClickListener {
            finish()
        }
    }
}
