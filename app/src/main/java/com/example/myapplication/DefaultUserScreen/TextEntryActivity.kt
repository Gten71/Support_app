package com.example.myapplication.DefaultUserScreen

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.EmployerQuestionActivity
import com.example.myapplication.QuestionActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TextEntryActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var radioGroupEmployers: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_entry)

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        radioGroupEmployers = findViewById(R.id.radioGroupEmployers)

        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            val selectedEmployerId = radioGroupEmployers.checkedRadioButtonId

            val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
            floatingActionButton.setOnClickListener {
                val intent = Intent(this, QuestionActivity::class.java)
                startActivity(intent)
            }
            val imageClose = findViewById<ImageView>(R.id.imageClose)
            imageClose.setOnClickListener {
                finish()
            }

            if (title.isNotEmpty() && content.isNotEmpty() && selectedEmployerId != -1) {
                val selectedEmployer = findViewById<RadioButton>(selectedEmployerId)?.text.toString()

                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    val databaseRef = FirebaseDatabase.getInstance().reference.child("problems")
                    val problemRef = databaseRef.push() // Создаем уникальный ключ для проблемы

                    val problemKey = problemRef.key // Получаем сгенерированный ключ

                    if (problemKey != null) {
                        val problemData = HashMap<String, Any>()
                        problemData["userId"] = userId
                        problemData["title"] = title
                        problemData["content"] = content
                        problemData["employer"] = selectedEmployer // Add selected employer to the problem data

                        problemRef.setValue(problemData)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Проблема успешно отправлена
                                    Log.d(TAG, "Проблема успешно отправлена. ProblemKey:$problemKey")
                                    finish()
                                } else {
                                    // Ошибка при отправке проблемы
                                    Log.d(TAG, "Ошибка при отправке проблемы: ${task.exception}")
                                }
                            }
                    }
                }
            } else {
                // Если пользователь не выбрал работодателя или не заполнил обязательные поля, показываем ошибку
                Toast.makeText(this, "Заполните все поля и выберите работодателя перед отправкой проблемы.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


