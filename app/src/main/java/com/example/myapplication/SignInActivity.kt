package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignIn = findViewById(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Успешный вход в систему, получение UID текущего пользователя
                        val uid = user.uid

                        // Проверка типа аккаунта и перенаправление в соответствующую активити
                        if (uid == "G8wWA7X6XzMEYTOksBOsSH53vWk2") {
                            val intent = Intent(this, EmployerActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this, UserActivity::class.java)
                            startActivity(intent)


                            finish()
                        }
                    }
                } else {
                    // Обработка ошибок входа в систему
                    when (val exception = task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            // Неверный адрес электронной почты или аккаунт не существует
                            Toast.makeText(this, "Неверный адрес электронной почты или аккаунт не существует", Toast.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Неверный пароль
                            Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // Общая ошибка входа в систему
                            Toast.makeText(this, "Ошибка входа в систему: ${exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }
}
