package com.example.databaseexplore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.databaseexplore.databinding.ActivityLoginBinding
import com.example.databaseexplore.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class login : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.Signin.setOnClickListener {
            val database = FirebaseDatabase.getInstance().reference.child("users")

            val name1 = binding.namein.text.toString().trim()
            val pass1 = binding.passin.text.toString().trim()
            if (name1.isEmpty() || pass1.isEmpty()) {
                Toast.makeText(this, "Fill Both Fields", Toast.LENGTH_SHORT).show()
            }else {
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var userFound = false
                    var userId: String?
                    for (userSnapshot in snapshot.children) {
                        val storedName = userSnapshot.child("name").getValue(String::class.java)
                        val storedPass = userSnapshot.child("password").getValue(String::class.java)
                        if (storedName == name1) {
                            userFound = true
                            if (storedPass == pass1) {
                                userId = userSnapshot.key
                                val intent = Intent(this@login, profile::class.java)
                                intent.putExtra("key", userId)
                                startActivity(intent)
                                Toast.makeText(this@login, "Login Successful", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@login, "Incorrect Password", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                    if (!userFound) {
                        Toast.makeText(this@login, "User Not Found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@login, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }

            })


            }
        }
    }
}