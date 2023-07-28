package com.example.myapplication.EmployerScreen

import com.example.myapplication.Adapter.ProblemAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Problem
import com.example.myapplication.R
import com.google.firebase.database.*

class EmployerActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var problemAdapter: ProblemAdapter
    private val problemList: MutableList<Problem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employer)

        recyclerView = findViewById(R.id.recyclerViewProblems)
        problemAdapter = ProblemAdapter(problemList)
        recyclerView.adapter = problemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Получение списка проблем из Firebase
        val databaseRef = FirebaseDatabase.getInstance().reference.child("problems")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                problemList.clear()
                for (problemSnapshot in snapshot.children) {
                    val problem = problemSnapshot.getValue(Problem::class.java)
                    if (problem != null) {
                        problemList.add(problem)
                    }
                }
                problemAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки, если не удалось получить данные из Firebase
            }
        })

        // Обработчик клика на элемент списка проблем
        problemAdapter.setOnItemClickListener(object : ProblemAdapter.OnItemClickListener {
            override fun onItemClick(problem: Problem) {
                val intent = Intent(this@EmployerActivity, RespondActivity::class.java)
                intent.putExtra("problem", problem)
                intent.putExtra("problemKey", problem.key)
                intent.putExtra("problemTitle", problem.title)
                startActivity(intent)
            }
        })

    }
}