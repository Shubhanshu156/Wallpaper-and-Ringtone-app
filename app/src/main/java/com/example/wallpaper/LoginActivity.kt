package com.example.wallpaper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*

import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

import android.content.Intent
import android.util.Log

import com.google.firebase.auth.AuthResult

import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import java.lang.Exception
import com.google.firebase.auth.FirebaseAuthUserCollisionException

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

import com.google.firebase.auth.FirebaseAuthWeakPasswordException





class LoginActivity : AppCompatActivity() {
    lateinit var emailTextView: EditText
    lateinit  var passwordTextView:EditText
    lateinit var Btn: Button
    lateinit var progressbar: ProgressBar
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance();
         emailTextView = findViewById<EditText>(R.id.email);
         passwordTextView = findViewById<EditText>(R.id.password);
         Btn = findViewById<Button>(R.id.login);
         progressbar = findViewById<ProgressBar>(R.id.progressBar);
        val txt=findViewById<TextView>(R.id.back)
        txt.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left ,android.R.anim.slide_in_left);
        }
        Btn.setOnClickListener {
            try{
                loginUserAccount();
            }
            catch (e:Exception){
                Toast.makeText(this,"Unable to login try after some time",Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun loginUserAccount() {

        progressbar.visibility = View.VISIBLE
        var email: String
        var password: String
        email = emailTextView.text.toString()
        password = passwordTextView.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,
                "Please enter email!!",
                Toast.LENGTH_SHORT)
                .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                "Please enter password!!",
                Toast.LENGTH_SHORT)
                .show();
            return;
        }


        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                Toast.makeText(this,"Login failed dont know error!!",Toast.LENGTH_SHORT).show();
                if (task.isSuccessful) {

                    progressbar!!.visibility=View.GONE

                    val intent = Intent(this@LoginActivity, MainActivity2::class.java)
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                else {
                    try {
                        throw task.exception!!
                    }  catch (e: Exception) {
                        Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
//                        Log.e(TAG, e.message)
                    }


                    progressbar!!.visibility=View.GONE
                }
            }
    }
}