package com.example.myapplication.DefaultUserScreen

import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ChatActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.ListItemsAdapter
import com.example.myapplication.Models.ListItem
import com.example.myapplication.MainActivity
import com.example.myapplication.popularProblem.PopularProblem1
import com.example.myapplication.popularProblem.PopularProblem2
import com.example.myapplication.popularProblem.PopularProblem3
import com.example.myapplication.popularProblem.PopularProblem4
import com.example.myapplication.popularProblem.PopularProblem5

class UserActivity : AppCompatActivity(), ListItemsAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listItems: List<ListItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val uid = sharedPref.getString("uid", "default_uid")

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
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("is_authenticated", false)
            editor.putString("user_type", "")
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("userId", uid)
            intent.putExtra("userName", if (uid == "Mu3pjnrWKbM0V3aVAY5bpVaQfHG2") "Gten" else getString(R.string.user))
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализация списка элементов
        listItems = listOf(
            ListItem(getString(R.string.problem1), PopularProblem1::class.java),
            ListItem(getString(R.string.problem2), PopularProblem2::class.java),
            ListItem(getString(R.string.problem3), PopularProblem3::class.java),
            ListItem(getString(R.string.problem4), PopularProblem4::class.java),
            ListItem(getString(R.string.problem5), PopularProblem5::class.java)
        )

        fun updateListItems(newItems: List<ListItem>) {
            listItems = newItems
            val adapter = ListItemsAdapter(listItems, this)
            recyclerView.adapter = adapter
        }

        // Создание и установка адаптера RecyclerView
        val adapter = ListItemsAdapter(listItems, this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(item: ListItem) {
        val intent = Intent(this, item.destinationActivity)
        startActivity(intent)

    }
}
