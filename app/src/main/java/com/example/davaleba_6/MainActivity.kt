package com.example.davaleba_6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.davaleba_6.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)


        val btnAdd = binding.btnAdd
        val btnUpdate = binding.btnUpdate
        val btnDelete = binding.btnDelete
        val etEmail = binding.etEmail
        val etFirstName = binding.etFirstName
        val etLastName = binding.etLastName
        val etAge = binding.etAge
        val etPassword = binding.etPassword
        val database = Firebase.database.reference


        btnAdd.setOnClickListener {
            val age = etAge.text.toString().trim().toInt()
            val email = etEmail.text.toString().trim()
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val password = etPassword.text.toString().trim()


            if (isValidAge(age) && isValidEmail(email) && isValidText(firstName) && isValidText(lastName) && isValidPassword(password))
            {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser


                            val userData = hashMapOf(
                                "firstname" to firstName,
                                "lastname" to lastName,
                                "age" to age,
                                "email" to email,
                            )

                            database.child("Users").child(user!!.uid).setValue(userData)


                            Toast.makeText(
                                baseContext,
                                "User added successfully",
                                Toast.LENGTH_SHORT,
                            ).show()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "User already exists",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }


            }




        }

        btnDelete.setOnClickListener{


            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (isValidEmail(email) && isValidPassword(password)){

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if(task.isSuccessful){
                            val user = auth.currentUser

                            database.child("Users").child(user!!.uid).removeValue()
                            Toast.makeText(this, "User deleted succcessfully", Toast.LENGTH_SHORT).show()
                            user.delete()
                        }
                        else{
                            Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
                        }
                    }

            }


        }

        btnUpdate.setOnClickListener{
            val age = etAge.text.toString().trim().toInt()
            val email = etEmail.text.toString().trim()
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (isValidAge(age) && isValidEmail(email) && isValidText(firstName) && isValidText(
                    lastName
                ) && isValidPassword(password)
            ) {

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser


                            val userData = hashMapOf(
                                "firstname" to firstName,
                                "lastname" to lastName,
                                "age" to age,
                                "email" to email,
                            )

                            database.child("Users").child(user!!.uid).setValue(userData)


                            Toast.makeText(
                                baseContext,
                                "User updated successfully",
                                Toast.LENGTH_SHORT,
                            ).show()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Authentication failed. ${task.exception}",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }


            }




        }


    }


    private fun isValidPassword(password: String): Boolean {
        return password.length >= 7
    }


    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun isValidAge(etAge: Int): Boolean {
        return etAge > 0
    }

    private fun isValidText(etText: String): Boolean {
        if (etText.contains("[0-9]".toRegex())) {
            return false
        }

        return true
    }

    


}