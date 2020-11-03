package com.azeem.hardwork

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.getInstance
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.nav_header_main.*


class SignupActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var auth: FirebaseAuth
    var mProgress: ProgressDialog? = null
    var runnable: Runnable? = null
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        database = getInstance()
        reference = database.getReference("Users")

        auth = FirebaseAuth.getInstance()

        mProgress = ProgressDialog(this);
        mProgress!!.setTitle("Processing...");
        mProgress!!.setMessage("Please wait...");
        mProgress!!.setCancelable(false);
        mProgress!!.setIndeterminate(true);

        loadData(false)
        if (loadData(true)) {
            openActivity(MainPageActivity::class.java)
        }

    }

    fun buSignup(view: View) {
        signUpValidate()
    }

    fun signUpValidate() {
        var uFname = txtFirstName.text.toString()
        var uLname = txtLastName.text.toString()
        var uMail = txtEmail.text.toString()
        var uPassword = txtPssword.text.toString()
        var uCpassword = txtConfirmPassword.text.toString()


        if (uFname.isNamevalid()
            && uLname.isNamevalid()
            && uMail.isEmailValid()
            && uPassword.isPassWordValid()
            && uCpassword.isPassWordValid()
        ) {

            writeNewUser(
                txtFirstName.text.toString(),
                txtLastName.text.toString(),
                txtEmail.text.toString()
            )

            auth.createUserWithEmailAndPassword(txtEmail.text.toString(), txtPssword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Account Created Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        saveData()
                        runnable = Runnable {
                            if (!isFinishing) {
                                mProgress!!.dismiss()
                                finish()
                            }
                        }
                        mProgress!!.show();
                        handler.postDelayed(runnable!!, 2000)
                        openActivity(MainPageActivity::class.java)
                        finish()
                    } else {
                        Toast.makeText(
                            baseContext, "Signup failed try after sometime",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {

            if (uFname.isEmpty()
                || uLname.isEmpty()
                || uMail.isEmpty()
                || uPassword.isEmpty()
                || uCpassword.isEmpty()
            ) {
                Toast.makeText(applicationContext, "Can't be Empty", Toast.LENGTH_SHORT).show()
            } else {
                if (uPassword != uCpassword) {
                    txtConfirmPassword.error = "confirm password and password not same"
                }
            }

            txtFirstName.error = "Not valid"
            txtLastName.error = "Not valid"
            txtEmail.error = "enter valid format"
            txtPssword.error = "enter valid format"
            txtConfirmPassword.error = "enter valid format"
        }

    }

    fun saveData() {

        val firstname = txtFirstName.text.toString()
        val lastname = txtLastName.text.toString()
        val email = txtEmail.text.toString()
        val password = txtPssword.text.toString()

        val sharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply {

            putString("firstname", firstname)
            putString("lastname", lastname)
            putString("email", email)
            putString("password", password)

        }.apply()
        Toast.makeText(applicationContext, "it saved!!!!", Toast.LENGTH_LONG).show()
    }

    fun loadData(boolean: Boolean): Boolean {

        val sharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
        val firstname = sharedPreferences.getString("firstname", null)
        val lastname = sharedPreferences.getString("lastname", null)
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        txtFirstName.setText(firstname)
        txtLastName.setText(lastname)
        txtEmail.setText(email)
        txtPssword.setText(password)
        return firstname != null && lastname != null && email != null && password != null
    }


    fun buBacktoLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    data class User(
        var fname: String? = "",
        var lname: String? = "",
        var email: String? = ""
    )

    private fun writeNewUser(fname: String, lname: String, email: String?) {
        var userId = FirebaseAuth.getInstance().uid ?: ""
        val user = User(fname, lname, email)
        reference.child(userId).setValue(user)
      //  reference.child("users").child(userId).setValue(user)
      //  reference.child("users").child(userId).child("fname").setValue(fname)

    }

    private fun addUserChangeListener() {
        // User data change listener
        var userId = FirebaseAuth.getInstance().uid ?: ""
        reference.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java) ?: return


                // Display newly updated name and email
                tv_ViewUserMailId.setText(user?.fname).toString()
                tv_ViewUserName.setText(user?.lname).toString()
                tvRememberpassword.setText(user?.email).toString()

                // clear edit text
                txtFirstName.setText("")
                txtLastName.setText("")
                txtEmail.setText("")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
