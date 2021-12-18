package com.example.wallpaper

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.firebase.auth.FirebaseAuth

import java.lang.Exception
import android.view.MotionEvent

import android.view.View.OnTouchListener
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_wallpaper.*

import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_account.*

import com.google.firebase.auth.EmailAuthProvider

import android.widget.LinearLayout

import android.widget.ProgressBar








// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val TAG="ACCOUNT"

/**
 * A simple [Fragment] subclass.
 * Use the [accountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class accountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var builder: AlertDialog.Builder
    lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        val reenterpass=view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.renterpas)
        val inputemail=view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.textInputEmailre)
        val reenterpassword=view.findViewById<EditText>(R.id.reenterpassword)
        val profilepic=view.findViewById<ImageView>(R.id.profilepic)
        val email=view.findViewById<EditText>(R.id.changeemail)

        email.isFocusable = false;
        email.isFocusableInTouchMode = false;
        email.isClickable=false
        val user = FirebaseAuth.getInstance().currentUser
        val uid=FirebaseAuth.getInstance().uid

        val db = FirebaseFirestore.getInstance()
        val notres = db.collection("fav").document("fav")
        val password=view.findViewById<EditText>(R.id.changepassword)
//        password.isFocusable=false
        password.isFocusable = false;
        password.isFocusableInTouchMode = false;
        password.isClickable=false

        val change=view.findViewById<Button>(R.id.change)
        val   databaseReference = FirebaseDatabase.getInstance().getReference()

        try{
            val googleProfilePic = acct.getPhotoUrl().toString().replace("s96-c", "s492-c");
        val useemail=acct.email.toString()
            val useepass=acct.displayName.toString()
        email.setText(useemail)
        password.setText(useepass)
            inputemail.hint="User-Name"
            email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        Glide.with(this)
            .load(googleProfilePic).fitCenter()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                  return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {



                    //do something when picture already loaded
                    return false
                }
            })
            .into(profilepic)}
        catch (e:Exception) {
            Log.d(TAG, "onCreateView: i am in catch")


            email.setText(user!!.email.toString())






            password.setOnTouchListener(OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= password.right - password.compoundDrawables
                            .get(DRAWABLE_RIGHT).getBounds().width()
                    ) {
                        password.isFocusable = true;
                        // your action here
                        change.visibility=View.VISIBLE
                        reenterpass.visibility = View.VISIBLE
                        password.isFocusableInTouchMode=true
                        password.isClickable=true

                        inputemail.hint = "enter old pass"

                        password.isEnabled = true;
                        password.isCursorVisible = true;

                        return@OnTouchListener true
                    }
                }
                false
            })

            change.setOnClickListener {
                try{
                val credential = EmailAuthProvider
                    .getCredential(user!!.email!!, password.getText().toString())
                user!!.reauthenticate(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog = getDialogProgressBar("Changing...")!!.create();
                            progressDialog.show()
                            user.updatePassword(reenterpassword.text.toString())
                                .addOnCompleteListener { task ->

                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Password Changed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        password.text.clear()
                                        password.isFocusable = false;
                                        password.isFocusableInTouchMode = false;
                                        password.isClickable=false
                                        change.visibility=View.GONE
                                        reenterpass.visibility = View.GONE

                                        progressDialog.dismiss()
                                        loadsuccess()


                                    }
                                    else{
                                        progressDialog.dismiss()
                                        Toast.makeText(context, "not able to change", Toast.LENGTH_SHORT).show()
                                    }
                                }

                        } else {
                            // Password is incorrect
                            Toast.makeText(context, "password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            catch (e:Exception){
                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }}
        }
        val checkfav=view.findViewById<Button>(R.id.checkfav)
        val logout=view.findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent=Intent(context,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }
        checkfav.setOnClickListener {
            val intent=Intent(context,favourates::class.java)
            startActivity(intent)


        }








        return view
    }
    fun getDialogProgressBar(text:String): AlertDialog.Builder? {

            builder = AlertDialog.Builder(context)
            builder.setTitle(text)
            val progressBar = ProgressBar(context)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            progressBar.layoutParams = lp
            builder.setView(progressBar)

        return builder
    }
    fun loadsuccess(){
        AlertDialog.Builder(context)
            .setTitle("Success")
            .setMessage("Your password Successfully changed"!!) // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                // Continue with delete operation
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.no, null)

            .show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment accountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            accountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}