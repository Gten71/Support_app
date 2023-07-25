package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AnsweresActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var answerAdapter: AnswerAdapter
    private val answerList: MutableList<Pair<String, String>> = mutableListOf()
    private lateinit var problemKey: String
    private lateinit var problemTitle: String // Добавляем переменную для хранения заголовка проблемы

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answeres)

        problemKey = intent.getStringExtra("problemKey") ?: ""
        problemTitle = intent.getStringExtra("problemTitle") ?: "" // Получаем заголовок проблемы

        Log.d("AnsweresActivity", "problemTitle: $problemTitle") // Проверяем значение problemTitle

        title = problemTitle // Устанавливаем заголовок активити


        recyclerView = findViewById(R.id.recyclerViewAnswers)
        answerAdapter = AnswerAdapter(answerList)
        recyclerView.adapter = answerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Получение списка ответов на выбранную проблему из Firebase
        val databaseRef = FirebaseDatabase.getInstance().reference.child("responses")
        databaseRef.orderByChild("problemKey").equalTo(problemKey).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                answerList.clear()
                for (responseSnapshot in snapshot.children) {
                    val problemTitle = responseSnapshot.child("problemTitle").getValue(String::class.java)
                    val responseText = responseSnapshot.child("responseText").getValue(String::class.java)
                    if (problemTitle != null && responseText != null) {
                        answerList.add(Pair(problemTitle, responseText))
                    }
                }
                answerAdapter.notifyDataSetChanged()

                // Установка заголовка проблемы в TextView
                //tvProblemTitle.text = snapshot.child(problemKey)?.child("problemTitle")?.getValue(String::class.java) ?: "Problem Title"
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки, если не удалось получить данные из Firebase
            }
        })
    }
}
