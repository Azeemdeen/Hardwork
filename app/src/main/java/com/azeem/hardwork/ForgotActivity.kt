package com.azeem.hardwork

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.activity_signup.*

open class ForgotActivity : AppCompatActivity() {

    var mProgress: ProgressDialog? = null
    var runnable: Runnable? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        mProgress = ProgressDialog(this);
        mProgress!!.setTitle("Password reset successful...");
        mProgress!!.setMessage("Please wait...");
        mProgress!!.setCancelable(false);
        mProgress!!.setIndeterminate(true);

    }

    fun buReset(view: View) {
        resetting()

    }


    fun resetting() {
        var urePassword = txtForgotpasswordReset.text.toString()
        var ureCpassword=txtForgotConfirmPasswordReset.text.toString()

        if (urePassword != ureCpassword) {
            txtForgotConfirmPasswordReset.error = "confirm password and password not same"
        }
        else if (urePassword.isEmpty() || ureCpassword.isEmpty()){
            Toast.makeText(applicationContext, "Can't be empty", Toast.LENGTH_SHORT).show()
            txtForgotpasswordReset.error = "empty field"
            txtForgotConfirmPasswordReset.error = "empty field"
        }
        else{
            if (urePassword == ureCpassword){
                runnable = Runnable {
                    if (!isFinishing) {
                        var intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        mProgress!!.dismiss()
                        finish()
                    }
                }
                mProgress!!.show();
                handler.postDelayed(runnable!!, 2000)
            }
        }
    }

}