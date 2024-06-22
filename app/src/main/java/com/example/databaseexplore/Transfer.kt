package com.example.databaseexplore

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.databaseexplore.databinding.ActivityTransferBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Transfer : AppCompatActivity() {
    val binding: ActivityTransferBinding by lazy {
    ActivityTransferBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

         val database = FirebaseDatabase.getInstance().reference.child("users")
        binding.button.setOnClickListener {
            val ToWhom = binding.towhom.text.toString().trim()
            val Amount = binding.howmuch.text.toString().trim()
            val key = intent.getStringExtra("key")
            if (key != null) {
                database.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (userSnapshot in snapshot.children) {
                            val storedName = userSnapshot.child("name").getValue(String::class.java)
                            if (storedName == ToWhom) {
                                val keyToWhom = userSnapshot.key
                                var Tempamt =
                                    userSnapshot.child("money").getValue(String::class.java)
                                var intTempamt = Tempamt?.toIntOrNull()
                                if (intTempamt != null) {
                                    intTempamt = intTempamt + Amount.toInt()
                                }
                                database.child(keyToWhom.toString()).child("money")
                                    .setValue(intTempamt.toString())
                                var tempamt =
                                    snapshot.child(key).child("money").getValue(String::class.java)
                                var inttempamt = tempamt?.toIntOrNull()
                                if (inttempamt != null) {
                                    inttempamt = inttempamt - Amount.toInt()
                                }
                                database.child(key).child("money").setValue(inttempamt.toString())
                                    .addOnSuccessListener {
                                        Toast.makeText(this@Transfer, "Transfer Successful", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this@Transfer, profile::class.java)
                                        intent.putExtra("key", key)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { Toast.makeText(this@Transfer, "Failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@Transfer, "Failed to Transfer", Toast.LENGTH_SHORT).show()
                    }
                }
                    )
            }}}}

