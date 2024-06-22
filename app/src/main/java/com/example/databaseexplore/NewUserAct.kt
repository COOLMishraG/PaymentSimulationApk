package com.example.databaseexplore

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.example.databaseexplore.UserDetails

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.databaseexplore.databinding.ActivityNewUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewUserAct : AppCompatActivity() {
    private val binding: ActivityNewUserBinding by lazy {
        ActivityNewUserBinding.inflate(layoutInflater)
    }
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference
        binding.SignUp.setOnClickListener {
            val name = binding.nameup.text.toString().trim()
            val pass = binding.passup.text.toString().trim()
            val money = binding.moneyup.text.toString().trim()

            if (name.isEmpty() || pass.isEmpty() || money.isEmpty()) {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show()
            } else {

                val user = UserDetails(name, pass , money)
                user?.let { user ->

                    //generate a unique key for the user
                    val userkey = database.child("users").push().key
                    //user details\
                    if (userkey != null) {
                        val userRef = database.child("users").child(userkey)

                        userRef.setValue(user)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this, login::class.java)
                                    startActivity(intent)
                                    Toast.makeText(this, "User Added", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                }

                            }
                    }
                }
            }
        }
    }
}