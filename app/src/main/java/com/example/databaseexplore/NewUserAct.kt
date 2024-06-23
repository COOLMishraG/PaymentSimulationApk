package com.example.databaseexplore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.databaseexplore.UserDetails

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.databaseexplore.databinding.ActivityNewUserBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class NewUserAct : AppCompatActivity() {
    private val binding: ActivityNewUserBinding by lazy {
        ActivityNewUserBinding.inflate(layoutInflater)
    }
    var imagekey:String?=null
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imageView.setImageResource(R.drawable.profile)

        binding.uploadImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            imageLauncher.launch(intent)
        }
        database = FirebaseDatabase.getInstance().reference
        binding.SignUp.setOnClickListener {
            val name = binding.nameup.text.toString().trim()
            val pass = binding.passup.text.toString().trim()
            val money = binding.moneyup.text.toString().trim()

            if (name.isEmpty() || pass.isEmpty() || money.isEmpty()) {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show()
            } else {

                val user = UserDetails(name, pass , money , imagekey.toString())
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
    val imageLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== RESULT_OK ){
            if(it.data!=null){
                val firebaseStorage = Firebase.storage.reference.child("ProfilePicture" +System.currentTimeMillis()+"." + getfileType(it.data!!.data))
                Toast.makeText(this, "Please Wait", Toast.LENGTH_LONG).show()
                firebaseStorage.putFile(it.data!!.data!!).addOnSuccessListener {
                    firebaseStorage.downloadUrl.addOnSuccessListener {
                        binding.imageView.setImageURI(it)
                        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_LONG).show()
                        Picasso.get().load(it.toString()).into(binding.imageView)
                        imagekey = it.toString()
                    }
                }
            }
        }
    }

    private fun getfileType(data: Uri?): String? {
        val r=contentResolver
        val mimeType= MimeTypeMap.getSingleton()
        return mimeType.getMimeTypeFromExtension(r.getType(data!!))
    }
}