package com.example.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DefaultUserScreen.UserActivity
import com.example.myapplication.EmployerScreen.EmployerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnSignIn.setOnClickListener {
            showSignInDialog()
        }

        btnSignUp.setOnClickListener {
            showSignUpDialog()
        }
    }

    private fun showSignInDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_sign_in, null)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Sign In")
            .setView(dialogView)
            .setPositiveButton("Sign In") { dialogInterface: DialogInterface, _: Int ->
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                signIn(email, password)
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
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
                            Toast.makeText(this, "Неверный адрес электронной почты или аккаунт не существует", Toast.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Ошибка входа в систему: ${exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }
    private fun showSignUpDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_sign_up, null)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = dialogView.findViewById<EditText>(R.id.etConfirmPassword)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Sign Up")
            .setView(dialogView)
            .setPositiveButton("Sign Up") { dialogInterface: DialogInterface, _: Int ->
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                if (password == confirmPassword) {
                    signUp(email, password)
                } else {
                    // Passwords don't match, show an error (e.g., a toast)
                    Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
