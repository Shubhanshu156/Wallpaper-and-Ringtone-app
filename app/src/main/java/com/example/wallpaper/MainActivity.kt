package com.example.wallpaper
import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.GestureDetector
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.firebase.auth.OAuthProvider
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.lang.Exception


class MainActivity : AppCompatActivity(){
    private  val RC_SIGN_IN = 9001
    lateinit var emailTextView: EditText
    lateinit var singin: SignInButton
    lateinit var progressbar: ProgressBar
    lateinit var passwordTextView: EditText
    lateinit var Btn: Button
    val TAG="welcome"
    val auth= FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onStart() {
        super.onStart()
        val user=auth.currentUser
        if (user!=null){
            val intent=Intent(applicationContext,MainActivity2::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_right ,android.R.anim.slide_out_right);
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginbtn=findViewById<TextView>(R.id.loginbtn)
        loginbtn.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right ,android.R.anim.slide_out_right);
        }


        createrequest()

        emailTextView = findViewById<EditText>(R.id.remail);

        passwordTextView = findViewById<EditText>(R.id.rpasswd);

        Btn = findViewById<Button>(R.id.btnregister);

        progressbar = findViewById<ProgressBar>(R.id.rprogressbar)

        singin = findViewById<SignInButton>(R.id.sign_in_button)

        Btn.setOnClickListener {
            registerNewUser()
        }

        singin.setOnClickListener { signIn() }

//        563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0
    }

    private fun registerNewUser() {
        progressbar.setVisibility(View.VISIBLE)
        val email: String
        val password: String
        email = emailTextView.getText().toString()
        password = passwordTextView.getText().toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,
                "Please enter email!!",
                Toast.LENGTH_LONG)
                .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,
                "Please enter password!!",
                Toast.LENGTH_LONG)
                .show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Registration successful!",Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE)

                    // if the user created intent to login activity

                    // if the user created intent to login activity
                    val intent = Intent(this,MainActivity2::class.java)
                    startActivity(intent)

                } else {

                   try{
                       throw task.exception!!
                   }
                   catch (e: Exception) {
                       progressbar.setVisibility(View.GONE)
                       Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()


                   }
                }
            }

    }


    fun createrequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("808991018362-tusl9d0l1pt4a064v88upa4ukitbpcnm.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progressbar.visibility=View.VISIBLE
        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("welcome", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("welcome", "Google sign in failed", e)
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        else{progressbar.visibility=View.GONE
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        Toast.makeText(this, "login successfully", Toast.LENGTH_SHORT).show()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
//                Toast.makeText(this, "login not successfully", Toast.LENGTH_SHORT).show()
                if (task.isSuccessful)
                {
//                    Toast.makeText(this, "login successfully", Toast.LENGTH_SHORT).show()
                    Log.d("welcome", "signInWithCredential:success")
                    val user = auth.currentUser
                    val intent=Intent(applicationContext,MainActivity2::class.java)
                    startActivity(intent)
                }
                else {

                    try{
                        throw task.exception!!
                    }
                    catch (e: Exception) {
                        progressbar.setVisibility(View.GONE)
                        Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()

//                        Log.e(TAG, e.message)
                    }

//                    !!.visibility=View.GONE
                }
            }

    }
}