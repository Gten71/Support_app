package com.example.myapplication.DefaultUserScreen

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.AnswerAdapter
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class AnsweresActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var answerAdapter: AnswerAdapter
    private val answerList: MutableList<Pair<String, String>> = mutableListOf()
    private lateinit var problemKey: String
    private lateinit var problemTitle: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answeres)

        problemKey = intent.getStringExtra("problemKey") ?: ""
        problemTitle = intent.getStringExtra("problemTitle") ?: "" // Получаем заголовок проблемы

        title = problemTitle // Устанавливаем заголовок активити

        recyclerView = findViewById(R.id.recyclerViewAnswers)
        answerAdapter = AnswerAdapter(answerList)
        recyclerView.adapter = answerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }
        val imageClose = findViewById<ImageView>(R.id.imageClose)
        imageClose.setOnClickListener {
            finish()
        }
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
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки, если не удалось получить данные из Firebase
            }
        })
    }
}

