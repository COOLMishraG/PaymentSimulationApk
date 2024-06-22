package com.example.databaseexplore

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.databaseexplore.databinding.ActivityAddmoneyfinalBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class addmoneyfinal : AppCompatActivity() {
    val binding: ActivityAddmoneyfinalBinding by lazy {
    ActivityAddmoneyfinalBinding.inflate(layoutInflater)
    }
   private val database = FirebaseDatabase.getInstance().reference.child("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.completed.setOnClickListener {
            val key = intent.getStringExtra("key")
            if (key != null) {
                database.child(key).child("money").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var moneycome= binding.moneycome.text.toString().toIntOrNull()

                        var newMoney = snapshot.getValue(String::class.java)
                        var intmoney = newMoney?.toIntOrNull()
                        if (moneycome != null) {
                            intmoney = moneycome + intmoney!!
                        }
                        intmoney.toString()
                        database.child(key).child("money").setValue(intmoney.toString())
                            .addOnSuccessListener{
                                Toast.makeText(this@addmoneyfinal, "Money Added", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@addmoneyfinal, profile::class.java)
                                intent.putExtra("key", key)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener{
                                Toast.makeText(this@addmoneyfinal, "Failed", Toast.LENGTH_SHORT).show()
                            }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@addmoneyfinal, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                    }
                }
                )
            }
        }
        }
    }
