package com.example.wallpaper
import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
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
import androidx.appcompat.app.AlertDialog
import com.example.wallpaper.wallpaper.MainActivity2
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.lang.Exception

import android.widget.Button;
import android.widget.Toast;


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
            val intent=Intent(applicationContext, MainActivity2::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_right ,android.R.anim.slide_out_right);
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        requestPermission()
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
                Log.d("welcome", "firebaseAuthWithGoogle:2" + account.id)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("welcome", "Google sign in failed", e)
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        else{progressbar.visibility=View.GONE
           }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Log.d("welcome", "firebaseAuthWithGoogle:3")
//        Toast.makeText(this, "login successfully", Toast.LENGTH_SHORT).show()
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                  if (task.isSuccessful)
                {
                    Toast.makeText(this, "login successfully", Toast.LENGTH_SHORT).show()
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
//    private fun requestPermission(){
//        Dexter.withContext(this).withPermissions(Manifest.permission.WRITE_SETTINGS,
//            Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_CONTACTS)
//            .withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
//                    if (p0?.areAllPermissionsGranted()!!) {
//                        // do you work now
//                        Toast.makeText(this@MainActivity, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
//                    }
//                    if (p0.isAnyPermissionPermanentlyDenied()) {
//                        // permission is denied permanently,
//                        // we will show user a dialog message.
//                        showSettingsDialog();
//                    }
//
//
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    p0: MutableList<PermissionRequest>?,
//                    p1: PermissionToken?
//                ) {
//                    p1!!.continuePermissionRequest();
//                }
//            }).withErrorListener { object :PermissionRequestErrorListener {
//                override fun onError(p0: DexterError?) {
//                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
//                }
//
//            }}.onSameThread().check();
//    }

    private fun showSettingsDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog, which -> // this method is called on click on positive
            // button and on clicking shit button we
            // are redirecting our user from our app to the
            // settings page of our app.
            dialog.cancel()
            // below is the intent from which we
            // are redirecting our user.
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> // this method is called when
            // user click on negative button.
            dialog.cancel()
        }

        builder.show();
        // below line is the title
        // for our alert dialog.

        // below line is the title
        // for our alert dialog.
        builder.setTitle("Need Permissions")

    }
}