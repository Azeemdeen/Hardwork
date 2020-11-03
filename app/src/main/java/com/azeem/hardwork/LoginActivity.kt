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
import android.view.WindowManager
import android.view.WindowManager.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.txtLoginEmail

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var mProgress: ProgressDialog? = null
    var runnable: Runnable? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        mProgress = ProgressDialog(this);
        mProgress!!.setTitle("Processing...")
        mProgress!!.setMessage("Please wait...")
        mProgress!!.setCancelable(false)
        mProgress!!.setIndeterminate(true)

        loadData(false)
        if (loadData(true)) {
            openActivity(MainPageActivity::class.java)
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
        } else {
            Toast.makeText(baseContext, "Login failed", Toast.LENGTH_SHORT).show()
        }

    }

    fun buLoginEvent(view: View) {

        logging()


    }

    fun logging() {
        var emaillog = txtLoginEmail.text.toString()
        var passwordlog = txtLoginPassword.text.toString()

        if (emaillog.isEmpty() && passwordlog.isEmpty()) {
            txtLoginEmail.error = "empty field"
            txtLoginPassword.error = "empty field"
        } else {
            if (passwordlog.length < 6) {
                txtLoginPassword.error = "length should be 6 to 20"
            } else if (!emaillog.isEmailValid()) {
                txtLoginEmail.error = "not valid"
            } else {
                auth.signInWithEmailAndPassword(
                    txtLoginEmail.text.toString(),
                    txtLoginPassword.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user)
                        } else {

                            updateUI(null)

                        }
                    }
                runnable = Runnable {
                    if (!isFinishing) {

                        openActivity(MainPageActivity::class.java)
                        mProgress!!.dismiss()
                        finish()
                    }
                }
                Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                saveData()
                mProgress!!.show();
                handler.postDelayed(runnable!!, 2000)
            }
        }


    }

    fun saveData() {

        val email = txtLoginEmail.text.toString()
        val password = txtLoginPassword.text.toString()

        val sharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply {

            putString("email", email)
            putString("password", password)

        }.apply()
        Toast.makeText(applicationContext, "saved!!!!", Toast.LENGTH_SHORT).show()
    }

    fun loadData(boolean: Boolean): Boolean {

        val sharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        txtLoginEmail.setText(email)
        txtLoginPassword.setText(password)
        return email != null && password != null
    }


    fun buCreate(view: View) {
        openActivity(SignupActivity::class.java)
        finish()
    }

    fun buResetPassword(view: View) {
        openActivity(ForgotActivity::class.java)
        finish()
    }

}
