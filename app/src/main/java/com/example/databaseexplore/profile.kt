package com.example.databaseexplore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.databaseexplore.databinding.ActivityProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profile : AppCompatActivity() {
    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    val database = FirebaseDatabase.getInstance().reference.child("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val temp1 = intent.getStringExtra( "key")

        if (temp1 != null) {
            database.child(temp1).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val storedMoney = snapshot.child("money").getValue(String::class.java)
                    val storedName = snapshot.child("name").getValue(String::class.java)
                    binding.profilename.text = storedName
                    binding.moneyin.text = storedMoney
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@profile, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }

            })
        }
        binding.profileaddmoney.setOnClickListener{
            val intent = Intent(this, addmoneyfinal::class.java)
            intent.putExtra("key", temp1)
            startActivity(intent)
            finish()
        }

        binding.profilesignout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "SignOut Successful", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.profiletransfer.setOnClickListener {{}
            val intent = Intent(this, Transfer::class.java)
            intent.putExtra("key", temp1)
            startActivity(intent)
            finish()
        }
    }
}