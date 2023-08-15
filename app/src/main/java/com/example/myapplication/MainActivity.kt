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
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun changeLanguage(locale: Locale) {
        val configuration = Configuration()
        configuration.setLocale(locale)
        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)
    }
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

        val btnChangeToRussian = findViewById<Button>(R.id.btnChangeLanguageToRussian)
        val btnChangeToEnglish = findViewById<Button>(R.id.btnChangeLanguageToEnglish)

        btnChangeToRussian.setOnClickListener {
            val locale = Locale("ru")
            Locale.setDefault(locale)
            changeLanguage(locale)
            recreate()
        }

        btnChangeToEnglish.setOnClickListener {
            val locale = Locale("en")
            Locale.setDefault(locale)
            changeLanguage(locale)
            recreate()
        }
    }
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        recreate()
    }

    private fun showSignInDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_sign_in, null)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)

        val dialog = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
            .setTitle(R.string.sign_in)
            .setView(dialogView)
            .setPositiveButton(R.string.sign_in) { dialogInterface: DialogInterface, _: Int ->
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                signIn(email, password)
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
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

                        saveUidToSharedPreferences(uid)

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
                    when (val exception = task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            Toast.makeText(this, "Invalid email address or account does not exist", Toast.LENGTH_SHORT).show()
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Error: ${exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun saveUidToSharedPreferences(uid: String) {
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref.edit()
        editor.putString("uid", uid)
        editor.apply()
    }

    private fun showSignUpDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_sign_up, null)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = dialogView.findViewById<EditText>(R.id.etConfirmPassword)

        val dialog = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
            .setTitle(R.string.sign_up)
            .setView(dialogView)
            .setPositiveButton(R.string.sign_up) { dialogInterface: DialogInterface, _: Int ->
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                if (password == confirmPassword) {
                    signUp(email, password)
                } else {
                    Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
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

