package com.example.myapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TextEntryActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_entry)

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
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
            }
        }
    }
}
