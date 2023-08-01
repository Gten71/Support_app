package com.example.myapplication.DefaultUserScreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class TextEntryActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_PICK = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_entry)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val radioGroupEmployers = findViewById<RadioGroup>(R.id.radioGroupEmployers)

        val btnChooseImage = findViewById<Button>(R.id.btnChooseImage)
        btnChooseImage.setOnClickListener {
            openImagePicker()
        }
        val imageClose = findViewById<ImageView>(R.id.imageClose)
        imageClose.setOnClickListener {
            finish()
        }

        val btnSend = findViewById<Button>(R.id.btnSend)
        btnSend.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            val selectedEmployerId = radioGroupEmployers.checkedRadioButtonId

            if (title.isNotEmpty() && content.isNotEmpty() && selectedEmployerId != -1) {
                val selectedEmployer = findViewById<RadioButton>(selectedEmployerId)?.text.toString()

                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    val databaseRef = FirebaseDatabase.getInstance().reference.child("problems")
                    val problemRef = databaseRef.push()

                    val problemKey = problemRef.key

                    if (problemKey != null) {
                        if (selectedImageUri != null) {
                            val imageRef = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}")
                            imageRef.putFile(selectedImageUri!!)
                                .addOnSuccessListener { taskSnapshot ->
                                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                                        val problemData = HashMap<String, Any>()
                                        problemData["userId"] = userId
                                        problemData["title"] = title
                                        problemData["content"] = content
                                        problemData["employer"] = selectedEmployer
                                        problemData["photoUrl"] = uri.toString()

                                        problemRef.setValue(problemData)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    finish()
                                                } else {
                                                    Toast.makeText(this, "Failed to submit the problem.", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to upload the image.", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            val problemData = HashMap<String, Any>()
                            problemData["userId"] = userId
                            problemData["title"] = title
                            problemData["content"] = content
                            problemData["employer"] = selectedEmployer

                            problemRef.setValue(problemData)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to submit the problem.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields and select an employer before submitting the problem.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            val selectedImageView = findViewById<ImageView>(R.id.selectedImage)
            selectedImageView.setImageURI(selectedImageUri)
            selectedImageView.visibility = View.VISIBLE
        }
    }
}




