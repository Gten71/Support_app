package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.ChatAdapter
import com.example.myapplication.Models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var userId: String // Уникальный идентификатор обычного пользователя
    private lateinit var userName: String // Имя обычного пользователя
    private lateinit var chatReference: DatabaseReference
    private lateinit var messageAdapter: ChatAdapter
    private val messageList: MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Получение данных из Intent
        userId = intent.getStringExtra("userId") ?: ""
        userName = intent.getStringExtra("userName") ?: ""

        // Проверяем, что userId и userName не являются пустыми значениями
        if (userId.isEmpty() || userName.isEmpty()) {
            Toast.makeText(this, "Ошибка: Некорректные данные пользователя", Toast.LENGTH_SHORT).show()
            finish() // Закрываем активити, если данные некорректны
            return
        }

        // Настройка тулбара
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Chat $userName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Инициализация RecyclerView и адаптера для отображения сообщений
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewChat)
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        messageAdapter = ChatAdapter(messageList, currentUserUid)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Получение ссылки на базу данных Firebase для узла "messages"
        chatReference = FirebaseDatabase.getInstance().reference.child("messages")

        // Получение списка сообщений из Firebase
        chatReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Ошибка при получении сообщений: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Обработчик кнопки отправки сообщения
        val btnSendMessage = findViewById<Button>(R.id.btnSendMessage)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        btnSendMessage.setOnClickListener {
            val messageText = etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val messageKey = chatReference.push().key
                if (messageKey != null) {
                    val message = Message(messageKey, userId, userName, messageText, System.currentTimeMillis())
                    chatReference.child(messageKey).setValue(message)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Сброс поля ввода после отправки сообщения
                                etMessage.text.clear()
                            } else {
                                Toast.makeText(this@ChatActivity, "Ошибка при отправке сообщения", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}
